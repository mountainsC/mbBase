package com.cloud.core.okrx;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.annotations.ApiCheckAnnotation;
import com.cloud.core.events.Action1;
import com.cloud.core.okrx.properties.OkRxValidParam;
import com.cloud.core.utils.NetworkUtils;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/7
 * Description:okgo请求验证
 * Modifier:
 * ModifyContent:
 */
public class OkrxRequestValid {

    //方法名与参数暂存集合
    private HashMap<String, Class<?>[]> methedParams = new HashMap<>();

    /**
     * @param context
     * @param t
     * @param tokenAction
     * @param <T>
     * @return
     */
    public <T extends BaseService> OkRxValidParam check(Context context, T t, Action1<T> tokenAction) {
        OkRxValidParam validParam = new OkRxValidParam();
        if (context == null || t == null) {
            validParam.setFlag(false);
            return validParam;
        }
        if (t == null) {
            validParam.setFlag(false);
            return validParam;
        }
        String invokeMethodName = getInvokingMethodName();
        if (TextUtils.isEmpty(invokeMethodName)) {
            validParam.setFlag(false);
            return validParam;
        }
        Method method = null;
        if (methedParams.containsKey(invokeMethodName)) {
            try {
                Class<?>[] classes = methedParams.get(invokeMethodName);
                method = t.getClass().getMethod(invokeMethodName, classes);
            } catch (NoSuchMethodException e) {
                validParam.setFlag(false);
                return validParam;
            }
        }
        if (method != null) {
            if (!method.isAnnotationPresent(ApiCheckAnnotation.class)) {
                validParam.setFlag(false);
                return validParam;
            }
            methodValid(context, method, validParam, t, tokenAction);
            return validParam;
        }
        Method[] methods = t.getClass().getMethods();
        for (Method m : methods) {
            if (!TextUtils.equals(m.getName(), invokeMethodName)) {
                continue;
            }
            method = m;
            methedParams.put(invokeMethodName, m.getParameterTypes());
            break;
        }
        if (method != null) {
            if (!method.isAnnotationPresent(ApiCheckAnnotation.class)) {
                validParam.setFlag(false);
                return validParam;
            }
            methodValid(context, method, validParam, t, tokenAction);
            return validParam;
        }
        return validParam;
    }

    private <T extends BaseService> void methodValid(Context context, Method method, OkRxValidParam validParam, T t, Action1<T> tokenAction) {
        ApiCheckAnnotation apiCheckAnnotation = method.getAnnotation(ApiCheckAnnotation.class);
        BaseSubscriber subscriber = t.getBaseSubscriber();
        if (subscriber == null) {
            //若未注册请求定阅则结束请求
            validParam.setFlag(false);
            return;
        }
        validParam.setApiCheckAnnotation(apiCheckAnnotation);
        //检查网络
        if (apiCheckAnnotation.IsNetworkValid()) {
            if (NetworkUtils.isConnected(context)) {
                //拼接当前调用方法参数
                //拼接缓存key
                validParam.setCacheKey(String.format("%s_{0}_%s_%s", t.getClass().getSimpleName(), method.getName(), apiCheckAnnotation.CacheKey()));
                //验证并请求
                tokenValidReqProcess(context, apiCheckAnnotation, tokenAction, validParam, t);
            } else {
                //无网络结束请求
                validParam.setFlag(false);
            }
        } else {
            //若不进行网络检测，则直接请求
            tokenValidReqProcess(context, apiCheckAnnotation, tokenAction, validParam, t);
        }
    }

    private <T extends BaseService> void tokenValidReqProcess(Context context,
                                                              ApiCheckAnnotation apiCheckAnnotation,
                                                              Action1<T> tokenAction,
                                                              OkRxValidParam validParam,
                                                              T t) {
        if (apiCheckAnnotation.IsTokenValid()) {
            if (tokenAction == null) {
                //若需要token时tokenAction为空则结束请求
                validParam.setFlag(false);
            } else {
                tokenAction.call(t);
                if (TextUtils.isEmpty(t.getToken())) {
                    //若重新获取token后仍为空则跳至登录页
                    validParam.setFlag(false);
                    validParam.setNeedLogin(true);
                } else {
                    validParam.setNeedLogin(false);
                    //若重新获取token后仍不为空则通过验证
                    validParam.setFlag(true);
                }
            }
        } else {
            //当前接口不进行token验证则直接通过
            validParam.setFlag(true);
        }
    }

    /**
     * 获取调用方法名
     * <p>
     * return
     */
    public static String getInvokingMethodName() {
        Exception exception = new Exception();
        if (exception == null) {
            return "";
        }
        StackTraceElement[] stacks = exception.getStackTrace();
        if (ObjectJudge.isNullOrEmpty(stacks)) {
            return "";
        }
        String[] fms = {"getInvokingMethodName", "check", "requestObject"};
        List<String> fmslst = Arrays.asList(fms);
        String methodName = "";
        for (StackTraceElement stack : stacks) {
            if (fmslst.contains(stack.getMethodName())) {
                continue;
            } else {
                methodName = stack.getMethodName();
                break;
            }
        }
        return methodName;
    }
}
