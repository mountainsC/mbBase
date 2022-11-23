package com.cloud.core.oss;

import android.app.Activity;
import android.text.TextUtils;

import java.io.File;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/21
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class UploadUtils {

    private Activity activity = null;
    private int count = 0;
    private int currUploadCount = 0;
    //是否显示上传进度
    private boolean isShowUploadProgress = true;

    public UploadUtils() {
        this.count = 0;
        this.currUploadCount = 0;
    }

    protected void onUploadProgress(float progress, String uploadKey) {

    }

    protected void onUploadSuccess(int position, String url, String uploadKey, Object extra) {

    }

    protected void onUploadCompleted() {

    }

    public void setShowUploadProgress(boolean isShowUploadProgress) {
        this.isShowUploadProgress = isShowUploadProgress;
    }

    public void setCount(int count) {
        this.count = count;
        this.currUploadCount = 0;
    }

    private FileUploadUtils fileUploadUtils = new FileUploadUtils() {
        @Override
        protected void onUploadProgress(float progress, String uploadKey) {
            UploadUtils.this.onUploadProgress(progress, uploadKey);
        }

        @Override
        protected void onUploadSuccess(int position, String url, String uploadKey, Object extra) {
            currUploadCount++;
            setCurrUploadCount(currUploadCount);
            UploadUtils.this.onUploadSuccess(position, url, uploadKey, extra);
        }

        @Override
        protected void onCompleted() {
            onUploadCompleted();
        }
    };

    public void upload(Activity activity,
                       String fileName,
                       File file,
                       String uploadingText,
                       String roleFulUrl,
                       String uploadKey,
                       int position) {
        if (activity == null ||
                TextUtils.isEmpty(fileName) ||
                file == null ||
                !file.exists() ||
                TextUtils.isEmpty(roleFulUrl)) {
            return;
        }
        this.activity = activity;
        fileUploadUtils.setActivity(activity);
        fileUploadUtils.setCount(count);
        fileUploadUtils.setShowUploadProgress(isShowUploadProgress);
        fileUploadUtils.upload(fileName, file, uploadingText, roleFulUrl, uploadKey, position);
    }

    public void upload(Activity activity,
                       String fileName,
                       File file,
                       String roleFulUrl,
                       String uploadKey,
                       int position) {
        upload(activity, fileName, file, String.format("上传第%d张图片", currUploadCount + 1), roleFulUrl, uploadKey, position);
    }
}
