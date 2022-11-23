package com.cloud.core.txcert.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/8
 * Description:认证结果信息
 * Modifier:
 * ModifyContent:
 */
public class CertResult {
    /**
     * 活体检测分数
     */
    private String liveRate = "";
    /**
     * 人脸对比分数
     */
    private String similarity = "";
    /**
     * 用户头像
     */
    private String userImage = "";
    /**
     * 认证签名信息
     */
    private CertSignInfo certSignInfo = null;

    public String getLiveRate() {
        return liveRate;
    }

    public void setLiveRate(String liveRate) {
        this.liveRate = liveRate;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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
