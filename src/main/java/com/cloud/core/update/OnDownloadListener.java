package com.cloud.core.update;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/11
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDownloadListener {
    /**
     * 下载前处理
     */
    public void onPreDownload();

    /**
     * 进度回调
     *
     * @param progress 下载进度
     * @param downType
     */
    public void onDownloadProgress(double progress, DownType downType);
}
