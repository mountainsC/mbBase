package com.cloud.core.okrx.properties;


import com.cloud.core.annotations.ApiCheckAnnotation;
import com.cloud.core.annotations.ReturnCodeFilter;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/8
 * Description:验证返回参数
 * Modifier:
 * ModifyContent:
 */
public class OkRxValidParam {
    private ApiCheckAnnotation apiCheckAnnotation = null;
    /**
     * 判断验证是否通过
     */
    private boolean flag = false;
    /**
     * 网络请求缓存key
     */
    private String cacheKey = "";
    /**
     * 是否需要登录
     */
    private boolean isNeedLogin = false;
    /**
     * api返回过滤器
     */
    private ReturnCodeFilter returnCodeFilter = null;

    public ApiCheckAnnotation getApiCheckAnnotation() {
        return apiCheckAnnotation;
    }

    public void setApiCheckAnnotation(ApiCheckAnnotation apiCheckAnnotation) {
        this.apiCheckAnnotation = apiCheckAnnotation;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        isNeedLogin = needLogin;
    }

    public ReturnCodeFilter getReturnCodeFilter() {
        return returnCodeFilter;
    }

    public void setReturnCodeFilter(ReturnCodeFilter returnCodeFilter) {
        this.returnCodeFilter = returnCodeFilter;
    }
}
