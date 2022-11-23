package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.annotations.APIUrlInterfaceClass;
import com.cloud.core.annotations.ReturnCodeFilter;
import com.cloud.core.beans.BaseDataBean;
import com.cloud.core.beans.RetrofitParams;
import com.cloud.core.beans.UnLoginCallInfo;
import com.cloud.core.enums.ActivityNames;
import com.cloud.core.enums.ResultState;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Func1;
import com.cloud.core.events.Func2;
import com.cloud.core.events.OnRequestApiUrl;
import com.cloud.core.okrx.BaseService;
import com.cloud.core.okrx.BaseSubscriber;
import com.cloud.core.okrx.OkrxRequestValid;
import com.cloud.core.okrx.events.OnResponseErrorListener;
import com.cloud.core.okrx.events.OnSuccessfulListener;
import com.cloud.core.okrx.events.OnUnLoginCallInfoListener;
import com.cloud.core.okrx.properties.OkRxValidParam;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.RoutesUtils;
import com.cloud.core.utils.ToastUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/25
 * @Description:接口请求服务基类
 * @Modifier:
 * @ModifyContent:
 */
public class BaseOkrxService extends BaseService {

    private HashMap<String, String> keyUrls = new HashMap<String, String>();
    private ReturnCodeFilter codeFilter = null;

    private <S extends BaseService> String getBaseUrls(S server, String apiUrlTypeName) {
        if (keyUrls.containsKey(apiUrlTypeName)) {
            String url = keyUrls.get(apiUrlTypeName);
            if (TextUtils.isEmpty(url)) {
                return getAnnonBaseUrl(server, apiUrlTypeName);
            } else {
                return url;
            }
        } else {
            return getAnnonBaseUrl(server, apiUrlTypeName);
        }
    }

