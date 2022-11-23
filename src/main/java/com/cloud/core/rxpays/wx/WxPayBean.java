package com.cloud.core.rxpays.wx;

/**
 * @Author Gs
 * @Email:gs_12@foxmail.com
 * @CreateTime:2017/3/14
 * @Description: 微信支付Bean
 * @Modifier:
 * @ModifyContent:
 */

public class WxPayBean {

    private String appid;
    private String partnerid;
    private String prepayid;
    private String packageValue;
    private String noncestr;
    private String timestamp;
    private String sign;

    public WxPayBean() {
    }

    public WxPayBean(String appid, String partnerid, String prepayid, String packageValue, String noncestr, String timestamp, String sign) {
        this.appid = appid;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.packageValue = packageValue;
        this.noncestr = noncestr;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
