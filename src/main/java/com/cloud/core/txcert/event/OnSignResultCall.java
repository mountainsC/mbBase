package com.cloud.core.txcert.event;

import android.content.Context;

import com.cloud.core.txcert.beans.CertSignInfo;


/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:签名结果回调
 * Modifier:
 * ModifyContent:
 */
public interface OnSignResultCall {

    /**
     * 签名结果
     */
    public void onSignResult(CertSignInfo certSignInfo);
}
