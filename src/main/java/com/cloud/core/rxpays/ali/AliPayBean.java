package com.cloud.core.rxpays.ali;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/24
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class AliPayBean {
    private String outTradeNo;
    private String subject;
    private String description;
    private String totalAmount;
    private String sign;
    private String paySigned;
    private String signType;
    private String appId;
    private String sellerId;

    public AliPayBean() {
    }

    public AliPayBean(String outTradeNo, String subject, String description, String totalAmount, String sign, String paySigned, String signType, String appId, String sellerId) {
        this.outTradeNo = outTradeNo;
        this.subject = subject;
        this.description = description;
        this.totalAmount = totalAmount;
        this.sign = sign;
        this.paySigned = paySigned;
        this.signType = signType;
        this.appId = appId;
        this.sellerId = sellerId;
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPaySigned() {
        return this.paySigned;
    }

    public void setPaySigned(String paySigned) {
        this.paySigned = paySigned;
    }

    public String getSignType() {
        return this.signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
