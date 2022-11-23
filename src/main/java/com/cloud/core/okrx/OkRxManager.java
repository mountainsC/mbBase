package com.cloud.core.okrx;

import android.content.Context;
import android.graphics.Bitmap;

import com.cloud.core.enums.RequestContentType;
import com.cloud.core.enums.RequestState;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.properties.ByteRequestItem;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.okrx.requests.OkRxBitmapRequest;
import com.cloud.core.okrx.requests.OkRxDeleteRequest;
import com.cloud.core.okrx.requests.OkRxDownloadFileRequest;
import com.cloud.core.okrx.requests.OkRxGetRequest;
import com.cloud.core.okrx.requests.OkRxHeadRequest;
import com.cloud.core.okrx.requests.OkRxOptionsRequest;
import com.cloud.core.okrx.requests.OkRxPatchRequest;
import com.cloud.core.okrx.requests.OkRxPostRequest;
import com.cloud.core.okrx.requests.OkRxPutRequest;
import com.cloud.core.okrx.requests.OkRxTraceRequest;
import com.cloud.core.okrx.requests.OkRxUploadByteRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:网络请求类
 * Modifier:
 * ModifyContent:
 */
public class OkRxManager {

    private static OkRxManager okRxManager = null;

    public static OkRxManager getInstance() {
        return okRxManager == null ? okRxManager = new OkRxManager() : okRxManager;
    }

    public void get(Context context,
                    String url,
                    HashMap<String, String> headers,
                    HashMap<String, Object> params,
                    boolean isCache,
                    String cacheKey,
                    long cacheTime,
                    Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action1<RequestState> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxGetRequest request = new OkRxGetRequest();
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void post(Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     boolean isCache,
                     String cacheKey,
                     long cacheTime,
                     RequestContentType requestContentType,
                     Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPostRequest request = new OkRxPostRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void delete(Context context,
                       String url,
                       HashMap<String, String> headers,
                       HashMap<String, Object> params,
                       boolean isCache,
                       String cacheKey,
                       long cacheTime,
                       RequestContentType requestContentType,
                       Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                       String apiUnique,
                       Action2<String, HashMap<String, String>> headersAction,
                       Action1<RequestState> completeAction,
                       Action2<String, String> printLogAction,
                       String apiRequestKey,
                       HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDeleteRequest request = new OkRxDeleteRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void put(Context context,
                    String url,
                    HashMap<String, String> headers,
                    HashMap<String, Object> params,
                    boolean isCache,
                    String cacheKey,
                    long cacheTime,
                    RequestContentType requestContentType,
                    Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                    String apiUnique,
                    Action2<String, HashMap<String, String>> headersAction,
                    Action1<RequestState> completeAction,
                    Action2<String, String> printLogAction,
                    String apiRequestKey,
                    HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPutRequest request = new OkRxPutRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void patch(Context context,
                      String url,
                      HashMap<String, String> headers,
                      HashMap<String, Object> params,
                      boolean isCache,
                      String cacheKey,
                      long cacheTime,
                      RequestContentType requestContentType,
                      Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action1<RequestState> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPatchRequest request = new OkRxPatchRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void head(Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     boolean isCache,
                     String cacheKey,
                     long cacheTime,
                     Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxHeadRequest request = new OkRxHeadRequest();
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void options(Context context,
                        String url,
                        HashMap<String, String> headers,
                        HashMap<String, Object> params,
                        boolean isCache,
                        String cacheKey,
                        long cacheTime,
                        RequestContentType requestContentType,
                        Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                        String apiUnique,
                        Action2<String, HashMap<String, String>> headersAction,
                        Action1<RequestState> completeAction,
                        Action2<String, String> printLogAction,
                        String apiRequestKey,
                        HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxOptionsRequest request = new OkRxOptionsRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void trace(Context context,
                      String url,
                      HashMap<String, String> headers,
                      HashMap<String, Object> params,
                      boolean isCache,
                      String cacheKey,
                      long cacheTime,
                      RequestContentType requestContentType,
                      Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                      String apiUnique,
                      Action2<String, HashMap<String, String>> headersAction,
                      Action1<RequestState> completeAction,
                      Action2<String, String> printLogAction,
                      String apiRequestKey,
                      final HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxTraceRequest request = new OkRxTraceRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, apiUnique, headersAction);
    }

    public void download(Context context,
                         String url,
                         HashMap<String, String> headers,
                         HashMap<String, Object> params,
                         File downFile,
                         Action1<Float> progressAction,
                         Action1<File> successAction,
                         Action1<RequestState> completeAction,
                         String apiRequestKey,
                         HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxDownloadFileRequest request = new OkRxDownloadFileRequest();
        request.call(context, url, headers, params, downFile, progressAction, successAction, completeAction, apiRequestKey, reqQueueItemHashMap);
    }

    public void getBitmap(Context context,
                          String url,
                          HashMap<String, String> headers,
                          HashMap<String, Object> params,
                          Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction,
                          Action1<RequestState> completeAction,
                          String apiRequestKey,
                          HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxBitmapRequest request = new OkRxBitmapRequest();
        request.call(context, url, headers, params, successAction, completeAction, apiRequestKey, reqQueueItemHashMap, "", null);
    }

    public void uploadFile(Context context,
                           String url,
                           HashMap<String, String> headers,
                           HashMap<String, Object> params,
                           boolean isCache,
                           String cacheKey,
                           long cacheTime,
                           RequestContentType requestContentType,
                           Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                           Action1<RequestState> completeAction,
                           Action2<String, String> printLogAction,
                           String apiRequestKey,
                           HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxPostRequest request = new OkRxPostRequest(requestContentType);
        request.call(context, url, headers, params, isCache, cacheKey, cacheTime, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap, "", null);
    }

    public void uploadBytes(Context context,
                            String url,
                            HashMap<String, String> httpHeaders,
                            HashMap<String, Object> httpParams,
                            List<ByteRequestItem> byteRequestItems,
                            Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                            Action1<RequestState> completeAction,
                            Action2<String, String> printLogAction,
                            String apiRequestKey,
                            HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        OkRxUploadByteRequest request = new OkRxUploadByteRequest();
        request.call(context, url, httpHeaders, httpParams, byteRequestItems, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap);
    }

    public void uploadByte(Context context,
                           String url,
                           HashMap<String, String> httpHeaders,
                           HashMap<String, Object> httpParams,
                           ByteRequestItem byteRequestItem,
                           Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                           Action1<RequestState> completeAction,
                           Action2<String, String> printLogAction,
                           String apiRequestKey,
                           HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        List<ByteRequestItem> byteRequestItems = new ArrayList<ByteRequestItem>();
        byteRequestItems.add(byteRequestItem);
        uploadBytes(context, url, httpHeaders, httpParams, byteRequestItems, successAction, completeAction, printLogAction, apiRequestKey, reqQueueItemHashMap);
    }
}
