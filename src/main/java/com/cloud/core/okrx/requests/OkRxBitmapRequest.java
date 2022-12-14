package com.cloud.core.okrx.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RequestType;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.OkRx;
import com.cloud.core.okrx.callback.BitmapCallback;
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
public class OkRxBitmapRequest extends BaseRequest {

    @Override
    public void call(Context context, String url, HashMap<String, String> headers, HashMap<String, Object> params, Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction, Action1<RequestState> completeAction, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap, String apiUnique, Action2<String, HashMap<String, String>> headersAction) {
        if (context == null || TextUtils.isEmpty(url) || !ValidUtils.valid(RuleParams.Url.getValue(), url)) {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            return;
        }
        setRequestType(RequestType.GET);
        Request.Builder builder = getBuilder(url, headers, params).get();
        Request request = builder.build();
        OkHttpClient client = OkRx.getInstance().getOkHttpClient(context);
        client.newCall(request).enqueue(new BitmapCallback(context, successAction, completeAction, reqQueueItemHashMap, apiRequestKey, apiUnique, headersAction));
    }
}
