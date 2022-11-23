package com.cloud.core.txcert.event;


import com.cloud.core.txcert.beans.OcrCertResult;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:ocr认证成功回调
 * Modifier:
 * ModifyContent:
 */
public interface OnOcrCertSuccessCall {
    /**
     * ocr认证成功
     *
     * @param result ocr认证结果
     */
    public void onOcrCertSuccess(OcrCertResult result);
}
