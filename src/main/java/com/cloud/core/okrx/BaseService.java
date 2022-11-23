package com.cloud.core.okrx;

import android.content.Context;
import android.database.Observable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.annotations.ApiHeadersCall;
import com.cloud.core.annotations.ReturnCodeFilter;
import com.cloud.core.beans.BaseBean;
import com.cloud.core.beans.RetrofitParams;
import com.cloud.core.beans.UnLoginCallInfo;
import com.cloud.core.cache.RxCache;
import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RequestType;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.events.Func1;
import com.cloud.core.events.Func2;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.events.OnApiRetCodesFilterListener;
import com.cloud.core.okrx.events.OnHttpRequestHeadersListener;
import com.cloud.core.okrx.events.OnResponseErrorListener;
import com.cloud.core.okrx.events.OnUnLoginCallInfoListener;
import com.cloud.core.okrx.properties.ByteRequestItem;
import com.cloud.core.okrx.properties.OkRxConfigParams;
import com.cloud.core.okrx.properties.OkRxValidParam;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.PathsUtils;
import com.cloud.core.utils.ThreadPoolUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/6/14
 * Description:
 */
public class BaseService {

    /**
     * token值
     */
    private String token = "";
    /**
     * 接口名
     */
    private String apiName = "";

    private BaseSubscriber baseSubscriber = null;
    private HashMap<String, StringBuffer> logmaps = new HashMap<String, StringBuffer>();
    //请求队列
    private HashMap<String, ReqQueueItem> reqQueueItemHashMap = new HashMap<String, ReqQueueItem>();
    private Handler mhandler = new Handler(Looper.getMainLooper());
    private ReturnCodeFilter returnCodeFilter = null;

    /**
     * 获取token值
     */

    public String getToken() {
        if (token == null) {
            token = "";
        }
        return token;
    }

    /**
     * 设置token值
     * <p>
     * param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取接口名
     */
    public String getApiName() {
        if (apiName == null) {
            apiName = "";
        }
        return apiName;
    }

    /**
     * 设置接口名
     * <p>
     * param apiName
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public BaseSubscriber getBaseSubscriber() {
        return this.baseSubscriber;
    }

    public void setBaseSubscriber(BaseSubscriber baseSubscriber) {
        this.baseSubscriber = baseSubscriber;
    }

    /**
     * API请求完成(结束)
     */
    protected void onRequestCompleted() {

    }

    protected void onRequestError() {

    }

