package com.cloud.core.configs.h5;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/30
 * @Description:js方法
 * @Modifier:
 * @ModifyContent:
 */
public interface JsMethods {
    /**
     * 加载完成js
     */
    public String loadCompleteJs = "javascript:nativeBridgeJsLoadComplate();";
    /**
     * 登录成功
     */
    public String loginSuccessJs = "javascript:nativeBridgeJsLoginComplate();";
}
