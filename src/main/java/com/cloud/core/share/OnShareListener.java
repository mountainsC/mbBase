package com.cloud.core.share;

import com.cloud.core.enums.ShareCallType;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/27
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnShareListener {
    public void onShareSuccessful(SHARE_MEDIA platform, ShareCallType shareCallType);

    public void onError(SHARE_MEDIA platform, Throwable throwable);
}
