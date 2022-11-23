package com.cloud.core.okrx;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.beans.BaseBean;
import com.cloud.core.beans.UnLoginCallInfo;
import com.cloud.core.configs.ApiConfig;
import com.cloud.core.configs.BaseCConfig;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.ResultState;
import com.cloud.core.events.Runnable1;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.events.OnResponseErrorListener;
import com.cloud.core.okrx.events.OnUnLoginCallInfoListener;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.StorageUtils;
import com.cloud.core.utils.ThreadPoolUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/6/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class BaseSubscriber<T extends BaseBean, BaseT extends BaseService> {

    private Context context = null;
    private BaseT baseT = null;
    private String reqKey = "";
    private String apiName = "";
    private Object extra = null;
    private List<String> allowRetCodes = new ArrayList<String>();
    private boolean isLoginValid = true;
    private List<String> apiUnloginRetCodes = new ArrayList<String>();
    /**
     * 启动登录页时间戳
     */
    public static long START_LOGIN_TIME_STMPT = 0;
    /**
     * 未登录接口对应的本地文件名
     */
    private static String API_UNLOGIN_FILE_NAME = "6cedf5f448c84b528f7cbbb72ac691d5.txt";
    private static String API_ERROR_FILE_NAME = "680b2ce7ba2e4873b9de9b7ce9d301d5";

    private OnUnLoginCallInfoListener onUnLoginCallInfoListener = null;

    /**
     * 是否对回调结果进行验证
     */
    private boolean isValidCallResult = true;
    //返回失败监听
    private OnResponseErrorListener errorListener = null;
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    private String apiRequestKey = "";
    private Handler mhandler = new Handler(Looper.getMainLooper());

    private void onCacheSuccessful(T t, ResultState resultState) {
        if (resultState == ResultState.Success) {
            onSuccessful(t);
            onSuccessful(t, reqKey);
            onSuccessful(t, reqKey, extra);
        }
        onSuccessful(resultState, t, reqKey, extra);
    }

    /**
     * 设置未登录回调监听
     *
     * @param listener 事件监听
     */
    public void setOnUnLoginCallInfoListener(OnUnLoginCallInfoListener listener) {
        this.onUnLoginCallInfoListener = listener;
    }

    /**
     * 获取未登录回调监听
     *
     * @return 事件监听
     */
    public OnUnLoginCallInfoListener getOnUnLoginCallInfoListener() {
        return this.onUnLoginCallInfoListener;
    }

    /**
     * 设置接口返回失败监听
     *
     * @param errorListener 事件监听
     */
    public void setErrorListener(OnResponseErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    /**
     * 对接口返回的结果是否需要进行验证
     *
     * @param isValidCallResult true-做相关验证及过滤;false-不处理;
     */
    public void setValidCallResult(boolean isValidCallResult) {
        this.isValidCallResult = isValidCallResult;
    }

    /**
     * 设置请求key
     *
     * @param reqKey 此key在回调中原样传回
     */
    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    /**
     * 设置api名称
     *
     * @param apiName 当前请求的api名称
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * 设置扩展数据
     *
     * @param extra 扩展数据
     */
    public void setExtra(Object extra) {
        this.extra = extra;
    }

    /**
     * 获取接口定义中的请允许验证通过的返回码
     *
     * @return 返回码集合
     */
    public List<String> getAllowRetCodes() {
        return this.allowRetCodes;
    }

    public void setLoginValid(boolean isLoginValid) {
        this.isLoginValid = isLoginValid;
    }

    public void setApiUnloginRetCodes(List<String> apiUnloginRetCodes) {
        this.apiUnloginRetCodes = apiUnloginRetCodes;
    }

    /**
     * API请求成功
     * <p>
     *
     * @param resultState
     * @param t
     * @param reqKey      请求唯一标识符
     * @param extra       额外数据
     */
    protected void onSuccessful(ResultState resultState, T t, String reqKey, Object extra) {

    }

    /**
     * API请求成功
     * <p>
     *
     * @param t
     * @param reqKey 请求唯一标识符
     * @param extra  额外数据
     */
    protected void onSuccessful(T t, String reqKey, Object extra) {

    }

    /**
     * API请求成功
     * <p>
     * param t
     * param reqKey 请求唯一标识符
     */
    protected void onSuccessful(T t, String reqKey) {

    }

    /**
     * API请求成功
     * <p>
     * param t
     */
    protected void onSuccessful(T t) {

    }

    public <ExtraT> BaseSubscriber(Context context, BaseT cls) {
        this.context = context;
        this.baseT = cls;
        if (baseT != null) {
            baseT.setBaseSubscriber(this);
        }
    }

    public void onCompleted() {
        if (baseT == null) {
            return;
        }
        if (ObjectJudge.isMainThread()) {
            baseT.onRequestCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    baseT.onRequestCompleted();
                }
            });
        }
    }

    public void onNext(T t, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiRequestKey) {
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
        ExecutorService fixThread = ThreadPoolUtils.fixThread();
        fixThread.execute(new ApiCallWith(t));
    }

    private class ResultParams {
        public T t;
        public ResultState state;
    }

    private void successWith(T t, ResultState state) {
        ResultParams resultParams = new ResultParams();
        resultParams.t = t;
        resultParams.state = state;
        Handler handler = ThreadPoolUtils.getMhandler();
        handler.post(new ApiHandlerRun(resultParams));
    }

    private class ApiHandlerRun implements Runnable {

        private ResultParams params = null;

        public ApiHandlerRun(ResultParams params) {
            this.params = params;
        }

        @Override
        public void run() {
            if (params == null) {
                return;
            }
            onCacheSuccessful(params.t, params.state);
            requestFinishWith();
        }
    }

    private void requestFinishWith() {
        if (reqQueueItemHashMap == null || TextUtils.isEmpty(apiRequestKey)) {
            return;
        }
        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setSuccess(true);//这里只作为临时变量，如果成功先回调那么在onRequestCompleted中有用;
            if (queueItem.isReqNetCompleted() && queueItem.isSuccess()) {
                onCompleted();
                reqQueueItemHashMap.remove(apiRequestKey);
            }
        }
    }

    private class ApiCallWith implements Runnable {

        private T t;

        public ApiCallWith(T t) {
            this.t = t;
        }

        @Override
        public void run() {
            try {
                if (t == null) {
                    successWith(t, ResultState.Fail);
                    requestFinishWith();
                    return;
                }
                if (TextUtils.isEmpty(apiName)) {
                    apiName = baseT.getApiName();
                }
                RxCoreConfigItems configItems = BaseCConfig.getInstance().getConfigItems(context);
                ApiConfig apiConfigs = configItems.getApiConfigs();
                List<String> apiSuccessRet = apiConfigs.getApiSuccessRet();
                List<String> apiSpecificNameFilter = apiConfigs.getApiSpecificNameFilter();
                if (TextUtils.isEmpty(t.getCode())) {
                    successWith(t, ResultState.Success);
                } else {
                    if (isValidCallResult) {
                        //需要做基本的回调验证
                        if (apiSuccessRet.contains(t.getCode()) ||
                                apiSpecificNameFilter.contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(t.getCode()))) {
                            successWith(t, ResultState.Success);
                        } else {
                            List<String> apiUnauthorizedRet = apiConfigs.getApiUnauthorizedRet();
                            if (apiUnauthorizedRet.contains(t.getCode())) {
                                unLoginSend(t);
                            } else {
                                failRemind(apiSuccessRet, t, apiConfigs);
                            }
                            successWith(t, ResultState.Fail);
                        }
                    } else {
                        //只作未登录验证
                        if (apiSuccessRet.contains(t.getCode()) ||
                                apiSpecificNameFilter.contains(apiName) ||
                                (allowRetCodes != null && allowRetCodes.contains(t.getCode()))) {
                            successWith(t, ResultState.Success);
                        } else {
                            List<String> apiUnauthorizedRet = apiConfigs.getApiUnauthorizedRet();
                            if (apiUnauthorizedRet.contains(t.getCode())) {
                                unLoginSend(t);
                                successWith(t, ResultState.Fail);
                            } else {
                                successWith(t, ResultState.None);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                successWith(t, ResultState.Fail);
                requestFinishWith();
                Logger.L.error(e);
            }
        }
    }

    private void failRemind(List<String> apiSuccessRet, T t, ApiConfig apiConfigs) {
        List<String> apiMessagePromptFilter = apiConfigs.getApiMessagePromptFilter();
        if (TextUtils.isEmpty(t.getCode()) ||
                !apiMessagePromptFilter.contains(t.getCode())) {
            if (context == null) {
                return;
            }
            recordAPIClassInfo(t, API_ERROR_FILE_NAME);
            if (apiSuccessRet.contains(t.getCode())) {
                return;
            }
            if (errorListener != null) {
                Handler handler = ThreadPoolUtils.getMhandler();
                handler.post(new Runnable1<T>(t) {
                    @Override
                    public void run() {
                        errorListener.onResponseError(t.getCode(), t.getMessage());
                    }
                });
            }
        }
    }

    private void unLoginSend(T t) {
        recordAPIClassInfo(t, API_UNLOGIN_FILE_NAME);
        if (baseT != null) {
            //请求token api请求中的token清空
            baseT.setToken("");
        }
        if (onUnLoginCallInfoListener != null) {
            Handler handler = ThreadPoolUtils.getMhandler();
            handler.post(new Runnable1<T>(t) {
                @Override
                public void run() {
                    UnLoginCallInfo callInfo = new UnLoginCallInfo();
                    callInfo.setApiName(apiName);
                    callInfo.setResponse(JsonUtils.toStr(t));
                    onUnLoginCallInfoListener.onCallInfo(callInfo);
                }
            });
        }
    }

    private void recordAPIClassInfo(T t, String fileName) {
        try {
            String className = t.getClass().getName();
            File tobeProcessedDir = StorageUtils.getTobeProcessed();
            File tobeFile = new File(tobeProcessedDir, fileName);
            StringBuffer unprocesssb = new StringBuffer();
            unprocesssb.append("\n");
            unprocesssb.append(apiName);
            unprocesssb.append("\n");
            unprocesssb.append(className);
            unprocesssb.append("\n");
            unprocesssb.append(JsonUtils.toStr(t));
            unprocesssb.append("\n");
            unprocesssb.append("--------------------------------------------------------------------------------------------------------------------");
            unprocesssb.append("\n");
            StorageUtils.appendContent(unprocesssb.toString(), tobeFile);
        } catch (Exception e) {
            Logger.L.warn(e.getMessage(), e);
        }
    }
}
