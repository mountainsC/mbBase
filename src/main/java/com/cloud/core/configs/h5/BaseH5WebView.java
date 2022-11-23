package com.cloud.core.configs.h5;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cloud.core.R;
import com.cloud.core.configs.x5.BaseWebLoad;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseH5WebView extends BaseWebLoad {

    private OnH5WebViewListener onH5WebViewListener = null;
    private Activity activity = null;
    private OnFileChooserCall onFileChooserCall = null;

    public BaseH5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnFileChooserCall(OnFileChooserCall chooserCall) {
        this.onFileChooserCall = chooserCall;
    }

    /**
     * @param
     */
    public void setOnH5WebViewListener(OnH5WebViewListener listener) {
        this.onH5WebViewListener = listener;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    protected void onReceivedTitle(String title) {
        if (onH5WebViewListener != null) {
            onH5WebViewListener.onTitle(title);
        }
    }

    public String clipTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return activity.getResources().getString(R.string.app_name);
        } else {
            if (!TextUtils.isEmpty(title)) {
                if (title.length() > 15) {
                    title = title.substring(0, 15) + "...";
                }
            }
            return title;
        }
    }

    @Override
    protected void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        if (onFileChooserCall != null) {
            onFileChooserCall.onFileChooser(uploadMsg);
        }
    }

    @Override
    protected void openFileChooserImplForSdk5(ValueCallback<Uri[]> uploadMsg) {
        if (onFileChooserCall != null) {
            onFileChooserCall.onFileChooserSdk5(uploadMsg);
        }
    }

    /**
     * 获取h5标签内容
     *
     * @param tagId 标签id
     */
    public void getH5TagContent(String tagId) {
        if (onH5WebViewListener == null) {
            return;
        }
        this.loadUrl("javascript:window." + onH5WebViewListener.getJsInterfaceName() + ".getH5TagContent(document.getElementById('" + tagId + "').innerText);");
    }

    @Override
    protected void onLoadFinished(WebView webView, boolean success, int errorCode, String description, String url) {
        if (onH5WebViewListener == null || TextUtils.isEmpty(url) || url.contains("javascript:")) {
            return;
        }
        onH5WebViewListener.onLoaded(webView, success, errorCode, description, url);
    }
}
