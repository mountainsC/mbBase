package com.cloud.core.okrx.callback;

import com.cloud.core.enums.RequestState;
import com.cloud.core.events.Action1;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.properties.ReqQueueItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
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
public class FileCallback implements Callback {

    //下载文件
    private File downFile = null;
    //下载进度
    private Action1<Float> progressAction = null;
    //处理成功回调
    private Action1<File> successAction = null;
    //请求完成时回调(成功或失败)
    private Action1<RequestState> completeAction = null;
    //请求标识队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = null;
    //请求标识
    private String apiRequestKey = "";

    public FileCallback(File downFile,
                        Action1<Float> progressAction,
                        Action1<File> successAction,
                        Action1<RequestState> completeAction,
                        HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                        String apiRequestKey) {
        this.downFile = downFile;
        this.progressAction = progressAction;
        this.successAction = successAction;
        this.completeAction = completeAction;
        this.reqQueueItemHashMap = reqQueueItemHashMap;
        this.apiRequestKey = apiRequestKey;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            reqQueueItemHashMap.remove(apiRequestKey);
        }
        if (completeAction != null) {
            completeAction.call(RequestState.Error);
        }
        if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
            queueItem.setReqNetCompleted(true);
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if (response == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
            }
            ResponseBody body = response.body();
            if (body == null) {
                if (completeAction != null) {
                    completeAction.call(RequestState.Error);
                }
                return;
            }
            if (successAction != null) {
                InputStream stream = body.byteStream();
                //获取字节流总长度
                long total = body.contentLength();
                FileOutputStream fos = new FileOutputStream(downFile);
                long sum = 0;
                int len = 0;
                byte[] buf = new byte[2048];
                DecimalFormat df = new DecimalFormat("0.00");
                while ((len = stream.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    float progress = sum * 1.0f / total;
                    if (progressAction != null) {
                        Number number = NumberFormat.getNumberInstance().parse(df.format(progress));
                        progressAction.call(number.floatValue());
                    }
                }
                fos.flush();
                successAction.call(downFile);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        } finally {
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                reqQueueItemHashMap.remove(apiRequestKey);
            }
            if (completeAction != null) {
                completeAction.call(RequestState.Completed);
            }
            if (reqQueueItemHashMap != null && reqQueueItemHashMap.containsKey(apiRequestKey)) {
                ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                queueItem.setReqNetCompleted(true);
            }
        }
    }
}
