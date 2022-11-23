package com.cloud.core.txcert.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:认证签名信息
 * Modifier:
 * ModifyContent:
 */
public class CertSignInfo {
    /**
     * 认证appid
     */
    private String certAppId = "";
    /**
     * 订单号
     */
    private String orderNo = "";
    /**
     * 签名
     */
    private String sign = "";
    /**
     * 真实姓名
     */
    private String realName = "";
    /**
     * 用户身份证号码
     */
    private String idNumber = "";
    /**
     * 随机数 32 位随机串（字母+数字组成的随机数）
     */
    private String nonce = "";
    /**
     * 签名接口版本
     */
    private String version = "";
    /**
     * 经度
     */
    private double longitude = 0;
    /**
     * 纬度
     */
    private double latitude = 0;
    /**
     * 每个用户唯一标识
     */
    private String userId = "";
    /**
     * sdk许可证
     */
    private String sdkLicense = "";

    public String getCertAppId() {
        return certAppId;
    }

    public void setCertAppId(String certAppId) {
        this.certAppId = certAppId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSdkLicense() {
        return sdkLicense;
    }

    public void setSdkLicense(String sdkLicense) {
        this.sdkLicense = sdkLicense;
    }
}
