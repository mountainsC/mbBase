package com.cloud.core.txcert.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:ocr认证结果
 * Modifier:
 * ModifyContent:
 */
public class OcrCertResult {
    /**
     * 拉起SDK的模式所对应的int 值，也就是startActivityForOcr 方法中WBOCRTYPEMODE type的枚举值value
     */
    private int type;
    /**
     * 身份证号码
     */
    private String cardNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 住址
     */
    private String address;
    /**
     * 民族
     */
    private String nation;
    /**
     * 出生年月日
     */
    private String birth;
    /**
     * 人像面图片存放路径
     */
    private String frontFullImageSrc;
    /**
     * 人像面告警码
     */
    private String frontWarning;
    /**
     * 签发机关
     */
    private String office;
    /**
     * 有效期限
     */
    private String validDate;
    /**
     * 国徽面图片存放路径
     */
    private String backFullImageSrc;
    /**
     * 国徽面告警码
     */
    private String backWarning;
    /**
     * 签名
     */
    private String sign;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 识别的唯一标识
     */
    private String ocrId;
    /**
     * 认证签名信息
     */
    private CertSignInfo certSignInfo = null;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFrontFullImageSrc() {
        return frontFullImageSrc;
    }

    public void setFrontFullImageSrc(String frontFullImageSrc) {
        this.frontFullImageSrc = frontFullImageSrc;
    }

    public String getFrontWarning() {
        return frontWarning;
    }

    public void setFrontWarning(String frontWarning) {
        this.frontWarning = frontWarning;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getBackFullImageSrc() {
        return backFullImageSrc;
    }

    public void setBackFullImageSrc(String backFullImageSrc) {
        this.backFullImageSrc = backFullImageSrc;
    }

    public String getBackWarning() {
        return backWarning;
    }

    public void setBackWarning(String backWarning) {
        this.backWarning = backWarning;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOcrId() {
        return ocrId;
    }

    public void setOcrId(String ocrId) {
        this.ocrId = ocrId;
    }

    public CertSignInfo getCertSignInfo() {
        if (certSignInfo == null) {
            certSignInfo = new CertSignInfo();
        }
        return certSignInfo;
    }

    public void setCertSignInfo(CertSignInfo certSignInfo) {
        this.certSignInfo = certSignInfo;
    }
}
