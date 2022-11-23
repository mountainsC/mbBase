package com.cloud.core.utils;

import android.app.Activity;

import com.cloud.core.share.BaseShareAuth;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/11/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */

public class AuthUtils extends BaseShareAuth {

    public void auth(Activity activity, SHARE_MEDIA platform) {
        super.auth(activity, platform);
    }

    public void deleteAuthInfo(Activity activity, SHARE_MEDIA platform) {
        super.deleteAuthInfo(activity, platform);
    }

    public void getAuthInfo(Activity activity, SHARE_MEDIA platform) {
        super.getAuthInfo(activity, platform);
    }

    public void getPlatformInfo(Activity activity, SHARE_MEDIA platform) {
        super.getPlatformInfo(activity, platform);
    }
}
