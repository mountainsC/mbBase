package com.cloud.core.okrx;

import android.content.Context;

import com.cloud.core.cache.RxCache;
import com.cloud.core.okrx.cookie.CookieJarImpl;
import com.cloud.core.okrx.cookie.store.SPCookieStore;
import com.cloud.core.okrx.events.OnConfigParamsListener;
import com.cloud.core.okrx.properties.OkRxConfigParams;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Author Gs
 * Email:gs_12@foxmail.com
 * CreateTime:2017/6/1
 * Description: OkGo基础
 * Modifier:
 * ModifyContent:
 */

public class OkRx {

    private static OkRx okRx = null;
    private OkRxConfigParams okRxConfigParams = null;
    private OnConfigParamsListener onConfigParamsListener = null;

    public static OkRx getInstance() {
        if (okRx == null) {
            okRx = new OkRx();
        }
        return okRx;
    }

    /**
     * 设置全局配置参数监听
     *
     * @param listener 全局配置参数监听
     */
    public void setOnConfigParamsListener(OnConfigParamsListener listener) {
        this.onConfigParamsListener = listener;
    }

    /**
     * okrx初始化
     * (一般在Application初始化时调用)
     *
     * @param context 上下文
     */
    public void Initialize(Context context) {
        if (onConfigParamsListener != null) {
            okRxConfigParams = onConfigParamsListener.onConfigParamsCall();
        }
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        //缓存okRxConfigParams参数
        OkHttpClient client = newHttpClient(context, okRxConfigParams);
        RxCache.setObjectValue(OkRxKeys.okhttpClientKey, client);
    }

    /**
     * 重新构建http client
     *
     * @param context          上下文
     * @param okRxConfigParams 全局配置参数
     * @return OkHttpClient
     */
    public OkHttpClient newHttpClient(Context context, OkRxConfigParams okRxConfigParams) {
        //创建http client对象
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //连接超时
        builder.connectTimeout(okRxConfigParams.getConnectTimeout(), TimeUnit.MILLISECONDS);
        //读取超时
        builder.readTimeout(okRxConfigParams.getReadTimeOut(), TimeUnit.MILLISECONDS);
        //写入超时
        builder.writeTimeout(okRxConfigParams.getWriteTimeOut(), TimeUnit.MILLISECONDS);
        //设置失败时重连次数,请求头信息
        builder.addInterceptor(new RequestRetryIntercepter(okRxConfigParams.getRetryCount(), okRxConfigParams.getHeaders()));
        //cookies持久化
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        //添加证书信任
        SslSocketManager.SSLParams sslParams1 = SslSocketManager.getSslSocketFactory();
        if (sslParams1 != null) {
            builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        }
        return builder.build();
    }

    /**
     * 获取http client对象
     *
     * @param context 上下文
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient(Context context) {
        Object objectValue = RxCache.getObjectValue(OkRxKeys.okhttpClientKey);
        if ((objectValue instanceof OkHttpClient)) {
            OkHttpClient httpClient = (OkHttpClient) objectValue;
            return httpClient;
        }
        OkRxConfigParams configParams = getOkRxConfigParams();
        OkHttpClient client = newHttpClient(context, configParams);
        RxCache.setObjectValue(OkRxKeys.okhttpClientKey, client);
        return client;
    }

    /**
     * 获取okrx全局配置参数
     *
     * @return
     */
    public OkRxConfigParams getOkRxConfigParams() {
        if (okRxConfigParams == null) {
            if (onConfigParamsListener != null) {
                okRxConfigParams = onConfigParamsListener.onConfigParamsCall();
            }
        }
        //再次判断若全局参数为空则重新创建参数
        if (okRxConfigParams == null) {
            okRxConfigParams = new OkRxConfigParams();
        }
        return okRxConfigParams;
    }
}
