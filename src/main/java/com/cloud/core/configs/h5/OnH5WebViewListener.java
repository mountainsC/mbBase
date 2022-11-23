package com.cloud.core.configs.h5;

import android.app.Activity;
import android.content.Context;

import com.cloud.core.behavior.BehaviorType;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/24
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnH5WebViewListener {
    //返回html中标题
    public void onTitle(String title);

    //获取脚本注册前缀
    public String getJsInterfaceName();

    //获取脚本注册接口对象
    public Object[] onGetJavascriptInterfaces();

    //url回调监听
    public boolean onUrlListener(String url);

    //获取api基地址
    public String onGetApiBaseUrl();

    //获取token
    public String onGetToken();

    //显示登录窗口
    public void onShowLogin(Activity activity, boolean isCallback);

    //获取head颜色回调
    public void onHeadLineColor(boolean isMatchThisColor);

    //webview加载完成后回调
    public void onLoaded(WebView view, boolean success, int errorCode, String description, String url);

    //通过scheme url启动页面
    public void onOpenAppUiBySchemeUrl(String schemeUrl);

    //h5调用本地分享回调
    public void onShare(String shareKey);

    //事件统计
    public void onBehaviorStatistics(Context context, String eventId, BehaviorType behaviorType, HashMap<String, String> map, int du);

    public void openAliAuth(String authJson);

    public void realNameCallback(String authJson);

    public void jdAuthCall(String authResult);

    public void onGetH5TagContent(String content);

    public void onInterceptTagContent(String tagId, String content);

    //h5调用app登录
    public void onDidAppLogin(String token);
}