    private void getServerAPIUrlAnnon(Class<?> cls, APIUrlInterfaceClass[] apiUrlInterfaceClasss) {
        Annotation[] annotations = cls.getDeclaredAnnotations();
        if (ObjectJudge.isNullOrEmpty(annotations)) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass != null) {
                getServerAPIUrlAnnon(superclass, apiUrlInterfaceClasss);
            }
        } else {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == APIUrlInterfaceClass.class) {
                    apiUrlInterfaceClasss[0] = (APIUrlInterfaceClass) annotation;
                    break;
                }
            }
            if (apiUrlInterfaceClasss[0] == null) {
                Class<?> superclass = cls.getSuperclass();
                if (superclass != null) {
                    getServerAPIUrlAnnon(superclass, apiUrlInterfaceClasss);
                }
            }
        }
    }

    private <S extends BaseService> String getAnnonBaseUrl(S server, String apiUrlTypeName) {
        Class<? extends BaseService> cls = server.getClass();
        APIUrlInterfaceClass[] apiUrlInterfaceClasss = new APIUrlInterfaceClass[1];
        getServerAPIUrlAnnon(cls, apiUrlInterfaceClasss);
        if (apiUrlInterfaceClasss[0] == null) {
            return "";
        }
        Object apiObj = JsonUtils.newNull(apiUrlInterfaceClasss[0].value());
        if (apiObj == null || !(apiObj instanceof OnRequestApiUrl)) {
            return "";
        }
        OnRequestApiUrl requestApiUrl = (OnRequestApiUrl) apiObj;
        String url = requestApiUrl.onBaseUrl(apiUrlTypeName);
        keyUrls.put(apiUrlTypeName, url);
        return url;
    }

    private <S extends BaseService> ReturnCodeFilter getCodeFilter(S server) {
        if (codeFilter == null) {
            return getAnnonCodeFilter(server);
        } else {
            return codeFilter;
        }
    }

    private void getReturnCodeFilterAnnon(Class<?> cls, ReturnCodeFilter[] returnCodeFilters) {
        Annotation[] annotations = cls.getDeclaredAnnotations();
        if (ObjectJudge.isNullOrEmpty(annotations)) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass != null) {
                getReturnCodeFilterAnnon(superclass, returnCodeFilters);
            }
        } else {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ReturnCodeFilter.class) {
                    returnCodeFilters[0] = (ReturnCodeFilter) annotation;
                    break;
                }
            }
            if (returnCodeFilters[0] == null) {
                Class<?> superclass = cls.getSuperclass();
                if (superclass != null) {
                    getReturnCodeFilterAnnon(superclass, returnCodeFilters);
                }
            }
        }
    }

    private <S extends BaseService> ReturnCodeFilter getAnnonCodeFilter(S server) {
        Class<? extends BaseService> cls = server.getClass();
        ReturnCodeFilter[] returnCodeFilters = new ReturnCodeFilter[1];
        getReturnCodeFilterAnnon(cls, returnCodeFilters);
        if (returnCodeFilters[0] != null) {
            codeFilter = returnCodeFilters[0];
        }
        return returnCodeFilters[0];
    }

    /**
     * 网络请求配置
     *
     * @param context        上下文
     * @param apiClass       接口定义类Class
     * @param server         请求接口服务类，一般传this
     * @param baseSubscriber 数据返回订阅器
     * @param decApiAction   定义接口回调方法，用来调用apiClass中的接口
     * @param <I>            接口泛型类
     * @param <S>            接口服务泛型类
     */
    public <I, S extends BaseService> void requestObject(final Context context,
                                                         Class<I> apiClass,
                                                         S server,
                                                         BaseSubscriber baseSubscriber,
                                                         Func1<I, RetrofitParams> decApiAction) {
        //网络请求前检查，网络、token以及是否需要缓存
        OkrxRequestValid requestValid = new OkrxRequestValid();
        OkRxValidParam validParam = requestValid.check(context, server, new Action1<S>() {
            @Override
            public void call(S t1) {
                t1.setToken(UserDataCache.getDefault().getCacheUserInfo(context).getToken());
            }
        });
        validParam.setReturnCodeFilter(getCodeFilter(server));
        //请求接口
        requestObject(context, apiClass, server, baseSubscriber, validParam, new Func2<String, S, String>() {
                    @Override
                    public String call(S server, String apiUrlTypeName) {
                        return getBaseUrls(server, apiUrlTypeName);
                    }
                },
                decApiAction,
                new OnUnLoginCallInfoListener() {
                    @Override
                    public void onCallInfo(UnLoginCallInfo unLoginCallInfo) {
                        //清除本地登录信息
                        UserDataCache.getDefault().clearCacheUserInfo(context, null);
                        //跳转至登录页面
                        RoutesUtils.startActivity(context, ActivityNames.LoginActivity, null);
                    }
                },
                new OnResponseErrorListener() {
                    @Override
                    public void onResponseError(String code, String message) {
                        ToastUtils.showLong(context, message);
                    }
                });
    }

    private <T extends BaseDataBean, DT> void beenCall(OnSuccessfulListener<DT> listener, T t, T datat, ResultState resultState, String reqKey, Object extras) {
        datat.setCode(t.getCode());
        datat.setMessage(t.getMessage());
        if (resultState != null) {
            listener.onSuccessful((DT) datat, resultState, reqKey, extras);
            return;
        }
        listener.onSuccessful((DT) datat, reqKey, extras);
        listener.onSuccessful((DT) datat, reqKey);
    }

    private <DT> void otherCall(OnSuccessfulListener<DT> listener, Object data, ResultState resultState, String reqKey, Object extras) {
        if (resultState != null) {
            listener.onSuccessful((DT) data, resultState, reqKey, extras);
            return;
        }
        listener.onSuccessful((DT) data, reqKey, extras);
        listener.onSuccessful((DT) data, reqKey);
    }

    /**
     * 接口成功后回调
     *
     * @param listener    回调监听
     * @param t           接口返回数据对象
     * @param resultState 接口自定义回调时返回的状态类型
     * @param reqKey      返回接口请求时输入的标识符
     * @param extras      返回接口请求时输入的数据
     * @param <T>         接口返回数据对象类型
     * @param <DT>        自定义返回数据类型,即OnSuccessfulListener中的参数类型
     */
    protected <T extends BaseDataBean, DT> void bindCall(OnSuccessfulListener<DT> listener, T t, ResultState resultState, String reqKey, Object extras) {
        if (listener == null || t == null) {
            return;
        }
        Object data = t.getData();
        if (data instanceof BaseDataBean) {
            T datat = (T) data;
            beenCall(listener, t, datat, resultState, reqKey, extras);
        } else {
            Class<? extends BaseDataBean> tClass = t.getClass();
            if (tClass.getSuperclass() == BaseDataBean.class) {
                //如果未定义任何字段则判断data是否为空,若为空则直接返回data值,若有定义其它字段则返回t
                if (checkDataFieldNumber(t) > 0) {
                    otherCall(listener, t, resultState, reqKey, extras);
                } else {
                    //若data==null则不处理，反之返回data数据
                    if (checkCallMethodDataParamType(listener)) {
                        if (data != null) {
                            otherCall(listener, data, resultState, reqKey, extras);
                        }
                    } else {
                        otherCall(listener, t, resultState, reqKey, extras);
                    }
                }
            } else {
                if (checkCallMethodDataParamType(listener)) {
                    if (data != null) {
                        otherCall(listener, data, resultState, reqKey, extras);
                    }
                } else {
                    otherCall(listener, t, resultState, reqKey, extras);
                }
            }
        }
    }

    private <T extends BaseDataBean> int checkDataFieldNumber(T t) {
        int count = 0;
        Class<? extends BaseDataBean> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        if (!ObjectJudge.isNullOrEmpty(fields)) {
            //需要排除属性
            List<String> removeFields = new ArrayList<String>();
            removeFields.add("serialVersionUID");
            removeFields.add("$change");
            for (Field field : fields) {
                if (removeFields.contains(field.getName())) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    private <DT> boolean checkCallMethodDataParamType(OnSuccessfulListener<DT> listener) {
        boolean flag = true;
        Class<? extends OnSuccessfulListener> listenerClass = listener.getClass();
        Method[] methods = listenerClass.getDeclaredMethods();
        for (Method method : methods) {
            Type[] parameterTypes = method.getGenericParameterTypes();
            if (ObjectJudge.isNullOrEmpty(parameterTypes)) {
                continue;
            }
            Type dataType = parameterTypes[0];
            if (dataType == BaseDataBean.class) {
                flag = false;
            } else {
                if (dataType instanceof Class) {
                    Class superclass = ((Class) dataType).getSuperclass();
                    if (superclass == BaseDataBean.class) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 接口成功后回调
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param reqKey   返回接口请求时输入的标识符
     * @param extras   返回接口请求时输入的数据
     * @param <T>      接口返回数据对象类型
     * @param <DT>     自定义返回数据类型,即OnSuccessfulListener中的参数类型
     */
    protected <T extends BaseDataBean, DT> void bindCall(OnSuccessfulListener<DT> listener, T t, String reqKey, Object extras) {
        bindCall(listener, t, null, reqKey, extras);
    }

    /**
     * 接口成功后回调
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param reqKey   返回接口请求时输入的标识符
     * @param <T>      接口返回数据对象类型
     * @param <DT>     自定义返回数据类型,即OnSuccessfulListener中的参数类型
     */
    protected <T extends BaseDataBean, DT> void bindCall(OnSuccessfulListener<DT> listener, T t, String reqKey) {
        bindCall(listener, t, reqKey, null);
    }

    /**
     * 接口成功后回调
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param <T>      接口返回数据对象类型
     * @param <DT>     自定义返回数据类型,即OnSuccessfulListener中的参数类型
     */
    protected <T extends BaseDataBean, DT> void bindCall(OnSuccessfulListener<DT> listener, T t) {
        bindCall(listener, t, "");
    }

    /**
     * 接口成功后回调(用于BaseMibaoBean中data类型会根据请求条件改变而改变的)
     *
     * @param listener    回调监听
     * @param t           接口返回数据对象
     * @param resultState 接口自定义回调时返回的状态类型
     * @param reqKey      返回接口请求时输入的标识符
     * @param extras      返回接口请求时输入的数据
     * @param <T>         接口返回数据对象类型
     */
    protected <T extends BaseDataBean> void bindVariableCall(OnSuccessfulListener<T> listener, T t, ResultState resultState, String reqKey, Object extras) {
        if (listener == null || t == null) {
            return;
        }
        Object data = t.getData();
        if (data instanceof BaseDataBean) {
            T datat = (T) data;
            beenCall(listener, t, datat, resultState, reqKey, extras);
        } else {
            otherCall(listener, t, resultState, reqKey, extras);
        }
    }

    /**
     * 接口成功后回调(用于BaseMibaoBean中data类型会根据请求条件改变而改变的)
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param reqKey   返回接口请求时输入的标识符
     * @param extras   返回接口请求时输入的数据
     * @param <T>      接口返回数据对象类型
     */
    protected <T extends BaseDataBean> void bindVariableCall(OnSuccessfulListener<T> listener, T t, String reqKey, Object extras) {
        bindVariableCall(listener, t, null, reqKey, extras);
    }

    /**
     * 接口成功后回调(用于BaseMibaoBean中data类型会根据请求条件改变而改变的)
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param reqKey   返回接口请求时输入的标识符
     * @param <T>      接口返回数据对象类型
     */
    protected <T extends BaseDataBean> void bindVariableCall(OnSuccessfulListener<T> listener, T t, String reqKey) {
        bindVariableCall(listener, t, reqKey, null);
    }

    /**
     * 接口成功后回调(用于BaseMibaoBean中data类型会根据请求条件改变而改变的)
     *
     * @param listener 回调监听
     * @param t        接口返回数据对象
     * @param <T>      接口返回数据对象类型
     */
    protected <T extends BaseDataBean> void bindVariableCall(OnSuccessfulListener<T> listener, T t) {
        bindVariableCall(listener, t, "");
    }
}
