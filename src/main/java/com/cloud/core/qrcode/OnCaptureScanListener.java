package com.cloud.core.qrcode;

import android.graphics.Bitmap;
import android.widget.RelativeLayout;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/1
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnCaptureScanListener {

    public void onBuildTopView(RelativeLayout container);

    public void onBuildBottomView(RelativeLayout container);

    public void onAnalyzeSuccess(Bitmap mBitmap, String result);

    public void onAnalyzeFailed();
}
