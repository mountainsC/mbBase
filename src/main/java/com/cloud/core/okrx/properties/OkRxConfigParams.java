package com.cloud.core.okrx.properties;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/8
 * Description:OkRx配置参数
 * Modifier:
 * ModifyContent:
 */
public class OkRxConfigParams {

    //连接超时时间(毫秒)
    private long connectTimeout = 10000;
    //读取超时时间(毫秒)
    private long readTimeOut = 10000;
    //写入超时时间(毫秒)
    private long writeTimeOut = 10000;
    //重连次数
    private int retryCount = 3;
    //公共头参数
    private HashMap<String, String> headers = null;
    //true-debug模式;false-非debug模式(相关日志不打印)
    private boolean isDebug = false;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public long getWriteTimeOut() {
        return writeTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public HashMap<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
