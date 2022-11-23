package com.cloud.core.configs.h5;

import com.tencent.smtt.sdk.WebView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/30
 * @Description:部分监听需要在WebviewActivity中回调
 * @Modifier:
 * @ModifyContent:
 */
public abstract class OnH5Calls {
    public abstract void onTitle(String title);

    public abstract void onHeadLineColor(boolean isMatchThisColor);

    public void openAliAuth(String authJson){

    }

    public void realNameCallback(String authJson){

    }

    public void jdAuthCall(String authResult){

    }

    public abstract void onOpenAppUiBySchemeUrl(String schemeUrl);

    public abstract void onShare(String shareKey);

    public abstract void onGetH5TagContent(String content);

    public abstract void onDidAppLogin(String token);

    public void onLoadFinished(WebView view, boolean success, int errorCode, String description, String url){

    }

    public abstract void instalmentPayResult(String payResultUrl);

    public abstract void onLoadFinished(android.webkit.WebView view, boolean success, int errorCode, String description, String url);
}
