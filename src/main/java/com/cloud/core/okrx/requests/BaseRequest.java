package com.cloud.core.okrx.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.cache.RxCache;
import com.cloud.core.enums.RequestContentType;
import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RequestType;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.OkRxKeys;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseRequest {

    private RequestType requestType = null;
    private RequestContentType requestContentType = null;

    protected void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void call(Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     boolean isCache,
                     String cacheKey,
                     long cacheTime,
                     Action3<String, String, HashMap<String, ReqQueueItem>> successAction,
                     Action1<RequestState> completeAction,
                     Action2<String, String> printLogAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        //子类重载方法
    }

    public void call(Context context,
                     String url,
                     HashMap<String, String> headers,
                     HashMap<String, Object> params,
                     Action3<Bitmap, String, HashMap<String, ReqQueueItem>> successAction,
                     Action1<RequestState> completeAction,
                     String apiRequestKey,
                     HashMap<String, ReqQueueItem> reqQueueItemHashMap,
                     String apiUnique,
                     Action2<String, HashMap<String, String>> headersAction) {
        //子类重载方法
    }

    public void setRequestCache(Context context, String cacheKey, String data, long cacheTime) {
        RxCache.setCacheData(context, cacheKey, data, cacheTime, TimeUnit.MILLISECONDS);
    }

    public String getRequestCache(Context context, String cacheKey) {
        return RxCache.getCacheData(context, cacheKey, true);
    }

    public void setRequestContentType(RequestContentType requestContentType) {
        this.requestContentType = requestContentType;
    }

    protected Request.Builder getBuilder(String url,
                                         HashMap<String, String> headers,
                                         HashMap<String, Object> params) {
        Request.Builder builder = new Request.Builder();
        if (requestType == RequestType.GET) {
            url = addGetRequestParams(url, params);
        }
        builder.url(url);
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.removeHeader(entry.getKey());
                String value = entry.getValue() + "";
                builder.header(entry.getKey(), value);
            }
        }
        if (requestType != RequestType.GET) {
            if (requestType == RequestType.HEAD) {
                builder.head();
            } else {
                addRequestParams(builder, params);
            }
        }
        return builder;
    }

    private void submitMultipartParams(Request.Builder builder, MultipartBody requestBody) {
        if (requestType == RequestType.POST) {
            builder.post(requestBody);
        } else if (requestType == RequestType.PUT) {
            builder.put(requestBody);
        } else if (requestType == RequestType.DELETE) {
            builder.delete(requestBody);
        } else if (requestType == RequestType.PATCH) {
            builder.patch(requestBody);
        } else if (requestType == RequestType.OPTIONS) {
            builder.method("OPTIONS", requestBody);
        } else if (requestType == RequestType.TRACE) {
            builder.method("TRACE", requestBody);
        }
    }

    private void submitRequestParams(Request.Builder builder, RequestBody requestBody) {
        if (requestType == RequestType.POST) {
            builder.post(requestBody);
        } else if (requestType == RequestType.PUT) {
            builder.put(requestBody);
        } else if (requestType == RequestType.DELETE) {
            builder.delete(requestBody);
        } else if (requestType == RequestType.PATCH) {
            builder.patch(requestBody);
        } else if (requestType == RequestType.OPTIONS) {
            builder.method("OPTIONS", requestBody);
        } else if (requestType == RequestType.TRACE) {
            builder.method("TRACE", requestBody);
        }
    }

    //如果参数集合包含file或byte[]则无论requestContentType是否为json均以Form方式提交
    private void addRequestParams(Request.Builder builder, HashMap<String, Object> params) {
        ValidResult validResult = validParams(params);
        if (!ObjectJudge.isNullOrEmpty(validResult.streamParamKeys) ||
                !ObjectJudge.isNullOrEmpty(validResult.fileParamKeys)) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (validResult.streamParamKeys.contains(entry.getKey())) {
                    //以字节流的形式上传文件
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    RequestBody body = RequestBody.create(mediaType, (byte[]) entry.getValue());
                    String filename = String.format("%s.rxtiny", GlobalUtils.getGuidNoConnect());
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                } else if (validResult.fileParamKeys.contains(entry.getKey())) {
                    //以文件的形式上传文件
                    MediaType mediaType = MediaType.parse("multipart/form-data");
                    RequestBody body = RequestBody.create(mediaType, (File) entry.getValue());
                    String filename="";
                    if (((File) entry.getValue()).getAbsolutePath().endsWith(".mp4")) {
                         filename = String.format("%s.mp4", GlobalUtils.getGuidNoConnect());
                    }else {
                        filename = String.format("%s.rxtiny", GlobalUtils.getGuidNoConnect());
                    }
                    requestBody.addFormDataPart(entry.getKey(), filename, body);
                } else if ((entry.getValue() instanceof List) || (entry.getValue() instanceof Map)) {
                    requestBody.addFormDataPart(entry.getKey(), JsonUtils.toStr(entry.getValue()));
                } else {
                    requestBody.addFormDataPart(entry.getKey(), entry.getValue() + "");
                }
            }
            MultipartBody body = requestBody.build();
            submitMultipartParams(builder, body);
        } else {
            RequestBody requestBody = addJsonRequestParams(validResult.ignoreParamContainsKeys, params);
            submitRequestParams(builder, requestBody);
        }
    }

    private RequestBody addJsonRequestParams(List<String> ignoreParamKeys, HashMap<String, Object> params) {
        if (requestContentType == RequestContentType.Form) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (!ObjectJudge.isNullOrEmpty(params)) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof List) {
                        bodyBuilder.add(entry.getKey(), JsonUtils.toStr(entry.getValue()));
                    } else {
                        bodyBuilder.add(entry.getKey(), entry.getValue() + "");
                    }
                }
            }
            RequestBody requestBody = bodyBuilder.build();
            return requestBody;
        } else {
            if (ObjectJudge.isNullOrEmpty(params)) {
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
            }
            if (ObjectJudge.isNullOrEmpty(ignoreParamKeys)) {
                String body = JsonUtils.toStr(params);
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
            } else {
                //如果包含有忽略参数将忽略其它参数提交
                if (ignoreParamKeys.size() == 1) {
                    String key = ignoreParamKeys.get(0);
                    Object value = params.get(key);
                    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value + "");
                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (String paramKey : ignoreParamKeys) {
                        map.put(paramKey, params.get(paramKey));
                    }
                    String value = JsonUtils.toStr(map);
                    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
                }
            }
        }
    }

    private class ValidResult {
        //被忽略参数key
        public List<String> ignoreParamContainsKeys = new ArrayList<String>();
        //流参数key
        public List<String> streamParamKeys = new ArrayList<String>();
        //文件参数key
        public List<String> fileParamKeys = new ArrayList<String>();
    }

    private ValidResult validParams(HashMap<String, Object> params) {
        ValidResult result = new ValidResult();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().startsWith(OkRxKeys.ignoreParamContainsKey) && entry.getValue() instanceof String) {
                if (!result.ignoreParamContainsKeys.contains(entry.getKey())) {
                    result.ignoreParamContainsKeys.add(entry.getKey());
                }
            } else if (entry.getValue() instanceof byte[]) {
                if (!result.streamParamKeys.contains(entry.getKey())) {
                    result.streamParamKeys.add(entry.getKey());
                }
            } else if (entry.getValue() instanceof File) {
                if (!result.fileParamKeys.contains(entry.getKey())) {
                    result.fileParamKeys.add(entry.getKey());
                }
            }
        }
        return result;
    }

    private String addGetRequestParams(String url, HashMap<String, Object> params) {
        if (ObjectJudge.isNullOrEmpty(params)) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int count = params.size();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue() + "";
            builder.append(entry.getKey() + "=" + value.trim() + ((index + 1) < count ? "&" : ""));
            index++;
        }
        //判断原url中是否包含?
        if (StringUtils.isContains(url, "?")) {
            return url + "&" + builder.toString();
        } else {
            return url + "?" + builder.toString();
        }
    }
}
