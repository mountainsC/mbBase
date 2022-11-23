package com.cloud.core.okrx.callback;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.OkAndroid;
import com.cloud.core.enums.RequestState;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.properties.ReqQueueItem;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/30
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class StringCallback implements Callback {

    //上下文
    private Context context = null;
    //处理成功回调
    private Action3<String, String, HashMap<String, ReqQueueItem>> successAction = null;
    //请求完成时回调(成功或失败)
    private Action1<RequestState> completeAction = null;
    //请求完成时输出日志
    private Action2<String, String> printLogAction = null;
    //请求标识队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    //请求标识
    private String apiRequestKey = "";
    //数据返回内容
    private String responseString = "";
    //api唯一标识
    private String apiUnique = "";
    //header回调
    private Action2<String, HashMap<String, String>> headersAction = null;

    protected abstract void onSuccessCall(String responseString);

    public StringCallback(Context context,
                          Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                          Action1<RequestState> completeAction,
                          Action2<String, String> printLogAction,
                          HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                          String apiRequestKey,
                          String apiUnique,
                          Action2<String, HashMap<String, String>> headersAction) {
        this.context = context;
        this.successAction = successAction;
        this.completeAction = completeAction;
        this.printLogAction = printLogAction;
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
        this.apiUnique = apiUnique;
        this.headersAction = headersAction;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setReqNetCompleted(true);
        }
        if (completeAction != null) {
            completeAction.call(RequestState.Error);
        }
    }

    private void headerDealWith(Response response) {
        if (TextUtils.isEmpty(apiUnique)) {
            return;
        }
        Headers headers = response.headers();
        if (headers == null) {
            return;
        }
        if (headersAction == null) {
            return;
        }
        OkAndroid.OkAndroidBuilder builder = OkAndroid.getInstance().getBuilder(context);
        String[] headerParamNames = builder.getHttpHeaderParamNames();
        if (ObjectJudge.isNullOrEmpty(headerParamNames)) {
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        for (String paramName : headerParamNames) {
            map.put(paramName, headers.get(paramName));
        }
        headersAction.call(apiUnique, map);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if (response == null || !response.isSuccessful()) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
            }
            headerDealWith(response);
            ResponseBody body = response.body();
            if (body == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
                return;
            }
            if (successAction != null) {
                responseString = body.string();
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap);

                onSuccessCall(responseString);

                //输出日志
                if (printLogAction != null) {
                    printLogAction.call(apiRequestKey, responseString);
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        } finally {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                queueItem.setReqNetCompleted(true);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
        }
    }
}
