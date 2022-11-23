package com.cloud.core.beans.user;

import com.cloud.core.beans.BaseDataBean;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/9/23
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class UserInfoBean extends BaseDataBean<UserInfoBean> {
    /**
     * id
     */
    private long id = 0;
    /**
     * 昵称
     */
    private String name = "";
    /**
     * 真实姓名
     */
    private String realName = "";
    /**
     * 身份证号加*
     */
    private String certNo = "";
    /**
     * 公司名称
     */
    private String workplace = "";
    /**
     * 公司电话
     */
    private String companyPhone = "";
    /**
     * 职业身份中文
     */
    private String occupationalIdentityTypeStr = "";
    /**
     * 手机号
     */
    private String phone = "";
    /**
     * 渠道
     */
    private String channel = "";
    /**
     * 推荐码
     */
    private String recommendCode = "";
    /**
     * 性别
     */
    private String sex = "";
    /**
     * 生日
     */
    private long birthday = 0;
    /**
     * 邮箱
     */
    private String email = "";
    /**
     * 头像
     */
    private String headImageUrl = "";
    /**
     * 创建时间
     */
    private long createTime = 0;
    /**
     * 更新时间
     */
    private long updateTime = 0;
    /**
     * 认证状态
     * 0：未认证；
     * 1：认证成功；
     * 2：上传身份证照片;
     * 3：待完善公司信息；
     */
    private long creditStatus = 0;
    /**
     * 是否填写了邀请码
     */
    private boolean invited = true;
    /**
     * 支付宝授权状态（false:未授权；true:授权）
     */
    private boolean alipayAuthorizationStatus = false;
    /**
     * 支付宝授权跳转地址
     */
    private String alipayAuthorizationUrl = "";
    /**
     * 芝麻分
     */
    private String zhimaScore = "";
    /**
     * 京东授权状态(false:未授权；true:授权)
     */
    private boolean jdAuthorizationStatus = false;
    /**
     * 京东授权跳转地址
     */
    private String jdAuthorizationUrl = "";
    /**
     * 京东小白分
     */
    private String jdScore = "";
    /**
     * 微信是否绑定成功
     */
    private boolean weChatBindStatus = false;
    /**
     * 支付宝是否绑定成功
     */
    private boolean aliPayBindStatus = false;
    /**
     * 0-未设置;1-已设置;
     */
    private int regist = 0;
    /**
     * 是否已认证(true已实名认证,false未认证)
     */
    private boolean authenticated = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getOccupationalIdentityTypeStr() {
        return occupationalIdentityTypeStr;
    }

    public void setOccupationalIdentityTypeStr(String occupationalIdentityTypeStr) {
        this.occupationalIdentityTypeStr = occupationalIdentityTypeStr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecommendCode() {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(long creditStatus) {
        this.creditStatus = creditStatus;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public boolean isAlipayAuthorizationStatus() {
        return alipayAuthorizationStatus;
    }

    public void setAlipayAuthorizationStatus(boolean alipayAuthorizationStatus) {
        this.alipayAuthorizationStatus = alipayAuthorizationStatus;
    }

    public String getAlipayAuthorizationUrl() {
        return alipayAuthorizationUrl;
    }

    public void setAlipayAuthorizationUrl(String alipayAuthorizationUrl) {
        this.alipayAuthorizationUrl = alipayAuthorizationUrl;
    }

    public String getZhimaScore() {
        return zhimaScore;
    }

    public void setZhimaScore(String zhimaScore) {
        this.zhimaScore = zhimaScore;
    }

    public boolean isJdAuthorizationStatus() {
        return jdAuthorizationStatus;
    }

    public void setJdAuthorizationStatus(boolean jdAuthorizationStatus) {
        this.jdAuthorizationStatus = jdAuthorizationStatus;
    }

    public String getJdAuthorizationUrl() {
        return jdAuthorizationUrl;
    }

    public void setJdAuthorizationUrl(String jdAuthorizationUrl) {
        this.jdAuthorizationUrl = jdAuthorizationUrl;
    }

    public String getJdScore() {
        return jdScore;
    }

    public void setJdScore(String jdScore) {
        this.jdScore = jdScore;
    }

    public boolean isWeChatBindStatus() {
        return weChatBindStatus;
    }

    public void setWeChatBindStatus(boolean weChatBindStatus) {
        this.weChatBindStatus = weChatBindStatus;
    }

    public boolean isAliPayBindStatus() {
        return aliPayBindStatus;
    }

    public void setAliPayBindStatus(boolean aliPayBindStatus) {
        this.aliPayBindStatus = aliPayBindStatus;
    }

    public int getRegist() {
        return regist;
    }

    public void setRegist(int regist) {
        this.regist = regist;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
