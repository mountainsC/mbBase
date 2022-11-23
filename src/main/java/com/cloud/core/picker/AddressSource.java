package com.cloud.core.picker;

import android.content.Context;

import com.cloud.core.enums.RequestState;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.OkRxManager;
import com.cloud.core.okrx.properties.ReqQueueItem;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/27
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class AddressSource {

    protected void onStarted() {

    }

    protected void onSuccessful(String response) {

    }

    protected void onCompleted() {

    }

    public void requestAddress(Context context, String url) {
        onStarted();
        OkRxManager.getInstance().get(context,
                url,
                null,
                null,
                false,
                "",
                0,
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        onSuccessful(response);
                    }
                },
                "",
                null, new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Completed) {
                            onCompleted();
                        }
                    }
                }, null, "", null);
    }
}
