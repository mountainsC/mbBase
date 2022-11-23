package com.cloud.core.okrx.requests;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.enums.RequestContentType;
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
public class OkRxPostRequest extends BaseRequest {

    private String responseString = "";
    private Action2<String, HashMap<String, String>> headersAction = null;

    public OkRxPostRequest(RequestContentType requestContentType) {
        super.setRequestContentType(requestContentType);
    }

    @Override
    public void call(final Context context, String url, HashMap<String, String> headers, HashMap<String, Object> params, final boolean isCache, final String cacheKey, final long cacheTime, Action3<String, String, HashMap<String, ReqQueueItem>> successAction, Action1<RequestState> completeAction, Action2<String, String> printLogAction, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiUnique, Action2<String, HashMap<String, String>> headersAction) {
        this.headersAction = headersAction;
        if (context == null || TextUtils.isEmpty(url) || !ValidUtils.valid(RuleParams.Url.getValue(), url)) {
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
        setRequestType(RequestType.POST);
        Request.Builder builder = getBuilder(url, headers, params);
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
