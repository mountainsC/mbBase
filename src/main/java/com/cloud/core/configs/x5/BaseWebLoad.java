package com.cloud.core.configs.x5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.bases.BaseApplication;
import com.cloud.core.dialog.BaseMessageBox;
import com.cloud.core.enums.DialogButtonsEnum;
import com.cloud.core.enums.MsgBoxClickButtonEnum;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.ToastUtils;
import com.cloud.core.utils.ValidUtils;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseWebLoad extends WebView implements OnInitUserAgentListener {

    private ProgressBar progressBar = null;
    private boolean isParseError = false;
    private OnInitUserAgentListener onInitUserAgentListener = null;

    public BaseWebLoad(Context context, AttributeSet attrs) {
        super(context, attrs);
        onPreCreated(context);
        init();
        initListener();
    }

    /**
     * ?????????webview??????
     *
     * @param activity ??????h5??????
     */
    public void initializa(Activity activity) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window != null) {
            window.setFormat(PixelFormat.TRANSLUCENT);
            if (Build.VERSION.SDK_INT >= 11) {
                window.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                );
            }
        }
    }

    private void init() {
        try {
            RelativeLayout.LayoutParams vparam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, PixelUtils.dip2px(getContext(), 3));
            progressBar = new ProgressBar(getContext());
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            progressBar.setLayoutParams(vparam);
            progressBar.setVisibility(GONE);
            Drawable mdrawable = progressBar.getResources().getDrawable(R.drawable.progressbar_style);
            progressBar.setProgressDrawable(mdrawable);
            this.addView(progressBar);
            initSetting();
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void initSetting() {
        try {
            this.requestFocus();
            this.setVerticalScrollbarOverlay(false);
            this.setHorizontalScrollbarOverlay(false);
            this.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            this.removeJavascriptInterface("searchBoxJavaBridge_");
            this.removeJavascriptInterface("accessibilityTra");
            this.removeJavascriptInterface("accessibility");

            WebSettings settings = this.getSettings();
            if (settings != null) {
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setAllowFileAccess(true);
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                settings.setSupportZoom(true);
                settings.setBuiltInZoomControls(true);
                settings.setUseWideViewPort(true);
                settings.setSupportMultipleWindows(true);
                settings.setLoadWithOverviewMode(true);
                settings.setAppCacheEnabled(true);
                settings.setDatabaseEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setGeolocationEnabled(true);
                settings.setAppCacheMaxSize(Long.MAX_VALUE);
                settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
                settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                settings.setDefaultTextEncodingName("utf-8");
                File database = this.getContext().getDir("database", Context.MODE_PRIVATE);
                if (database != null) {
                    settings.setDatabasePath(database.getPath());
                }
                File geolocation = this.getContext().getDir("geolocation", Context.MODE_APPEND);
                if (geolocation != null) {
                    settings.setGeolocationDatabasePath(geolocation.getPath());
                }
                File appcache = this.getContext().getDir("appcache", Context.MODE_PRIVATE);
                if (appcache != null) {
                    settings.setAppCachePath(appcache.getPath());
                }
                settings.setBlockNetworkImage(false);
                settings.setLoadsImagesAutomatically(true);
                settings.setSavePassword(true);
                List<String> userAgents = new ArrayList<String>();
                addUserAgent(userAgents);
                if (!ObjectJudge.isNullOrEmpty(userAgents)) {
                    String join = ConvertUtils.toJoin(userAgents, ";");
                    String agentString = settings.getUserAgentString();
                    settings.setUserAgent(String.format("%s;%s", join, agentString));
                }
            }
            CookieSyncManager.createInstance(getContext());
            CookieSyncManager.getInstance().sync();
            this.setClickable(true);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
    //??????app????????????
    private boolean isInstall(Intent intent) {
        return BaseApplication.getInstance().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
    private void initListener() {
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                webView.getContext().startActivity();
                if (url.contains("alipays://platformapi/startapp")) {//??????
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (isInstall(intent)) {
                        webView.getContext().startActivity(intent);
                    } else {
                        ToastUtils.showShort(webView.getContext(),"?????????????????????App");
                    }
                }
                return onOverrideUrlLoading(webView, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                isParseError = true;
                onLoadFinished(view, false, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon) {
                progressBar.setVisibility(VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                if (!isParseError) {
                    onLoadFinished(webView, true, 0, "", url);
                }
                progressBar.setProgress(0);
                progressBar.setVisibility(GONE);
            }
        });
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                mbox.setContentGravity(Gravity.LEFT);
                mbox.setContent(message);
                mbox.setShowTitle(false);
                mbox.setTarget("ON_JS_CONFIRM_TARGET", result);
                mbox.show(view.getContext(), DialogButtonsEnum.ConfirmCancel);
                return true;
            }

            private View createPromptEditView(Context context, String defaultText) {
                LinearLayout ll = new LinearLayout(context);
                LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        PixelUtils.dip2px(context, 32));
                EditText editText = new EditText(context);
                editText.setLayoutParams(llparam);
                editText.setPadding(2, 1, 2, 1);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                editText.setTextColor(Color.parseColor("#323232"));
                editText.setHintTextColor(Color.parseColor("#999999"));
                editText.setText(defaultText);
                ll.addView(editText);
                return ll;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                mbox.setContentGravity(Gravity.LEFT);
                mbox.setShowTitle(true);
                mbox.setTitle(message);
                mbox.setContentView(createPromptEditView(view.getContext(), defaultValue));
                Object[] extras = {result, defaultValue};
                mbox.setTarget("ON_JS_PROMPT_TARGET", extras);
                mbox.show(view.getContext(), DialogButtonsEnum.ConfirmCancel);
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                mbox.setContentGravity(Gravity.CENTER);
                mbox.setContent(message);
                mbox.setShowTitle(false);
                mbox.setTarget("ON_JS_ALERT_TARGET", result);
                mbox.show(view.getContext(), DialogButtonsEnum.Confirm);
                result.cancel();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.postInvalidate();
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                BaseWebLoad.this.onReceivedTitle(title);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
                callback.invoke(origin, true, false);
            }

            //???????????????????????????
            //3.0++??????
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            //3.0--??????
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                openFileChooserImpl(valueCallback);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImplForSdk5(uploadMsg);
                return true;
            }
        });
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {

            }
        });
    }

    private BaseMessageBox mbox = new BaseMessageBox() {
        @Override
        public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum, String target, Object extraData) {
            if (TextUtils.equals(target, "ON_JS_CONFIRM_TARGET")) {
                JsResult result = (JsResult) extraData;
                if (result == null) {
                    if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                        result.confirm();
                    } else {
                        result.cancel();
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_PROMPT_TARGET")) {
                if (extraData == null) {
                    dismiss();
                } else {
                    if (extraData instanceof Object[]) {
                        Object[] extras = (Object[]) extraData;
                        if (extras == null || extras.length != 2) {
                            dismiss();
                        } else {
                            JsPromptResult promptResult = (JsPromptResult) extras[0];
                            String defaultText = String.valueOf(extras[1]);
                            if (promptResult != null && !TextUtils.isEmpty(defaultText)) {
                                if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                                    promptResult.confirm(defaultText);
                                } else {
                                    promptResult.cancel();
                                }
                            } else {
                                dismiss();
                            }
                        }
                    } else {
                        dismiss();
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_ALERT_TARGET")) {
                if (extraData == null) {
                    dismiss();
                } else {
                    JsResult result = (JsResult) extraData;
                    result.confirm();
                }
            } else {
                dismiss();
            }
            return true;
        }
    };

    /**
     * ??????webviewz
     */
    public void onDestroy() {
        mbox.dismiss();
        this.destroy();
    }

    /**
     * ???webview??????????????????
     *
     * @param context ???????????????
     */
    protected void onPreCreated(Context context) {

    }

    /**
     * ???WebViewClient???onOverrideUrlLoading????????????
     *
     * @param webView ??????webview??????
     * @param url     ????????????url
     * @return
     */
    protected boolean onOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    /**
     * webview?????????????????????
     *
     * @param webView     ??????webview??????
     * @param success     true-????????????;false-????????????;
     * @param errorCode   success=false?????????????????????
     * @param description success=false????????????????????????
     * @param failingUrl  success=false??????????????????url
     */
    protected void onLoadFinished(WebView webView, boolean success, int errorCode, String description, String failingUrl) {

    }

    /**
     * ????????????webview???????????????
     *
     * @param title webview??????
     */
    protected void onReceivedTitle(String title) {

    }

    /**
     * ??????WebChromeClient???openFileChooser??????
     * ??????????????????????????? 3.0++??????
     *
     * @param uploadMsg ??????????????????
     */
    protected void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {

    }

    /**
     * ??????WebChromeClient???onShowFileChooser??????
     *
     * @param uploadMsg ??????????????????
     */
    protected void openFileChooserImplForSdk5(ValueCallback<Uri[]> uploadMsg) {

    }

    /**
     * ?????????????????????
     *
     * @return
     */
    protected Map<String, String> getExtraHeaders(
            Map<String, String> extraHeaders) {
        return extraHeaders;
    }

    /**
     * ???post????????????url???????????????
     *
     * @param url      ????????????url
     * @param postData ???????????????
     */
    public void postUrl(String url, HashMap<String, String> postData) {
        try {
            isParseError = false;
            String data = "";
            if (!ObjectJudge.isNullOrEmpty(postData)) {
                Iterator<Map.Entry<String, String>> iter = postData
                        .entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
                            .next();
                    sb.append(String.format("%s=%s&", entry.getKey(),
                            URLEncoder.encode(entry.getValue(), "utf-8")));
                }
                if (sb.length() > 0) {
                    data = sb.substring(0, sb.length() - 1);
                }
            }
            if (!TextUtils.isEmpty(data)) {
                this.postUrl(url, data.getBytes());
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * ???post????????????url???????????????
     *
     * @param url ????????????url
     */
    public void postUrl(String url) {
        //????????????????????????HashMap????????????webview.postUrl(String url,byte[] bytes)??????
        postUrl(url, (HashMap<String, String>) null);
    }

    /**
     * ??????url
     *
     * @param url          ??????url
     * @param extraHeaders ??????????????????
     */
    public void loadUrl(String url, Map<String, String> extraHeaders) {
        try {
            isParseError = false;
            if (extraHeaders == null) {
                extraHeaders = new HashMap<String, String>();
            }
            Map<String, String> headersdata = getExtraHeaders(extraHeaders);
            if (headersdata == null) {
                super.loadDataWithBaseURL(url, "", "text/html", "utf-8", "");
            } else {
                super.loadUrl(url, headersdata);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * ??????url
     *
     * @param url ??????url
     */
    public void loadUrl(String url) {
        loadUrl(url, null);
    }

    /**
     * ??????html??????
     *
     * @param htmlContent html??????
     */
    public void loadData(String htmlContent) {
        try {
            isParseError = false;
            if (htmlContent.contains("<html>")) {
                super.loadDataWithBaseURL("", htmlContent, "text/html", "utf-8", "");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("<!DOCTYPE html>");
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<meta charset=\"utf-8\"/>");
                sb.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\"/>");
                sb.append("<style type=\"text/css\">body,div,ul,li {padding: 0;margin: 0;display: block;}");
                sb.append("img{max-width:100% !important; width:auto; height:auto;}</style>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append(htmlContent);
                sb.append("</body>");
                sb.append("</html>");
                sb.append("<script type=\"text/javascript\">window.onload = function() {var imgs = document.getElementsByTagName('img');for(var i in imgs) {imgs[i].style.maxWidth = '100% !important';}};</script>");
                super.loadDataWithBaseURL("", sb.toString(), "text/html", "utf-8", "");
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * ??????web?????????????????????
     *
     * @param activity
     * @param flag                   true????????????????????????,false??????????????????
     * @param finishOrGoBackListener ??????h5????????????????????????
     */
    public void finishOrGoBack(Activity activity, boolean flag, OnFinishOrGoBackListener finishOrGoBackListener) {
        try {
            if (flag) {
                activity.finish();
            } else {
                if (this.canGoBack()) {
                    this.goBack();
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(this.canGoBack());
                    }
                } else {
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(false);
                    } else {
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * ??????web?????????????????????
     *
     * @param activity
     */
    public void finishOrGoBack(Activity activity) {
        finishOrGoBack(activity, true, null);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param bgcolor   ????????????
     * @param thisColor ????????????
     * @return true-??????;false-?????????;
     */
    protected boolean isMatchThisColor(String bgcolor, String thisColor) {
        try {
            if (TextUtils.isEmpty(bgcolor)) {
                return false;
            }
            String pattern = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\(", "\\)");
            String text = ValidUtils.matche(pattern, bgcolor);
            String mcolor = "";
            if (TextUtils.isEmpty(text)) {
                mcolor = bgcolor;
            } else {
                int r = 0, g = 0, b = 0;
                String[] rgbs = text.split(",");
                if (rgbs.length == 3) {
                    r = ConvertUtils.toInt(rgbs[0]);
                    g = ConvertUtils.toInt(rgbs[1]);
                    b = ConvertUtils.toInt(rgbs[2]);
                    mcolor = ConvertUtils.toRGBHex(r, g, b);
                } else if (rgbs.length == 4) {
                    if (TextUtils.equals(rgbs[0], "0")) {
                        mcolor = thisColor;
                    }
                }
            }
            boolean isWhite = false;
            if (!TextUtils.isEmpty(mcolor)) {
                mcolor = mcolor.toLowerCase();
                if (TextUtils.equals(mcolor, thisColor)) {
                    isWhite = true;
                }
            }
            return isWhite;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ??????web????????????
     */
    protected void getWebBackgroundColor() {
        this.loadUrl("javascript:window.mibao.getBackgroundColor(window.getComputedStyle(document.body,null).getPropertyValue('background-color'))");
    }

    /**
     * ?????????????????????
     */
    protected void getSelectText() {
        this.loadUrl("javascript:window.mibao.getSelectText(window.getSelection().toString());");
    }
}
