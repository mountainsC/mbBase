package com.cloud.core.configs.h5;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cloud.core.beans.BasicConfigBean;
import com.cloud.core.beans.user.UserCacheInfo;
import com.cloud.core.configs.APIConfigProcess;
import com.cloud.core.configs.UserDataCache;
import com.cloud.core.enums.ActivityNames;
import com.cloud.core.utils.RedirectUtils;
import com.cloud.core.utils.RoutesUtils;
import com.cloud.core.behavior.BehaviorType;
import com.cloud.core.utils.BaseCommonUtils;
import com.cloud.core.utils.GlobalUtils;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class H5WebView extends BaseH5WebView implements OnH5WebViewListener {

    private H5ForJs h5ForJs = new H5ForJs();
    private OnH5Calls onH5Calls = null;

    public H5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(Activity activity, OnH5Calls onH5Calls) {
        this.onH5Calls = onH5Calls;
        //事件
        super.setOnH5WebViewListener(this);
        h5ForJs.setOnH5WebViewListener(this);
        //设置选择文本监听
        //h5ForJs.setOnSelectTextListener(this);
        //设置webview
        h5ForJs.setBaseWebLoad(this);
        //设置activity
        h5ForJs.setActivity(activity);
        super.setActivity(activity);
        //注册脚本
        super.addJavascriptInterface(h5ForJs, this.getJsInterfaceName());

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String flag) {
//        loginSuccessCall();
    }

    private boolean interceptEffectiveScheme(String url) {
        HashMap<String, String> urlParams = GlobalUtils.getUrlParams(url);
        if (urlParams.containsKey("schemeUrl")) {
            String schemeUrl = urlParams.get("schemeUrl");
            if (!TextUtils.isEmpty(schemeUrl)) {
                h5ForJs.openAppUiBySchemeUrl(schemeUrl);
                return true;
            } else {
                if (h5ForJs.getOnH5WebViewListener() != null) {
                    return h5ForJs.getOnH5WebViewListener().onUrlListener(url);
                }
            }
        } else {
            if (h5ForJs.getOnH5WebViewListener() != null) {
                return h5ForJs.getOnH5WebViewListener().onUrlListener(url);
            }
        }
        return false;
    }

    @Override
    protected boolean onOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.contains("tel:")) {
            RedirectUtils.callTel(getActivity(), url);
            return true;
        }
        //拦截apk地址(如果以apk为后缀的则直接下载)
        String suffixName = GlobalUtils.getSuffixName(url);
        if (!TextUtils.isEmpty(suffixName) && TextUtils.equals(suffixName, "apk")) {
            BaseCommonUtils.downApp(getContext(), url, "");
            return true;
        }
        //拦截有效schemeUrl
        return interceptEffectiveScheme(url);
    }

    @Override
    public void onLoaded(WebView view, boolean success, int errorCode, String description, String url) {
        if (success) {
            view.loadUrl(JsMethods.loadCompleteJs);
            super.getWebBackgroundColor();
        }
        if (onH5Calls != null) {
            onH5Calls.onLoadFinished(view, success, errorCode, description, url);
        }
    }

    /**
     * 登录成功后需要回调此方法
     */
    public void loginSuccessCall() {
        super.loadUrl(JsMethods.loginSuccessJs);
    }

    @Override
    public void addUserAgent(List<String> userAgents) {
        userAgents.add("mbApp");
    }

    @Override
    public void onTitle(String title) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onTitle(title);
    }

    @Override
    public String getJsInterfaceName() {
        return "mibao";
    }

    @Override
    public Object[] onGetJavascriptInterfaces() {
        Object[] objects = new Object[]{h5ForJs};
        return objects;
    }

    @Override
    public boolean onUrlListener(String url) {
        return false;
    }

    @Override
    public String onGetApiBaseUrl() {
        BasicConfigBean configBean = APIConfigProcess.getInstance().getBasicConfigBean(getActivity());
        return configBean.getApiUrl();
    }

    @Override
    public String onGetToken() {
        UserCacheInfo cacheUserInfo = UserDataCache.getDefault().getCacheUserInfo(getContext());
        return cacheUserInfo.getToken();
    }

    @Override
    public void onShowLogin(Activity activity, boolean isCallback) {
        RoutesUtils.startActivity(activity, ActivityNames.LoginActivity, null);
    }

    @Override
    public void onHeadLineColor(boolean isMatchThisColor) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onHeadLineColor(isMatchThisColor);
    }

    @Override
    public void onOpenAppUiBySchemeUrl(String schemeUrl) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onOpenAppUiBySchemeUrl(schemeUrl);
    }

    @Override
    public void onShare(String shareKey) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onShare(shareKey);
    }

    @Override
    public void onBehaviorStatistics(Context context, String eventId, BehaviorType behaviorType, HashMap<String, String> map, int du) {

    }

    @Override
    public void openAliAuth(String authJson) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.openAliAuth(authJson);
    }

    @Override
    public void realNameCallback(String authJson) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.realNameCallback(authJson);
    }

    @Override
    public void jdAuthCall(String authResult) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.jdAuthCall(authResult);
    }

    @Override
    public void onGetH5TagContent(String content) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onGetH5TagContent(content);
    }

    @Override
    public void onInterceptTagContent(String tagId, String content) {

    }

    @Override
    public void onDidAppLogin(String token) {
        if (onH5Calls == null) {
            return;
        }
        onH5Calls.onDidAppLogin(token);
    }
}
