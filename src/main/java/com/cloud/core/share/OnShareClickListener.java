package com.cloud.core.share;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/14
 * Description:分享事件处发
 * Modifier:
 * ModifyContent:
 */
public interface OnShareClickListener {
    //分享事件
    public void onShareClick(SHARE_MEDIA platform);
}
