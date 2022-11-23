package com.cloud.core.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.cloud.core.beans.ShareContent;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/1/21
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseShareUtils {
    private Activity activity = null;
    protected OnUmShareListener onUmShareListener;

    enum RuleParams {
        /**
         * url或file:///本地文件路径验证
         */
        Url("^(http|https|file|rtsp|mms)://[/]?(([\\w-]+\\.)+)?[\\w-]+(:[0-9]{2,})*(/[\\w-./?%&=,@!~`#$%^&*,./_+|!:,.;]*)?$");
        private String value = "";

        RuleParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private boolean valid(String pattern, String input) {
        boolean flag = Pattern.matches(pattern, TextUtils.isEmpty(input) ? "" : input);
        return flag;
    }

    public void share(Activity activity, ShareContent shareContent) {
        try {
            this.activity = activity;
            SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                    {
                            SHARE_MEDIA.WEIXIN,
                            SHARE_MEDIA.WEIXIN_CIRCLE,
                            SHARE_MEDIA.QQ,
                            SHARE_MEDIA.QZONE
                    };
            new ShareAction(activity)
                    .setDisplayList(displaylist)
                    .withMedia(getUMWeb(shareContent))
                    .withText(shareContent.getContent())
                    .setCallback(umShareListener)
                    .open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享网页链接
     */
    public void shareWeb(Activity activity, ShareContent shareContent, SHARE_MEDIA platform) {
        try {
            this.activity = activity;
            new ShareAction(activity)
                    .setPlatform(platform)
                    .withMedia(getUMWeb(shareContent))
                    .withText(shareContent.getContent())
                    .setCallback(umShareListener)
                    .share();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享图片
     * 如果 file 为 null，则为网络图片
     */
    public void shareImg(Activity activity, String url, File file, SHARE_MEDIA platform) {
        try {
            this.activity = activity;
            UMImage umImage;
            if (file != null) {
                umImage = new UMImage(activity, file);
            } else {
                umImage = new UMImage(activity, url);
            }


            umImage.compressStyle = UMImage.CompressStyle.SCALE;
            new ShareAction(activity)
                    .withText("img")
                    .withMedia(umImage)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UMWeb getUMWeb(ShareContent shareContent) {
        UMWeb umWeb = new UMWeb(shareContent.getUrl());
        umWeb.setTitle(shareContent.getTitle());
        umWeb.setDescription(shareContent.getContent());
        UMImage umImage = null;
        if (valid(RuleParams.Url.getValue(), shareContent.getLogo())) {
            umImage = new UMImage(activity, shareContent.getLogo());
        }
        umWeb.setThumb(umImage);
        return umWeb;
    }

    /**
     * 分享文件至微信
     *
     * @param activity     上下文
     * @param shareContent 分享信息
     */
    public void shareFileToWx(Activity activity, ShareContent shareContent) {
        this.activity = activity;
        //文件不存在取消分享
        File file = shareContent.getFile();
        if (file == null) {
            return;
        }
        WXFileObject fileObject = new WXFileObject();
        fileObject.filePath = file.getAbsolutePath();

        WXMediaMessage mediaMessage = new WXMediaMessage(fileObject);
        mediaMessage.title = file.getName();
        mediaMessage.description = shareContent.getContent();
        mediaMessage.setThumbImage(shareContent.getFileThumImage());

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, shareContent.getWxAppId(), false);
        wxapi.sendReq(req);
    }

    public interface OnUmShareListener {
        void onResult(SHARE_MEDIA platform);
    }

    public void setOnUmShareListener(OnUmShareListener onUmShareListener) {
        this.onUmShareListener = onUmShareListener;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (onUmShareListener != null) {
                onUmShareListener.onResult(platform);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };
}
