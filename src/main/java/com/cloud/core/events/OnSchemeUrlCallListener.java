package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/26
 * @Description:scheme url回调监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnSchemeUrlCallListener {

    /**
     * 通过此回调获取scheme url
     *
     * @param schemeUrl scheme url
     */
    public void onSchemeUrlCall(String schemeUrl);
}
