package com.cloud.core.behavior;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.core.ObjectJudge;
import com.cloud.core.bases.BaseApplication;
import com.cloud.core.configs.RxCloud;
import com.cloud.core.configs.RxNames;
import com.cloud.core.events.HookEvent;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.StorageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/15
 * @Description:行为统计工具类
 * @Modifier:
 * @ModifyContent:
 */
public class BehaviorUtils {

    private static BehaviorUtils behaviorUtils = null;
    private HashMap<String, HashMap<String, EventItem>> behaviorDataItems = new HashMap<String, HashMap<String, EventItem>>();
    private HashMap<Integer, ControllItem> ctrlIdsMapper = new HashMap<Integer, ControllItem>();
    private HashMap<String, EventItem> instantStatList = new HashMap<String, EventItem>();
    private HashMap<Integer, BehaviorMapItem> dataMap = new HashMap<Integer, BehaviorMapItem>();
    private HandlerThread muithread = null;
    private Handler muithreadhandler = null;
    private UITouchRunnable uiTouchRunnable = null;
    private Class<?> pageCls = null;
    private Context context = null;

    public BehaviorUtils() {
        muithread = new HandlerThread(UITouchRunnable.class.getSimpleName());
        muithread.start();
    }

    public static BehaviorUtils getInstance() {
        return behaviorUtils == null ? behaviorUtils = new BehaviorUtils() : behaviorUtils;
    }

