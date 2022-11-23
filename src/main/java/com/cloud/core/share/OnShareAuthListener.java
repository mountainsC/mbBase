package com.cloud.core.share;

import com.cloud.core.enums.ShareAuthType;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/27
 * @Description:授权监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnShareAuthListener {

    /**
     * 授权成功
     *
     * @param platform           平台类型
     * @param action             action
     * @param data               回调数据
     */
    public void onAuthSuccessful(SHARE_MEDIA platform, int action, Map<String, String> data, ShareAuthType authType);
}
