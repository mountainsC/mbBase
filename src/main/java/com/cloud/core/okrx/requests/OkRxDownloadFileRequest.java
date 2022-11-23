package com.cloud.core.okrx.requests;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RequestType;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.events.Action1;
import com.cloud.core.okrx.OkRx;
import com.cloud.core.okrx.callback.FileCallback;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.ValidUtils;

import java.io.File;
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
public class OkRxDownloadFileRequest extends BaseRequest {

    public void call(Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     File downFile,
                     Action1<Float> progressAction,
                     Action1<File> successAction,
                     Action1<RequestState> completeAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        if (context == null || TextUtils.isEmpty(url) || !ValidUtils.valid(RuleParams.Url.getValue(), url) || downFile == null || !downFile.exists()) {
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
        client.newCall(request).enqueue(new FileCallback(downFile, progressAction, successAction, completeAction, reqQueueItemHashMap, apiRequestKey));
    }
}