    protected <T extends BaseBean> void baseConfig(Context context,
                                                   final BaseService baseService,
                                                   final RetrofitParams retrofitParams,
                                                   OkRxValidParam validParam,
                                                   final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                                   String apiRequestKey) {
        Observable<T> observable = null;
        final Class<T> dataClass = retrofitParams.getDataClass();
        try {
            if (!TextUtils.isEmpty(retrofitParams.getRequestUrl())) {
                String requestUrl = retrofitParams.getRequestUrl();
                //头信息
                HashMap<String, String> headParams = retrofitParams.getHeadParams();
                //检查头部是否已添加token，没有则添加
                if (!TextUtils.isEmpty(token)) {
                    if (validParam.getApiCheckAnnotation().IsTokenValid()) {
                        String tokenName = retrofitParams.getTokenName();
                        if (!TextUtils.isEmpty(tokenName)) {
                            headParams.put(tokenName, token);
                        }
                    }
                }
                //设置返回码监听
                if (returnCodeFilter == null) {
                    returnCodeFilter = validParam.getReturnCodeFilter();
                }
                //请求api
                reqQueueItemHashMap.put(apiRequestKey, new ReqQueueItem());
                if (retrofitParams.getRequestType() == RequestType.BYTES) {
                    HashMap<String, Object> updateByteParams = getUploadByteParams(retrofitParams);
                    List<ByteRequestItem> uploadByteItems = getUploadByteItems(retrofitParams);
                    subBytes(context, requestUrl, headParams, updateByteParams, uploadByteItems, baseService, dataClass, successAction, apiRequestKey);
                } else {
                    //请求参数
                    if (retrofitParams.getRequestType() == RequestType.POST) {
                        post(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.DELETE) {
                        delete(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.PUT) {
                        put(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.PATCH) {
                        patch(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.HEAD) {
                        head(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.POST) {
                        options(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else if (retrofitParams.getRequestType() == RequestType.TRACE) {
                        trace(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    } else {
                        get(context, requestUrl, headParams, retrofitParams, baseService, dataClass, successAction, apiRequestKey);
                    }
                }
            } else {
                finishedRequest(baseService);
            }
        } catch (Exception e) {
            finishedRequest(baseService);
            Logger.L.error(e);
        }
    }

    private void finishedRequest(final BaseService baseService) {
        if (ObjectJudge.isMainThread()) {
            baseService.onRequestCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    baseService.onRequestCompleted();
                }
            });
        }
    }

    private <T extends BaseBean> void successDealWith(Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                                      Class<T> dataClass,
                                                      BaseService baseService,
                                                      String response,
                                                      String apiRequestKey,
                                                      HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
        if (successAction == null) {
            finishedRequest(baseService);
        } else {
            T data = JsonUtils.parseT(response, dataClass);
            if (data != null && TextUtils.isEmpty(data.getCode())) {
                BaseBean baseBean = (BaseBean) JsonUtils.parseT(response, dataClass.getSuperclass());
                data.setCode(baseBean.getCode());
                data.setMessage(baseBean.getMessage());
                data.setHasNextPage(baseBean.isHasNextPage());
                data.setHasPreviousPage(baseBean.isHasPreviousPage());
                data.setFirstPage(baseBean.isFirstPage());
                data.setLastPage(baseBean.isLastPage());
                data.setFirstPage(baseBean.getFirstPage());
                data.setLastPage(baseBean.getLastPage());
                data.setPageNum(baseBean.getPageNum());
                data.setPageSize(baseBean.getPageSize());
                data.setPages(baseBean.getPages());
                data.setTotal(baseBean.getTotal());
            }
            //拦截符合的返回码
            filterMatchRetCodes(data);
            successAction.call(data, apiRequestKey, reqQueueItemHashMap);
        }
    }

    private <T extends BaseBean> void filterMatchRetCodes(T data) {
        if (returnCodeFilter == null || ObjectJudge.isNullOrEmpty(returnCodeFilter.retCodes()) || returnCodeFilter.retCodesListeningClass() == null) {
            return;
        }
        List<String> codes = Arrays.asList(returnCodeFilter.retCodes());
        if (!codes.contains(data.getCode())) {
            return;
        }
        Object obj = JsonUtils.newNull(returnCodeFilter.retCodesListeningClass());
        if (obj == null || !(obj instanceof OnApiRetCodesFilterListener)) {
            return;
        }
        OnApiRetCodesFilterListener filterListener = (OnApiRetCodesFilterListener) obj;
        filterListener.onApiRetCodesFilter(data.getCode(), data);
    }

    private OnHttpRequestHeadersListener getHeaderCall(ApiHeadersCall apiHeadersCall) {
        if (apiHeadersCall == null) {
            return null;
        }
        Class<?> headersCallClass = apiHeadersCall.requestHeadersCallClass();
        Object callObject = JsonUtils.newNull(headersCallClass);
        if (callObject == null || !(callObject instanceof OnHttpRequestHeadersListener)) {
            return null;
        }
        return (OnHttpRequestHeadersListener) callObject;
    }

    private <T extends BaseBean> void subBytes(Context context,
                                               String requestUrl,
                                               HashMap<String, String> httpHeaders,
                                               HashMap<String, Object> httpParams,
                                               List<ByteRequestItem> byteRequestItems,
                                               final BaseService baseService,
                                               final Class<T> dataClass,
                                               final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                               final String apiRequestKey) {
        OkRxManager.getInstance().uploadBytes(
                context,
                requestUrl,
                httpHeaders,
                httpParams,
                byteRequestItems,
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        finallPrintLog(apiRequestKey, responseString);
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void trace(Context context,
                                            String requestUrl,
                                            HashMap<String, String> headers,
                                            RetrofitParams retrofitParams,
                                            final BaseService baseService,
                                            final Class<T> dataClass,
                                            final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                            final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().trace(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void options(Context context,
                                              String requestUrl,
                                              HashMap<String, String> headers,
                                              RetrofitParams retrofitParams,
                                              final BaseService baseService,
                                              final Class<T> dataClass,
                                              final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                              final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().options(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void head(Context context,
                                           String requestUrl,
                                           HashMap<String, String> headers,
                                           RetrofitParams retrofitParams,
                                           final BaseService baseService,
                                           final Class<T> dataClass,
                                           final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                           final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().head(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void patch(Context context,
                                            String requestUrl,
                                            HashMap<String, String> headers,
                                            RetrofitParams retrofitParams,
                                            final BaseService baseService,
                                            final Class<T> dataClass,
                                            final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                            final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().patch(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void put(Context context,
                                          String requestUrl,
                                          HashMap<String, String> headers,
                                          RetrofitParams retrofitParams,
                                          final BaseService baseService,
                                          final Class<T> dataClass,
                                          final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                          final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().put(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void delete(Context context,
                                             String requestUrl,
                                             HashMap<String, String> headers,
                                             RetrofitParams retrofitParams,
                                             final BaseService baseService,
                                             final Class<T> dataClass,
                                             final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                             final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().delete(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void post(Context context,
                                           String requestUrl,
                                           HashMap<String, String> headers,
                                           RetrofitParams retrofitParams,
                                           final BaseService baseService,
                                           final Class<T> dataClass,
                                           final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                           final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().post(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                retrofitParams.getRequestContentType(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private <T extends BaseBean> void get(Context context,
                                          String requestUrl,
                                          HashMap<String, String> headers,
                                          RetrofitParams retrofitParams,
                                          final BaseService baseService,
                                          final Class<T> dataClass,
                                          final Action3<T, String, HashMap<String, ReqQueueItem>> successAction,
                                          final String apiRequestKey) {
        final ApiHeadersCall apiHeadersCall = retrofitParams.getApiHeadersCall();
        OkRxManager.getInstance().get(
                context,
                requestUrl,
                headers,
                retrofitParams.getParams(),
                retrofitParams.isCache(),
                retrofitParams.getCacheKey(),
                retrofitParams.getCacheTime(),
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        successDealWith(successAction, dataClass, baseService, response, apiRequestKey, reqQueueItemHashMap);
                    }
                },
                apiHeadersCall == null ? "" : apiHeadersCall.unique(),
                new Action2<String, HashMap<String, String>>() {
                    @Override
                    public void call(String apiUnique, HashMap<String, String> headers) {
                        OnHttpRequestHeadersListener call = getHeaderCall(apiHeadersCall);
                        if (call == null) {
                            return;
                        }
                        call.onRequestHeaders(apiUnique, headers);
                    }
                },
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Error) {
                            baseService.onRequestError();
                        }
                        if (reqQueueItemHashMap.containsKey(apiRequestKey)) {
                            ReqQueueItem queueItem = reqQueueItemHashMap.get(apiRequestKey);
                            if (queueItem.isSuccess() && queueItem.isReqNetCompleted()) {
                                reqQueueItemHashMap.remove(apiRequestKey);
                                finishedRequest(baseService);
                            }
                        }
                    }
                },
                new Action2<String, String>() {
                    @Override
                    public void call(String apiRequestKey, String responseString) {
                        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
                        if (globalHeaders.isDebug()) {
                            finallPrintLog(apiRequestKey, responseString);
                        }
                    }
                }, apiRequestKey, reqQueueItemHashMap);
    }

    private void finallPrintLog(String apiRequestKey, String responseString) {
        try {
            if (!logmaps.containsKey(apiRequestKey)) {
                return;
            }
            StringBuffer buffer = logmaps.get(apiRequestKey);
            if (buffer == null) {
                return;
            }
            if (!TextUtils.isEmpty(responseString)) {
                int length = responseString.length();
                for (int i = 0; i < length; i += 90) {
                    int endIndex = i + 90;
                    if (endIndex >= length) {
                        buffer.append(String.format("%s\n", responseString.substring(i)));
                    } else {
                        buffer.append(String.format("%s\n", responseString.substring(i, endIndex)));
                    }
                }
            }
            buffer.append(String.format("%s\n", responseString));
            buffer.append("===============================================================\n");
            Logger.L.info(buffer.toString());
        } catch (Exception e) {
            Logger.L.error(e);
        } finally {
            if (logmaps.containsKey(apiRequestKey)) {
                logmaps.remove(apiRequestKey);
            }
        }
    }

    private List<ByteRequestItem> getUploadByteItems(RetrofitParams retrofitParams) {
        List<ByteRequestItem> lst = new ArrayList<ByteRequestItem>();
        HashMap<String, Object> params = retrofitParams.getParams();
        if (ObjectJudge.isNullOrEmpty(params)) {
            return lst;
        }
        for (HashMap.Entry<String, Object> entry : params.entrySet()) {
            //参数名
            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            //参数值
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if ((value instanceof byte[]) || (value instanceof Byte[])) {
                ByteRequestItem requestItem = new ByteRequestItem();
                requestItem.setFieldName(key);
                requestItem.setBs((byte[]) value);
                lst.add(requestItem);
            }
        }
        return lst;
    }

    private HashMap<String, Object> getUploadByteParams(RetrofitParams retrofitParams) {
        HashMap<String, Object> params2 = new HashMap<String, Object>();
        if (ObjectJudge.isNullOrEmpty(retrofitParams.getParams())) {
            return params2;
        }
        HashMap<String, Object> params = retrofitParams.getParams();
        for (HashMap.Entry<String, Object> entry : params.entrySet()) {
            //参数名
            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            //参数值
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof Integer) {
                params2.put(key, value);
            } else if (value instanceof Long) {
                params2.put(key, value);
            } else if (value instanceof String) {
                params2.put(key, value);
            } else if (value instanceof Double) {
                params2.put(key, value);
            } else if (value instanceof Float) {
                params2.put(key, value);
            } else if (value instanceof Boolean) {
                params2.put(key, value);
            } else if (value instanceof List) {
                params2.put(key, JsonUtils.toStr(value));
            }
        }
        return params2;
    }

    private <S extends BaseService> void openLogin(Context context, S s, OnUnLoginCallInfoListener listener) {
        //请求token api请求中的token清空
        s.setToken("");
        if (listener != null) {
            UnLoginCallInfo callInfo = new UnLoginCallInfo();
            callInfo.setApiName(s.getApiName());
            listener.onCallInfo(callInfo);
        }
    }

    private <T extends BaseBean, S extends BaseService> void finishedRequest(final BaseSubscriber<T, S> baseSubscriber) {
        if (ObjectJudge.isMainThread()) {
            baseSubscriber.onCompleted();
        } else {
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    baseSubscriber.onCompleted();
                }
            });
        }
    }

    protected <T extends BaseBean, I, S extends BaseService> void requestObject(Context context,
                                                                                Class<I> apiClass,
                                                                                S server,
                                                                                final BaseSubscriber<T, S> baseSubscriber,
                                                                                OkRxValidParam validParam,
                                                                                Func2<String, S, String> urlAction,
                                                                                Func1<I, RetrofitParams> decApiAction,
                                                                                OnUnLoginCallInfoListener listener,
                                                                                OnResponseErrorListener errorListener) {
        try {
            //若需要登录验证则打开登录页面
            if (validParam.isNeedLogin()) {
                openLogin(context, server, listener);
                return;
            }
            //验证失败结束请求(需要判断当前请求的接口是否在线程中请求)
            if (!validParam.isFlag()) {
                finishedRequest(baseSubscriber);
                return;
            }
            if (urlAction == null || server == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            OkRxParsing parsing = new OkRxParsing();
            I decApi = parsing.createAPI(apiClass);
            if (decApiAction == null || decApi == null || validParam.getApiCheckAnnotation() == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            RetrofitParams retrofitParams = decApiAction.call(decApi);
            if (retrofitParams == null || !retrofitParams.getFlag()) {
                finishedRequest(baseSubscriber);
                return;
            }
            //若api类未指定base url类型名称则不作请求处理
            if (retrofitParams.getIsJoinUrl() && retrofitParams.getUrlTypeName() == null) {
                finishedRequest(baseSubscriber);
                return;
            }
            if (retrofitParams.getIsJoinUrl() && TextUtils.isEmpty(retrofitParams.getUrlTypeName().value())) {
                finishedRequest(baseSubscriber);
                return;
            }
            ExecutorService fixThread = ThreadPoolUtils.fixThread();
            fixThread.execute(new ApiRequestRunnable<T, I, S>(context, apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction, listener, errorListener));
        } catch (Exception e) {
            finishedRequest(baseSubscriber);
        }
    }

    private class ApiRequestRunnable<T extends BaseBean, I, S extends BaseService> implements Runnable {

        private Context context;
        private Class<I> apiClass;
        private S server;
        private BaseSubscriber<T, S> baseSubscriber;
        private OkRxValidParam validParam;
        private RetrofitParams retrofitParams;
        private Func2<String, S, String> urlAction;
        private OnUnLoginCallInfoListener listener;
        private OnResponseErrorListener errorListener;

        public ApiRequestRunnable(Context context,
                                  Class<I> apiClass,
                                  S server,
                                  final BaseSubscriber<T, S> baseSubscriber,
                                  OkRxValidParam validParam,
                                  RetrofitParams retrofitParams,
                                  Func2<String, S, String> urlAction,
                                  OnUnLoginCallInfoListener listener,
                                  OnResponseErrorListener errorListener) {
            this.context = context;
            this.apiClass = apiClass;
            this.server = server;
            this.baseSubscriber = baseSubscriber;
            this.validParam = validParam;
            this.retrofitParams = retrofitParams;
            this.urlAction = urlAction;
            this.listener = listener;
            this.errorListener = errorListener;
        }

        @Override
        public void run() {
            apiRequest(context, apiClass, server, baseSubscriber, validParam, retrofitParams, urlAction, listener, errorListener);
        }
    }

    private <T extends BaseBean, I, S extends BaseService> void apiRequest(Context context,
                                                                           Class<I> apiClass,
                                                                           S server,
                                                                           final BaseSubscriber<T, S> baseSubscriber,
                                                                           OkRxValidParam validParam,
                                                                           RetrofitParams retrofitParams,
                                                                           Func2<String, S, String> urlAction,
                                                                           OnUnLoginCallInfoListener listener,
                                                                           OnResponseErrorListener errorListener) {
        //设置回调是否作验证
        baseSubscriber.setValidCallResult(retrofitParams.isValidCallResult());
        //设置此接口允许返回码
        if (!ObjectJudge.isNullOrEmpty(retrofitParams.getAllowRetCodes())) {
            List<String> allowRetCodes = baseSubscriber.getAllowRetCodes();
            allowRetCodes.addAll(retrofitParams.getAllowRetCodes());
        }
        //设置请求地址
        if (retrofitParams.getUrlTypeName() != null) {
            if (retrofitParams.getIsJoinUrl()) {
                String baseUrl = urlAction.call(server, retrofitParams.getUrlTypeName().value());
                retrofitParams.setRequestUrl(PathsUtils.combine(baseUrl, retrofitParams.getRequestUrl()));
                if (retrofitParams.isLastContainsPath() && !retrofitParams.getRequestUrl().endsWith("/")) {
                    retrofitParams.setRequestUrl(retrofitParams.getRequestUrl() + "/");
                }
            }
            //设置token名字
            retrofitParams.setTokenName(retrofitParams.getUrlTypeName().tokenName());
        }
        //NO_CACHE: 不使用缓存,该模式下,cacheKey,cacheTime 参数均无效
        //DEFAULT: 按照HTTP协议的默认缓存规则，例如有304响应头时缓存。
        //REQUEST_FAILED_READ_CACHE：先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。
        //IF_NONE_CACHE_REQUEST：如果缓存不存在才请求网络，否则使用缓存。
        //FIRST_CACHE_THEN_REQUEST：先使用缓存，不管是否存在，仍然请求网络。
        //缓存的过期时间,单位毫秒
        //为确保未设置缓存请求几乎不做缓存，此处默认缓存时间暂设为5秒
        retrofitParams.setCache(validParam.getApiCheckAnnotation().IsCache());
        String cacheKey = MessageFormat.format(validParam.getCacheKey(), apiClass.getSimpleName());
        retrofitParams.setCacheKey(cacheKey);
        if (retrofitParams.isCache()) {
            long milliseconds = ConvertUtils.toMilliseconds(validParam.getApiCheckAnnotation().CacheTime(),
                    validParam.getApiCheckAnnotation().CacheTimeUnit());
            retrofitParams.setCacheTime(milliseconds);
        } else {
            retrofitParams.setCacheTime(5000);
        }
        //拼接完整的url
        //del请求看delQuery参数是不是为空
        if (!ObjectJudge.isNullOrEmpty(retrofitParams.getDelQueryParams())) {
            StringBuffer querysb = new StringBuffer();
            for (Map.Entry<String, String> entry : retrofitParams.getDelQueryParams().entrySet()) {
                querysb.append(MessageFormat.format("{0}={1},", entry.getKey(), entry.getValue()));
            }
            if (querysb.length() > 0) {
                if (retrofitParams.getRequestUrl().indexOf("?") < 0) {
                    retrofitParams.setRequestUrl(String.format("%s?%s",
                            retrofitParams.getRequestUrl(),
                            querysb.substring(0, querysb.length() - 1)));
                } else {
                    retrofitParams.setRequestUrl(String.format("%s&%s",
                            retrofitParams.getRequestUrl(),
                            querysb.substring(0, querysb.length() - 1)));
                }
            }
        }
        baseSubscriber.setOnUnLoginCallInfoListener(listener);
        baseSubscriber.setErrorListener(errorListener);
        String apiRequestKey = GlobalUtils.getNewGuid();
        server.<T>baseConfig(context, server, retrofitParams, validParam,
                new Action3<T, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(T t, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        baseSubscriber.onNext(t, reqQueueItemHashMap, apiRequestKey);
                    }
                }, apiRequestKey);
        OkRxConfigParams globalHeaders = OkRx.getInstance().getOkRxConfigParams();
        if (globalHeaders.isDebug()) {
            printLog(apiRequestKey, retrofitParams);
        }
    }

    private void printLog(String apiRequestKey, RetrofitParams retrofitParams) {
        try {
            if (retrofitParams == null) {
                return;
            }
            if (retrofitParams.isPrintApiLog()) {
                StringBuffer logsb = new StringBuffer();
                logsb.append("===============================================================\n");
                logsb.append(String.format("接口名:%s\n", retrofitParams.getApiName()));
                logsb.append(String.format("请求类型:%s\n", retrofitParams.getRequestType().name()));
                logsb.append(String.format("请求token:%s=%s\n", retrofitParams.getTokenName(), token));
                logsb.append(String.format("请求地址:%s\n", retrofitParams.getRequestUrl()));
                logsb.append(String.format("Header信息:%s\n", JsonUtils.toStr(retrofitParams.getHeadParams())));
                if (retrofitParams.getRequestType() == RequestType.DELETE) {
                    logsb.append(String.format("请求参数:%s\n", JsonUtils.toStr(retrofitParams.getDelQueryParams())));
                } else {
                    logsb.append(String.format("请求参数:%s\n", JsonUtils.toStr(retrofitParams.getParams())));
                }
                logsb.append(String.format("数据提交方式:%s\n", retrofitParams.getRequestContentType().name()));
                logsb.append(String.format("缓存信息:isCache=%s,cacheKey=%s,cacheTime=%s\n",
                        retrofitParams.isCache(),
                        retrofitParams.getCacheKey(),
                        retrofitParams.getCacheTime()));
                logsb.append(String.format("返回数据类名:%s\n", retrofitParams.getDataClass().getSimpleName()));
                logsb.append(String.format("验证是否通过:%s\n", retrofitParams.getFlag()));
                logsb.append(String.format("允许接口返回码:%s\n", retrofitParams.getAllowRetCodes()));
                logmaps.put(apiRequestKey, logsb);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
}
