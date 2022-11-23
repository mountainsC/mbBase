package com.cloud.core.share;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/14
 * Description:分享成功回调
 * Modifier:
 * ModifyContent:
 */
public interface OnShareSuccessCall {
    //分享成功
    public void onShareSuccess(SHARE_MEDIA platform);
}
