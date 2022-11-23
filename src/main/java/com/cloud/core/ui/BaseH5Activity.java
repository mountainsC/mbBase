package com.cloud.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.bases.BaseActivity;
import com.cloud.core.beans.SelectImageProperties;
import com.cloud.core.configs.h5.BaseH5WebView;
import com.cloud.core.configs.h5.H5BuildProperties;
import com.cloud.core.configs.h5.OnFileChooserCall;
import com.cloud.core.configs.scheme.jumps.GoPagerTransformer;
import com.cloud.core.configs.x5.X5Codes;
import com.cloud.core.dialog.ImageSelectDialog;
import com.cloud.core.enums.H5LoadType;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.BundleUtils;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.RedirectUtils;
import com.tencent.smtt.sdk.ValueCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/28
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseH5Activity extends BaseActivity implements OnFileChooserCall {

    protected abstract void onBuildCompleted(H5BuildProperties properties);

    @Override
    protected boolean isInitStatistics() {
        return false;
    }

    private BaseH5WebView baseH5WebView = null;
    private ValueCallback<Uri> openUploadMsg = null;
    private ValueCallback<Uri[]> openUploadMsgSdk5 = null;

    private static String getCurrActivityFullClassName(Activity activity) {
        GoPagerTransformer transformer = new GoPagerTransformer();
        Context context = activity.getApplicationContext();
        String packageName = context.getPackageName();
        return transformer.getActivityFullClassName(activity, packageName, "H5WebViewActivity");
    }

    /**
     * 启动h5页面
     *
     * @param activity      activity
     * @param url           目标url地址
     * @param title         标题(不传默认从h5里取)
     * @param isHideShare   是否对当前渲染的h5隐藏分享功能(true-隐藏;false-显示;)
     * @param shareKey      如果isHideShare=true则需要传入获取分享信息的key值
     * @param isLevelReturn true-浏览器内容跳转后点返回，先在浏览器内部返回操作的上一级页面，再关闭当前页面;
     *                      false-直接关闭当前碳
     * @param extraParams   需要传入的扩展参数
     */
    public static void startActivityForUrl(Activity activity,
                                           String url,
                                           String title,
                                           boolean isHideShare,
                                           String shareKey,
                                           boolean isLevelReturn,
                                           HashMap<String, Object> extraParams) {
        Bundle bundle = getStartActivityForUrlBundle(url, title, isHideShare, shareKey, isLevelReturn);
        if (!ObjectJudge.isNullOrEmpty(extraParams)) {
            for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                BundleUtils.setBundleValue(bundle, entry.getKey(), entry.getValue());
            }
        }
        String fullClassName = getCurrActivityFullClassName(activity);
        RedirectUtils.startActivity(activity, fullClassName, bundle);
    }

    /**
     * 启动h5页面
     *
     * @param activity    activity
     * @param url         目标url地址
     * @param title       标题(不传默认从h5里取)
     *                    false-直接关闭当前碳
     * @param extraParams 需要传入的扩展参数
     */
    public static void startActivityForUrl(Activity activity,
                                           String url,
                                           String title,
                                           HashMap<String, Object> extraParams) {
        startActivityForUrl(activity, url, title, true, "", false, extraParams);
    }

    /**
     * 启动h5页面
     *
     * @param activity activity
     * @param url      目标url地址
     * @param title    标题(不传默认从h5里取)
     */
    public static void startActivityForUrl(Activity activity,
                                           String url,
                                           String title) {
        startActivityForUrl(activity, url, title, null);
    }

    /**
     * 启动h5页面
     *
     * @param activity      activity
     * @param htmlContent   需要显示的html内容
     * @param title         标题(不传默认从h5里取)
     * @param isHideShare   是否对当前渲染的h5隐藏分享功能(true-隐藏;false-显示;)
     * @param shareKey      如果isHideShare=true则需要传入获取分享信息的key值
     * @param isLevelReturn true-浏览器内容跳转后点返回，先在浏览器内部返回操作的上一级页面，再关闭当前页面;
     *                      false-直接关闭当前碳
     * @param extraParams   需要传入的扩展参数
     */
    public static void startActivityForHtml(Activity activity,
                                            String htmlContent,
                                            String title,
                                            boolean isHideShare,
                                            String shareKey,
                                            boolean isLevelReturn,
                                            HashMap<String, Object> extraParams) {
        Bundle bundle = getStartActivityForHtmlBundle(htmlContent, title, isHideShare, shareKey, isLevelReturn);
        if (!ObjectJudge.isNullOrEmpty(extraParams)) {
            for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                BundleUtils.setBundleValue(bundle, entry.getKey(), entry.getValue());
            }
        }
        String fullClassName = getCurrActivityFullClassName(activity);
        RedirectUtils.startActivity(activity, fullClassName, bundle);
    }

    /**
     * 启动h5页面
     *
     * @param activity      activity
     * @param htmlContent   需要显示的html内容
     * @param title         标题(不传默认从h5里取)
     * @param isHideShare   是否对当前渲染的h5隐藏分享功能(true-隐藏;false-显示;)
     * @param shareKey      如果isHideShare=true则需要传入获取分享信息的key值
     * @param isLevelReturn true-浏览器内容跳转后点返回，先在浏览器内部返回操作的上一级页面，再关闭当前页面;
     *                      false-直接关闭当前碳
     */
    public static void startActivityForHtml(Activity activity,
                                            String htmlContent,
                                            String title,
                                            boolean isHideShare,
                                            String shareKey,
                                            boolean isLevelReturn) {
        startActivityForHtml(activity, htmlContent, title, isHideShare, shareKey, isLevelReturn, null);
    }

    private static Bundle getStartActivityForUrlBundle(String url,
                                                       String title,
                                                       boolean isHideShare,
                                                       String shareKey,
                                                       boolean isLevelReturn) {
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("title", title);
        bundle.putInt("isHideShare", isHideShare ? 1 : 0);
        bundle.putString("shareKey", shareKey);
        bundle.putInt("isLevelReturn", isLevelReturn ? 1 : 0);
        bundle.putInt("type", H5LoadType.UrlType.ordinal());
        return bundle;
    }

    private static Bundle getStartActivityForHtmlBundle(String htmlContent,
                                                        String title,
                                                        boolean isHideShare,
                                                        String shareKey,
                                                        boolean isLevelReturn) {
        Bundle bundle = new Bundle();
        bundle.putString("HTML_CONTENT", htmlContent);
        bundle.putString("title", title);
        bundle.putInt("isHideShare", isHideShare ? 1 : 0);
        bundle.putString("shareKey", shareKey);
        bundle.putInt("isLevelReturn", isLevelReturn ? 1 : 0);
        bundle.putInt("type", H5LoadType.HtmlType.ordinal());
        return bundle;
    }

    //step 1 初始化
    protected <T extends BaseH5WebView> void init(T t) {
        try {
            this.baseH5WebView = t;
            t.setOnFileChooserCall(this);
            H5BuildProperties properties = new H5BuildProperties();
            properties.setUrl(getStringBundle("URL"));
            String content = getStringBundle("HTML_CONTENT");
            properties.setHtmlContent(content);
            properties.setHideShare(ObjectJudge.isTrue(getIntBundle("isHideShare")));
            properties.setLevelReturn(ObjectJudge.isTrue(getIntBundle("isLevelReturn")));
            HashMap<String, String> urlParams = GlobalUtils.getUrlParams(properties.getUrl());
            //先判断传过来的参数是否对分享隐藏
            //若为true则从url取isHideShare参数值
            if (properties.isHideShare()) {
                if (urlParams.containsKey("isHideShare")) {
                    properties.setHideShare(ObjectJudge.isTrue(urlParams.get("isHideShare")));
                }
            }
            properties.setTitle(getStringBundle("title"));
            //如果标题不为空则进行长度裁剪
            if (!TextUtils.isEmpty(properties.getTitle())) {
                properties.setTitle(t.clipTitle(properties.getTitle()));
            }
            if (urlParams.containsKey("shareKey")) {
                properties.setShareKey(urlParams.get("shareKey"));
            }
            if (TextUtils.isEmpty(properties.getShareKey())) {
                //如果空则从参数中获取
                properties.setShareKey(getStringBundle("shareKey"));
            }
            onBuildCompleted(properties);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    protected void refresh() {
        if (this.baseH5WebView == null) {
            return;
        }
        this.init(baseH5WebView);
    }

    @Override
    public void onFileChooser(ValueCallback<Uri> uploadMsg) {
        this.openUploadMsg = uploadMsg;
        imageSelectDialog.setMaxFileSize(1024);
        imageSelectDialog.setMaxSelectNumber(1);
        imageSelectDialog.setShowTakingPictures(true);
        imageSelectDialog.setTailoring(false);
        imageSelectDialog.setExtra(X5Codes.WEBVIEW_FILECHOOSER_RESULTCODE);
        imageSelectDialog.show(getActivity());
    }

    @Override
    public void onFileChooserSdk5(ValueCallback<Uri[]> uploadMsg) {
        this.openUploadMsgSdk5 = uploadMsg;
        imageSelectDialog.setMaxFileSize(1024);
        imageSelectDialog.setMaxSelectNumber(1);
        imageSelectDialog.setShowTakingPictures(true);
        imageSelectDialog.setTailoring(false);
        imageSelectDialog.setExtra(X5Codes.WEBVIEW_FILECHOOSER_RESULTCODE_SDK5);
        imageSelectDialog.show(getActivity());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectDialog.onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
        @Override
        protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {
            if (ObjectJudge.isNullOrEmpty(selectImageProperties)) {
                return;
            }
            SelectImageProperties selimageitem = selectImageProperties.get(0);
            Uri uri = Uri.parse(selimageitem.getImagePath());
            int flag = ConvertUtils.toInt(extra);
            if (flag == X5Codes.WEBVIEW_FILECHOOSER_RESULTCODE) {
                if (openUploadMsg != null) {
                    openUploadMsg.onReceiveValue(uri);
                }
            } else if (flag == X5Codes.WEBVIEW_FILECHOOSER_RESULTCODE_SDK5) {
                if (openUploadMsgSdk5 != null) {
                    openUploadMsgSdk5.onReceiveValue(new Uri[]{uri});
                }
            }
            openUploadMsg = null;
            openUploadMsgSdk5 = null;
        }
    };
}
