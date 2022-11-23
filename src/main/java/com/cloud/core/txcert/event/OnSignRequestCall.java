package com.cloud.core.txcert.event;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:签名请求回调
 * Modifier:
 * ModifyContent:
 */
public interface OnSignRequestCall {
    /**
     * 签名请求
     *
     * @param txCertAppId 认证appid
     * @param realName    真实姓名
     * @param idNumber    身份证号
     */
    public void onSignRequest(String txCertAppId, String realName, String idNumber);
}
