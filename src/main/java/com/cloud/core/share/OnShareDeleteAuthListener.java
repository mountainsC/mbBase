package com.cloud.core.share;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/27
 * @Description:删除授权监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnShareDeleteAuthListener {
    public void onDeleteAuthSuccessful(SHARE_MEDIA platform, int action, Map<String, String> data);
}