    public void setOnBehaviorEventStatistics(View v, int eventId) {
        try {
            if (eventId == 0 || pageCls == null) {
                return;
            }
            String className = pageCls.getName();
            if (TextUtils.isEmpty(className)) {
                return;
            }
            if (!behaviorDataItems.containsKey(className)) {
                return;
            }
            if (!ctrlIdsMapper.containsKey(eventId)) {
                return;
            }
            ControllItem controllItem = ctrlIdsMapper.get(eventId);
            if (controllItem == null || TextUtils.isEmpty(controllItem.getCtrlIdName())) {
                return;
            }
            HashMap<String, EventItem> eventItems = behaviorDataItems.get(className);
            if (ObjectJudge.isNullOrEmpty(eventItems) || !eventItems.containsKey(controllItem.getCtrlIdName())) {
                return;
            }
            EventItem eventItem = eventItems.get(controllItem.getCtrlIdName());
            if (eventItem == null || TextUtils.isEmpty(eventItem.getBehaviorEventId())) {
                return;
            }
            instantStatList.put(eventItem.getEventId(), eventItem);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private class UITouchRunnable implements Runnable {

        private View view = null;

        private UITouchRunnable(View view) {
            this.view = view;
        }

        @Override
        public void run() {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAssetsBehaviorConfigs(view);
                    if (!ObjectJudge.isNullOrEmpty(behaviorDataItems)) {
                        addViewTouchStatistics(view);
                    }
                }
            }, 50);
        }
    }

    private void addViewTouchStatistics(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            if (ctrlIdsMapper.containsKey(group.getId())) {
                hookEvent.didHook(group);
            }
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View childAt = group.getChildAt(i);
                addViewTouchStatistics(childAt);
            }
        } else {
            if (ctrlIdsMapper.containsKey(view.getId())) {
                hookEvent.didHook(view);
            }
        }
    }

    /**
     * 行为统计预备处理
     *
     * @param view 要进行touch绑定的视图
     */
    public void setStatisticsPreper(View view) {
        try {
            if (view == null) {
                return;
            }
            muithreadhandler = new Handler(muithread.getLooper());
            uiTouchRunnable = new UITouchRunnable(view);
            muithreadhandler.post(uiTouchRunnable);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private HookEvent hookEvent = new HookEvent() {
        @Override
        protected void onPreClick(View v) {
            if (v.getId() == -1) {
                return;
            }
            setOnBehaviorEventStatistics(v, v.getId());
        }

        @Override
        protected void onAfterClick(View v) {
            int eventId = v.getId();
            if (eventId == -1) {
                return;
            }
            String classNameUrl = pageCls.getName();
            if (dataMap.containsKey(eventId)) {
                BehaviorMapItem mapItem = dataMap.get(eventId);
                eventStatistics(v, eventId, classNameUrl, mapItem.getMap(), mapItem.getDu());
            } else {
                eventStatistics(v, eventId, classNameUrl, null, 0);
            }
        }
    };

    private void getAssetsBehaviorConfigs(View parent) {
        try {
            if (context == null) {
                return;
            }
            if (ObjectJudge.isNullOrEmpty(behaviorDataItems)) {
                String fileName = "";
                RxNames names = RxCloud.getInstance().getNames();
                if (TextUtils.isEmpty(names.getBehaviorStatConfigFileName())) {
                    fileName = "behavior_event_relationship.json";
                } else {
                    fileName = names.getBehaviorStatConfigFileName();
                }
                String configContent = StorageUtils.readAssetsFileContent(context, fileName);
                if (!TextUtils.isEmpty(configContent)) {
                    ctrlIdsMapper.clear();
                    List<HashMap> plist = JsonUtils.parseArray(String.format("[%s]", configContent), HashMap.class);
                    if (!ObjectJudge.isNullOrEmpty(plist)) {
                        String packageName = context.getPackageName();
                        HashMap<String, HashMap<String, EventItem>> hashMap = plist.get(0);
                        for (Map.Entry<String, HashMap<String, EventItem>> mapEntry : hashMap.entrySet()) {
                            String eventListJson = JsonUtils.toStr(mapEntry.getValue());
                            List<HashMap> elst = JsonUtils.parseArray(String.format("[%s]", eventListJson), HashMap.class);
                            HashMap<String, EventItem> evalue = (HashMap<String, EventItem>) elst.get(0);
                            if (evalue != null) {
                                HashMap<String, EventItem> enewvalue = new HashMap<String, EventItem>();
                                //添加id与名称映射关系
                                for (Map.Entry<String, EventItem> entry : evalue.entrySet()) {
                                    //获取控件id
                                    int id = context.getResources().getIdentifier(entry.getKey(), "id", packageName);
                                    //获取视图
                                    View child = parent.findViewById(id);
                                    ControllItem controllItem = new ControllItem();
                                    controllItem.setCtrlIdName(entry.getKey());
                                    controllItem.setView(child);
                                    ctrlIdsMapper.put(id, controllItem);
                                    String itemjson = JsonUtils.toStr(entry.getValue());
                                    EventItem eventItem = JsonUtils.parseT(itemjson, EventItem.class);
                                    enewvalue.put(entry.getKey(), eventItem);
                                }
                                behaviorDataItems.put(mapEntry.getKey(), enewvalue);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
            return;
        } finally {
            if (behaviorDataItems == null) {
                behaviorDataItems = new HashMap<String, HashMap<String, EventItem>>();
            }
        }
    }

    public <T> void onStart(T pager) {
        OnBehaviorStatistics behaviorStatistics = BaseApplication.getInstance().getOnBehaviorStatistics();
        if (behaviorStatistics != null) {
            behaviorStatistics.onPageStatistics(pager, LifeCycleStatus.Start);
        }
    }

    public <T> void onPause(T pager) {
        OnBehaviorStatistics behaviorStatistics = BaseApplication.getInstance().getOnBehaviorStatistics();
        if (behaviorStatistics != null) {
            behaviorStatistics.onPageStatistics(pager, LifeCycleStatus.Pause);
        }
    }

    public <T> void onResume(T pager) {
        OnBehaviorStatistics behaviorStatistics = BaseApplication.getInstance().getOnBehaviorStatistics();
        if (behaviorStatistics != null) {
            behaviorStatistics.onPageStatistics(pager, LifeCycleStatus.Resume);
        }
    }

    public <T> void onCreate(Context context, T pager, Class<?> pageCls) {
        this.pageCls = pageCls;
        this.context = context;
        OnBehaviorStatistics behaviorStatistics = BaseApplication.getInstance().getOnBehaviorStatistics();
        if (behaviorStatistics != null) {
            behaviorStatistics.onPageStatistics(pager, LifeCycleStatus.Create);
        }
    }

    public <T> void onDestory(T pager) {
        OnBehaviorStatistics behaviorStatistics = BaseApplication.getInstance().getOnBehaviorStatistics();
        if (behaviorStatistics != null) {
            behaviorStatistics.onPageStatistics(pager, LifeCycleStatus.Destroy);
        }
        if (muithreadhandler != null && uiTouchRunnable != null) {
            muithreadhandler.removeCallbacks(uiTouchRunnable);
        }
    }

    private EventItem getInstantStatItem(int eventId) {
        EventItem eventItem = null;
        if (ctrlIdsMapper.containsKey(eventId)) {
            ControllItem controllItem = ctrlIdsMapper.get(eventId);
            if (instantStatList.containsKey(controllItem.getCtrlIdName())) {
                eventItem = instantStatList.get(controllItem.getCtrlIdName());
            }
        }
        return eventItem;
    }

    private void removeInstantStatItem(int eventId) {
        try {
            if (ctrlIdsMapper.containsKey(eventId)) {
                ControllItem controllItem = ctrlIdsMapper.get(eventId);
                if (instantStatList.containsKey(controllItem.getCtrlIdName())) {
                    instantStatList.remove(controllItem.getCtrlIdName());
                }
            }
            if (dataMap.containsKey(eventId)) {
                dataMap.remove(eventId);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void behaviorEventStatistics(View v, String eventId, String classNameUrl, BehaviorType behaviorType, HashMap<String, String> map, int du) {
        if (context == null) {
            return;
        }
        OnBehaviorEventStatistics eventStatistics = BaseApplication.getInstance().getOnBehaviorEventStatistics();
        if (eventStatistics != null) {
            eventStatistics.onEvent(context, v, eventId, classNameUrl, behaviorType, map, du);
        }
    }

    public void reportStatisticsMap(View v, HashMap<String, String> map, int du) {
        if (v.getId() == -1) {
            return;
        }
        BehaviorMapItem mapItem = new BehaviorMapItem();
        mapItem.setMap(map);
        mapItem.setDu(du);
        dataMap.put(v.getId(), mapItem);
    }

    private void eventStatistics(View v, int eventId, String classNameUrl, HashMap<String, String> map, int du) {
        try {
            if (context == null) {
                return;
            }
            EventItem eventItem = getInstantStatItem(eventId);
            if (eventItem == null || !ctrlIdsMapper.containsKey(eventId)) {
                return;
            }
            ControllItem controllItem = ctrlIdsMapper.get(eventId);
            if (controllItem == null) {
                return;
            }
            if (du > 0) {
                if (ObjectJudge.isNullOrEmpty(map)) {
                    behaviorEventStatistics(controllItem.getView(), eventItem.getBehaviorEventId(), classNameUrl, BehaviorType.Count, map, du);
                } else {
                    behaviorEventStatistics(controllItem.getView(), eventItem.getBehaviorEventId(), classNameUrl, BehaviorType.Numerical, map, du);
                }
            } else {
                if (ObjectJudge.isNullOrEmpty(map)) {
                    behaviorEventStatistics(controllItem.getView(), eventItem.getBehaviorEventId(), classNameUrl, BehaviorType.Count, map, du);
                } else {
                    behaviorEventStatistics(controllItem.getView(), eventItem.getBehaviorEventId(), classNameUrl, BehaviorType.Properties, map, du);
                }
            }
            removeInstantStatItem(eventId);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
}
