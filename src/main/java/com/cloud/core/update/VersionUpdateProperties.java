package com.cloud.core.update;

import android.app.Activity;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/8/6
 * @Description:版本更新属性
 * @Modifier:
 * @ModifyContent:
 */
public class VersionUpdateProperties {
    /**
     * activity
     */
    private Activity activity = null;
    /**
     * 是否自动更新
     */
    private boolean isAutoUpdate = false;
    /**
     * 应用图标
     */
    private int appIcon = 0;
    /**
     * 适用于远程存放xml配置文件
     */
    private String checkUpdateUrl = "";
    /**
     * 请求头参数
     */
    private HashMap<String, String> httpHeaders = null;
    /**
     * 请求参数
     */
    private HashMap<String, Object> httpParams = null;

    /**
     * 获取activity
     */
    public Activity getActivity() {
        if (activity == null) {
            activity = new Activity();
        }
        return activity;
    }

    /**
     * 设置activity
     *
     * @param activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取是否自动更新
     */
    public boolean getIsAutoUpdate() {
        return isAutoUpdate;
    }

    /**
     * 设置是否自动更新
     *
     * @param isAutoUpdate
     */
    public void setIsAutoUpdate(boolean isAutoUpdate) {
        this.isAutoUpdate = isAutoUpdate;
    }

    /**
     * 获取应用图标
     */
    public int getAppIcon() {
        return appIcon;
    }

    /**
     * 设置应用图标
     *
     * @param appIcon
     */
    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    /**
     * 获取适用于远程存放xml配置文件
     */
    public String getCheckUpdateUrl() {
        if (checkUpdateUrl == null) {
            checkUpdateUrl = "";
        }
        return checkUpdateUrl;
    }

    /**
     * 设置适用于远程存放xml配置文件
     *
     * @param checkUpdateUrl
     */
    public void setCheckUpdateUrl(String checkUpdateUrl) {
        this.checkUpdateUrl = checkUpdateUrl;
    }

    public HashMap<String, String> getHttpHeaders() {
        if (httpHeaders == null) {
            httpHeaders = new HashMap<String, String>();
        }
        return httpHeaders;
    }

    public void setHttpHeaders(HashMap<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public HashMap<String, Object> getHttpParams() {
        if (httpParams == null) {
            httpParams = new HashMap<String, Object>();
        }
        return httpParams;
    }

    public void setHttpParams(HashMap<String, Object> httpParams) {
        this.httpParams = httpParams;
    }
}
