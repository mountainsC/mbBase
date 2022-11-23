package com.cloud.core.update;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/12
 * @Description:版本更新监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnVersionUpdateListener {
    /**
     * 有版本更新
     *
     * @param isCompulsoryUpdate 是否强制更新
     */
    public void hasVersion(UpdateInfo updateInfo, boolean isCompulsoryUpdate);
}
