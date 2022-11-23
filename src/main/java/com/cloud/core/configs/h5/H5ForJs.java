package com.cloud.core.configs.h5;

import android.app.Activity;
import android.text.TextUtils;

import com.cloud.core.behavior.BehaviorType;
import com.cloud.core.behavior.EventStatisticalBean;
import com.cloud.core.behavior.UMStatisticalParameItem;
import com.cloud.core.configs.x5.BaseWebLoad;
import com.cloud.core.configs.x5.JavascriptInterface;
import com.cloud.core.events.Func2;
import com.cloud.core.utils.BaseCommonUtils;
import com.cloud.core.ObjectJudge;
import com.cloud.core.logger.Logger;
import com.cloud.core.enums.RequestContentType;
import com.cloud.core.utils.JsonUtils;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/30
 * @Description:h5脚本注册
 * @Modifier:
 * @ModifyContent:
 */
public class H5ForJs {

    private OnH5WebViewListener onH5WebViewListener = null;
    private Activity activity = null;
    private boolean isCallGetBackgroundColor = false;
    private OnSelectTextListener onSelectTextListener = null;
    private BaseWebLoad baseWebLoad = null;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 设置h5 webview监听
     *
     * @param listener h5 webview监听
     */
    public void setOnH5WebViewListener(OnH5WebViewListener listener) {
        this.onH5WebViewListener = listener;
    }

    /**
     * h5 webview监听
     *
     * @return
     */
    public OnH5WebViewListener getOnH5WebViewListener() {
        return this.onH5WebViewListener;
    }

    /**
     * 设置h5选中文本监听
     *
     * @param listener 选中文本监听
     */
    public void setOnSelectTextListener(OnSelectTextListener listener) {
        this.onSelectTextListener = listener;
    }

    /**
     * 设置webview控件
     *
     * @param webLoad webview控件
     */
    public void setBaseWebLoad(BaseWebLoad webLoad) {
        this.baseWebLoad = webLoad;
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public String getToken() {
        if (onH5WebViewListener != null) {
            return onH5WebViewListener.onGetToken();
        } else {
            return "";
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void showLogin(boolean isCallback) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onShowLogin(activity, isCallback);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void downloadApk(String url, String downName) {
        BaseCommonUtils.downApp(activity, url, downName);
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getAPIMethod(String extras) {
        if (onH5WebViewListener == null) {
            return;
        }
        String apiBaseUrl = onH5WebViewListener.onGetApiBaseUrl();
        if (TextUtils.isEmpty(apiBaseUrl)) {
            return;
        }
        HashMap<String,String> headers = new HashMap<String, String>();
        headers.put("Device-type", "android");
        headers.put("token", getToken());
        H5InteractionAPIUtils.getAPIMethod(activity,
                apiBaseUrl,
                extras,
                headers,
                RequestContentType.Json, new Func2<Object, APIRequestState, APIReturnResult>() {
                    @Override
                    public Void call(APIRequestState apiRequestState, APIReturnResult apiReturnResult) {
                        try {
                            if (apiRequestState == APIRequestState.Success) {
                                if (baseWebLoad == null) {
                                    return null;
                                }
                                String json = JsonUtils.toStr(apiReturnResult);
                                json = URLEncoder.encode(json, "utf-8");
                                String script = "javascript:returnAPIResultMethod('" + json + "');";
                                baseWebLoad.loadUrl(script);
                            }
                        } catch (Exception e) {
                            Logger.L.error(e);
                        }
                        return null;
                    }
                });
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void eventStatistical(String statisticalJson) {
        try {
            if (onH5WebViewListener == null) {
                return;
            }
            if (TextUtils.isEmpty(statisticalJson) && !TextUtils.equals(statisticalJson, "undefined")) {
                return;
            }
            EventStatisticalBean eventStatisticalBean = JsonUtils.parseT(statisticalJson, EventStatisticalBean.class);
            if (ObjectJudge.isNullOrEmpty(eventStatisticalBean.getStatisticalParames())) {
                return;
            }
            HashMap<String, String> statsmap = new HashMap<String, String>();
            for (UMStatisticalParameItem umStatisticalParameItem : eventStatisticalBean.getStatisticalParames()) {
                statsmap.put(umStatisticalParameItem.getNodeName(), umStatisticalParameItem.getNodeValue());
            }
            if (ObjectJudge.isNullOrEmpty(statsmap)) {
                onH5WebViewListener.onBehaviorStatistics(activity, eventStatisticalBean.getEventId(), BehaviorType.Count, null, 0);
            } else {
                onH5WebViewListener.onBehaviorStatistics(activity, eventStatisticalBean.getEventId(), BehaviorType.Properties, statsmap, 0);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void share(String shareKey) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onShare(shareKey);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void clickedEvent(String extras) {

    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getSelectText(String selectText) {
        if (onSelectTextListener != null) {
            onSelectTextListener.onSelectText(selectText);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getBackgroundColor(String bgcolor) {
        if (onH5WebViewListener != null && !isCallGetBackgroundColor) {
            isCallGetBackgroundColor = true;
            boolean matchThisColor = H5Utils.isMatchThisColor(bgcolor, "#ffffff");
            onH5WebViewListener.onHeadLineColor(matchThisColor);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void openAppUiBySchemeUrl(String schemeUrl) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onOpenAppUiBySchemeUrl(schemeUrl);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void openAliAuth(String authJson) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.openAliAuth(authJson);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void realNameCallback(String authJson) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.realNameCallback(authJson);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void jdAuthCall(String authResult) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.jdAuthCall(authResult);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getH5TagContent(String content) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onGetH5TagContent(content);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void onInterceptTagContent(String tagId, String content) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onInterceptTagContent(tagId, content);
        }
    }

    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void doMibaoAPPLogin(String token) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onDidAppLogin(token);
        }
    }
}
