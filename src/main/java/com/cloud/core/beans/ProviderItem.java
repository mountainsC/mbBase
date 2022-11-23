package com.cloud.core.beans;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/6
 * @Description:content provider传递数据
 * @Modifier:
 * @ModifyContent:
 */
public class ProviderItem {
    /**
     * activity全类名
     */
    private String activityFullClassName = "";
    /**
     * activity短类名
     */
    private String activityClassName = "";
    /**
     * bundle json
     */
    private String bundleJson = null;
    /**
     * request code
     */
    private int requestCode = 0;
    /**
     * 转换协议
     */
    private String scheme = "";
    /**
     * 参数列表(scheme参数key-页面参数key)
     */
    private HashMap<String, String> paramList = null;

    public String getActivityFullClassName() {
        return activityFullClassName;
    }

    public void setActivityFullClassName(String activityFullClassName) {
        this.activityFullClassName = activityFullClassName;
    }

    public String getActivityClassName() {
        return activityClassName;
    }

    public void setActivityClassName(String activityClassName) {
        this.activityClassName = activityClassName;
    }

    public String getBundleJson() {
        return bundleJson;
    }

    public void setBundleJson(String bundleJson) {
        this.bundleJson = bundleJson;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public HashMap<String, String> getParamList() {
        if (paramList == null) {
            paramList = new HashMap<String, String>();
        }
        return paramList;
    }

    public void setParamList(HashMap<String, String> paramList) {
        this.paramList = paramList;
    }
}
