package com.cloud.core.ebus;

import android.app.Activity;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.daos.EBusDataItemDao;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ThreadPoolUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/22
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class EBus {

    private static EBus eBus = null;
    //className-values(key-EBusItem)
    private static HashMap<String, HashMap<String, EBusItem>> subMethods = new HashMap<String, HashMap<String, EBusItem>>();

    public static EBus getInstance() {
        return eBus == null ? eBus = new EBus() : eBus;
    }

    /**
     * 注册EBus订阅
     *
     * @param subscriber 适用于类、Activity、Fragment等
     */
    public void registered(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        Class<?> sub = subscriber.getClass();
        String classType = "";
        if (subscriber instanceof Activity) {
            classType = "Activity";
        } else if (subscriber instanceof Fragment) {
            classType = "Fragment";
        }
//        ThreadPoolUtils.fixThread().execute(new RegisterRunable(sub, subscriber, classType));
        RegisterRunable runable = new RegisterRunable(sub, subscriber, classType);
        runable.run();
    }

    private class RegisterRunable implements Runnable {

        private Class<?> cls = null;
        private Object subscriber = null;
        private String classType = "";

        public RegisterRunable(Class<?> cls, Object subscriber, String classType) {
            this.cls = cls;
            this.subscriber = subscriber;
            this.classType = classType;
        }

        @Override
        public void run() {
            try {
                List<SubscribeEBus> subscribeEBuses = new ArrayList<SubscribeEBus>();
                getRegisteredAnnons(cls, subscriber, subscribeEBuses);
                //cacheAnnonInfo(cls.getName(), classType, subscribeEBuses);
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    }

    /**
     * 反注册EBus订阅
     * (一般在onDestory中调用)
     *
     * @param subscriber 适用于类、Activity、Fragment等
     */
    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        Class<?> sub = subscriber.getClass();
        ThreadPoolUtils.fixThread().execute(new UnRegisterRunable(sub));
    }

    private class UnRegisterRunable implements Runnable {

        private Class<?> cls = null;

        public UnRegisterRunable(Class<?> cls) {
            this.cls = cls;
        }

        @Override
        public void run() {
            try {
                //移除集合中数据
                String className = cls.getName();
                if (subMethods.containsKey(className)) {
                    subMethods.remove(className);
                }
                //移除缓存中数据
                //removeCacheDatas(className);
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    }

    private void removeCacheDatas(String className) {
        EBusDataCacheDao dataCacheDao = new EBusDataCacheDao();
        EBusDataItemDao dataItemDao = dataCacheDao.getEBusDataItemDao();
        if (dataItemDao == null) {
            return;
        }
        QueryBuilder<EBusDataItem> builder = dataItemDao.queryBuilder();
        builder.where(EBusDataItemDao.Properties.Key.like(className));
        List<EBusDataItem> list = builder.build().list();
        if (ObjectJudge.isNullOrEmpty(list)) {
            return;
        }
        dataItemDao.deleteInTx(list);
    }

    private void cacheAnnonInfo(String className, String classType, List<SubscribeEBus> subscribeEBuses) {
        HashMap<String, EBusDataItem> dataItems = new HashMap<String, EBusDataItem>();
        for (SubscribeEBus bus : subscribeEBuses) {
            if (dataItems.containsKey(bus.receiveKey())) {
                continue;
            }
            EBusDataItem item = new EBusDataItem();
            item.setKey(className + bus.receiveKey());
            item.setClassName(className);
            item.setReceiveKey(bus.receiveKey());
            item.setThreadMode(bus.threadMode().name());
            item.setClassType(classType);
            dataItems.put(bus.receiveKey(), item);
        }
        //缓存数据
        EBusDataCacheDao dataCacheDao = new EBusDataCacheDao();
        EBusDataItemDao dataItemDao = dataCacheDao.getEBusDataItemDao();
        if (dataItemDao != null) {
            EBusDataItemDao.createTable(dataItemDao.getDatabase(), true);
            dataItemDao.insertOrReplaceInTx(dataItems.values());
        }
    }

    private void getRegisteredAnnons(Class<?> cls, Object subscriber, List<SubscribeEBus> subscribeEBuses) {
        Method[] methods = cls.getMethods();
        if (ObjectJudge.isNullOrEmpty(methods)) {
            return;
        }
        HashMap<String, EBusItem> map = null;
        String clsName = cls.getName();
        if (subMethods.containsKey(clsName)) {
            map = subMethods.get(clsName);
        }
        if (map == null) {
            map = new HashMap<String, EBusItem>();
        }
        for (Method method : methods) {
            if (!method.isAnnotationPresent(SubscribeEBus.class)) {
                continue;
            }
            SubscribeEBus annotation = method.getAnnotation(SubscribeEBus.class);
            subscribeEBuses.add(annotation);
            //添加key-method
            if (map.containsKey(annotation.receiveKey())) {
                continue;
            }
            EBusItem eBusItem = new EBusItem();
            eBusItem.setThreadMode(annotation.threadMode());
            eBusItem.setMethod(method);
            eBusItem.setSubscriber(subscriber);
            map.put(annotation.receiveKey(), eBusItem);
        }
        subMethods.put(clsName, map);
    }

    private void sendPosts(String receiveKey, Object[] args) {
        for (Map.Entry<String, HashMap<String, EBusItem>> entry : subMethods.entrySet()) {
            HashMap<String, EBusItem> value = entry.getValue();
            if (value == null || !value.containsKey(receiveKey)) {
                continue;
            }
            EBusItem busItem = value.get(receiveKey);
            if (busItem == null) {
                continue;
            }
            Method method = busItem.getMethod();
            if (method == null || busItem.getSubscriber() == null) {
                continue;
            }
            try {
                if (busItem.getThreadMode() == null || busItem.getThreadMode() == ThreadModeEBus.MAIN) {
                    busItem.setArgs(args);
                    mhandler.obtainMessage(5000, busItem).sendToTarget();
                } else {
                    method.invoke(busItem.getSubscriber(), args);
                }
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    }

    private class SendPostRunable implements Runnable {

        private String receiveKey = "";
        private Object[] event;

        public SendPostRunable(String receiveKey, Object[] event) {
            this.receiveKey = receiveKey;
            this.event = event;
        }

        @Override
        public void run() {
            try {
                sendPosts(receiveKey, event);
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    }

    /**
     * Posts the given event to the event bus
     *
     * @param receiveKey 接收key
     * @param event      事件参数(将作为接收方法传入)
     */
    public void post(String receiveKey, Object... event) {
        if (TextUtils.isEmpty(receiveKey)) {
            return;
        }
        ThreadPoolUtils.fixThread().execute(new SendPostRunable(receiveKey, event));
    }

    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 5000) {
                if (msg.obj == null || !(msg.obj instanceof EBusItem)) {
                    return;
                }
                EBusItem busItem = (EBusItem) msg.obj;
                Method method = busItem.getMethod();
                try {
                    method.invoke(busItem.getSubscriber(), busItem.getArgs());
                } catch (Exception e) {
                    Logger.L.error(e);
                }
            }
        }
    };
}
