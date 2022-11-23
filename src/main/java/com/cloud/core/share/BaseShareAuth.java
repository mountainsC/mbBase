package com.cloud.core.share;

import android.app.Activity;
import android.text.TextUtils;

import com.cloud.core.enums.ShareAuthType;
import com.cloud.core.enums.ShareCallType;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/27
 * @Description:UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);必须在具体的Activity对应的onActivityResult回调
 */
public class BaseShareAuth {

    private OnShareAuthListener onShareAuthListener = null;
    private OnShareDeleteAuthListener onShareDeleteAuthListener = null;
    private OnShareListener onShareListener = null;
    private ShareBoardlistener shareBoardlistener = null;

    public void setOnShareAuthListener(OnShareAuthListener listener) {
        this.onShareAuthListener = listener;
    }

    public void setOnShareDeleteAuthListener(OnShareDeleteAuthListener listener) {
        this.onShareDeleteAuthListener = listener;
    }

    public void setOnShareListener(OnShareListener listener) {
        this.onShareListener = listener;
    }

    public void setShareBoardlistener(ShareBoardlistener listener) {
        this.shareBoardlistener = listener;
    }

    /**
     * 分享授权初始化
     *
     * @param activity
     * @param platform
     */
    protected void auth(Activity activity, SHARE_MEDIA platform) {
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        if (shareAPI.isAuthorize(activity, platform)) {
            shareAPI.getPlatformInfo(activity, platform, umAuthListener);
        } else {
            shareAPI.doOauthVerify(activity, platform, umAuthListener);
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (onShareAuthListener != null) {
                onShareAuthListener.onAuthSuccessful(platform, action, data, ShareAuthType.Auth);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    /**
     * 删除对应授权信息
     *
     * @param activity
     * @param platform
     */
    protected void deleteAuthInfo(Activity activity, SHARE_MEDIA platform) {
        if (activity == null) {
            return;
        }
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        shareAPI.deleteOauth(activity, platform, umdelAuthListener);
    }

    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (onShareDeleteAuthListener != null) {
                onShareDeleteAuthListener.onDeleteAuthSuccessful(platform, action, data);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    /**
     * 获取授权信息
     *
     * @param activity
     * @param platform
     */
    protected void getAuthInfo(Activity activity, SHARE_MEDIA platform) {
        if (activity == null) {
            return;
        }
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        shareAPI.getPlatformInfo(activity, platform, getAuthListener);
    }

    private UMAuthListener getAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (onShareAuthListener != null) {
                onShareAuthListener.onAuthSuccessful(platform, action, data, ShareAuthType.GetAuthInfo);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    /**
     * 是否开户分享编辑
     *
     * @param flag true:开户;false:关闭;
     */
    public void isOpenShareEditor(boolean flag) {
        Config.OpenEditor = flag;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (TextUtils.equals(platform.name(), "WEIXIN_FAVORITE")) {
                if (onShareListener != null) {
                    onShareListener.onShareSuccessful(platform, ShareCallType.Favorite);
                }
            } else {
                if (onShareListener != null) {
                    onShareListener.onShareSuccessful(platform, ShareCallType.Share);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (onShareListener != null) {
                onShareListener.onError(platform, t);
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };

    public void shareInstance(Activity activity, ShareBean shareBean, SHARE_MEDIA... share_medias) {
        ShareAction shareAction = new ShareAction(activity);
        shareAction.setDisplayList(share_medias);
        shareAction.withText(shareBean.getSourceTitle());
        if (shareBean.getImage() != null) {
            shareAction.withMedia(shareBean.getImage());
        }
        if (shareBean.getMusic() != null) {
            shareAction.withMedia(shareBean.getMusic());
        }
        if (shareBean.getVideo() != null) {
            shareAction.withMedia(shareBean.getVideo());
        }
        //shareAction.setCallback(umShareListener);
        if (shareBoardlistener != null) {
            shareAction.setShareboardclickCallback(shareBoardlistener);
        }
        shareAction.open();
    }

    protected void getPlatformInfo(Activity activity, SHARE_MEDIA platform) {
        UMShareAPI shareAPI = UMShareAPI.get(activity);
        shareAPI.getPlatformInfo(activity, platform, umplatforminfoListener);
    }

    private UMAuthListener umplatforminfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (onShareAuthListener != null) {
                onShareAuthListener.onAuthSuccessful(platform, action, data, ShareAuthType.GetPlatformInfo);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };
}
