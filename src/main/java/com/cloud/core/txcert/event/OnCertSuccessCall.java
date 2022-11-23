package com.cloud.core.txcert.event;


import com.cloud.core.txcert.beans.CertResult;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:认证成功回调
 * Modifier:
 * ModifyContent:
 */
public interface OnCertSuccessCall {
    /**
     * 认证成功
     *
     * @param result 认证信息
     */
    public void onCertSuccess(CertResult result);
}
