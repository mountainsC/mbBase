package com.cloud.core.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/1/12
 * @Description:基础配置属性
 * @Modifier:
 * @ModifyContent:
 */
public class BasicConfigBean {
    /**
     * img url
     */
    private String imgUrl = "";
    /**
     * h5 url
     */
    private String h5Url = "";
    /**
     * api url
     */
    private String apiUrl = "";
    /**
     * 引用地址
     */
    private String referer = "";

    /**
     * 获取img
     * url
     */
    public String getImgUrl() {
        if (imgUrl == null) {
            imgUrl = "";
        }
        return imgUrl;
    }

    /**
     * 设置img
     * url
     *
     * @param imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取h5
     * url
     */
    public String getH5Url() {
        if (h5Url == null) {
            h5Url = "";
        }
        return h5Url;
    }

    /**
     * 设置h5
     * url
     *
     * @param h5Url
     */
    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    /**
     * 获取api
     * url
     */
    public String getApiUrl() {
        if (apiUrl == null) {
            apiUrl = "";
        }
        return apiUrl;
    }

    /**
     * 设置api
     * url
     *
     * @param apiUrl
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
