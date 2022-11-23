package com.cloud.core.okrx.requests;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RequestType;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.OkRx;
import com.cloud.core.okrx.callback.StringCallback;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.ValidUtils;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class OkRxGetRequest extends BaseRequest {

    private String responseString = "";

    /**
     * get请求回调
     *
     * @param context             context
     * @param url                 请求完整路径
     * @param headers             请求头信息
     * @param params              请求参数
     * @param isCache             true-缓存;false-不缓存;
     * @param cacheKey            缓存键值
     * @param cacheTime           缓存时间
     * @param successAction       成功回调
     * @param completeAction      完成回调
     * @param printLogAction      日志输出回调
     * @param apiRequestKey       api请求标识
     * @param reqQueueItemHashMap 请求标识队列
     */
    @Override
    public void call(final Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     final boolean isCache,
                     final String cacheKey,
                     final long cacheTime,
                     final Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                     final Action1<RequestState> completeAction,
                     final Action2<String, String> printLogAction,
                     final String apiRequestKey,
                     final HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        if (context == null || !ValidUtils.valid(RuleParams.Url.getValue(), url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }
        if (isCache) {
            String cache = getRequestCache(context, cacheKey);
            if (successAction != null && !TextUtils.isEmpty(cache)) {
                responseString = cache;
                successAction.call(responseString, apiRequestKey, reqQueueItemHashMap);
                return;
            }
        }
        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, params).get();
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient(context);
        client.newCall(request).enqueue(new StringCallback(context, successAction, completeAction, printLogAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction) {
            @Override
            protected void onSuccessCall(String responseString) {
                if (isCache) {
                    setRequestCache(context, cacheKey, responseString, cacheTime);
                }
            }
        });
    }
}
