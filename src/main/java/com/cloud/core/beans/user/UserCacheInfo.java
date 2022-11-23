package com.cloud.core.beans.user;

import com.cloud.core.beans.LocationInfo;
import com.cloud.core.enums.PlatformType;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/30
 * @Description:缓存用户信息
 * @Modifier:
 * @ModifyContent:
 */
public class UserCacheInfo {

    /**
     * 用户id
     */
    private long userId = 0;
    /**
     * 是否已绑定第三方帐号
     */
    private boolean isBind = false;
    /**
     * 推送通知开关设置
     */
    private boolean isSwitch = true;
    /**
     * 用户名
     */
    private String username = "";
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
     * 性别
     */
    private String gender = "";
    /**
     * 生日
     */
    private long birthday = 0;
    /**
     * 帐号
     */
    private String account = "";
    /**
     * 头像
     */
    private String litpic = "";
    /**
     * 昵称
     */
    private String nickname = "";
    /**
     * 接口token
     */
    private String token = "";
    /**
     * 打开是否弹出dialog
     */
    private boolean isOpen = true;
    /**
     * 省id
     */
    private int privinceId = 0;
    /**
     * 市id
     */
    private int cityId = 0;
    /**
     * 0:普通用户;1:大V用户
     */
    private int vipStatus = 0;
    /**
     * 联合登录信息
     */
    private HashMap<String, String> unitedLoginInfo = new HashMap<String, String>();
    /**
     * 地址
     */
    private String address = "";
    /**
     * 地址
     */
    private String skipMessage = "";
    /**
     * 个人简介
     */
    private String personalIntro = "";
    /**
     * 当前纬度
     */
    private double currLat = 0;
    /**
     * 当前经度
     */
    private double currLng = 0;

    /**
     * 用户渠道名
     */
    private String userPlaceName = "";
    /**
     * 是否实名
     */
    private int realStatus = 0;

    /**
     * 是否设置密码 true 为设置 false 为未设置
     */
    private boolean isRegist = false;

    /**
     * 渠道
     */
    private String channel = "";
    /**
     * 渠道
     */
    private String contactName = "";
    /**
     * 渠道
     */
    private String contactPhone = "";
    /**
     * 联系人关系
     */
    private String contactRelation = "";
    /**
     * 联系人关系
     */
    private String contactRelationList = "";
    /**
     * 推荐码
     */
    private String recommendCode = "";
    /**
     * 邮箱
     */
    private String email = "";
    /**
     * 创建时间
     */
    private long createTime = 0;
    /**
     * 更新时间
     */
    private long updateTime = 0;
    /**
     * 是否填写过邀请码
     */
    private boolean invited = true;
    /**
     * 位置信息
     */
    private LocationInfo locationInfo = null;
    /**
     * 平台类型
     */
    private String platformType = PlatformType.normal.name();
    /**
     * 商户图标
     */
    private String shopLogo = "";
    /**
     * 店铺背影
     */
    private String shopBg = "";
    /**
     * 商品名称
     */
    private String shopName = "";
    /**
     * 商品租赁件数
     */
    private int rentingNo = 0;
    /**
     * 商户二维码
     */
    private String merchantGeneralizeCode = "";
    /**
     * 用户id
     */
    private int merchantId = 0;

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getSkipMessage() {
        return skipMessage;
    }

    public void setSkipMessage(String skipMessage) {
        this.skipMessage = skipMessage;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactRelation() {
        return contactRelation;
    }

    public void setContactRelation(String contactRelation) {
        this.contactRelation = contactRelation;
    }

    public String getContactRelationList() {
        return contactRelationList;
    }

    public void setContactRelationList(String contactRelationList) {
        this.contactRelationList = contactRelationList;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    /**
     * 获取市id
     */
    public int getCityId() {
        return cityId;
    }

    /**
     * 市id
     *
     * @param cityId
     */
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取省id
     */
    public int getPrivinceId() {
        return privinceId;
    }

    /**
     * 设置省id
     *
     * @param privinceId
     */
    public void setPrivinceId(int privinceId) {
        this.privinceId = privinceId;
    }

    /**
     * 获取打开是否弹出dialog
     */
    public boolean getIsOpen() {
        return isOpen;
    }

    /**
     * 设置打开是否弹出dialog
     *
     * @param isOpen
     */
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * @return 获取用户id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return 获取是否已绑定第三方帐号
     */
    public boolean getIsBind() {
        return isBind;
    }

    /**
     * 设置是否已绑定第三方帐号
     *
     * @param isBind
     */
    public void setIsBind(boolean isBind) {
        this.isBind = isBind;
    }

    /**
     * @return 获取用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return 获取真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取性别
     */
    public String getGender() {
        if (gender == null) {
            gender = "";
        }
        return gender;
    }

    /**
     * 获取推送通知开关设置
     */
    public boolean getIsSwitch() {
        return isSwitch;
    }

    /**
     * 设置推送通知开关设置
     *
     * @param isSwitch
     */
    public void setIsSwitch(boolean isSwitch) {
        this.isSwitch = isSwitch;
    }

    /**
     * 设置性别
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取生日
     */
    public long getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday
     */
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    /**
     * @return 获取手机号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置手机号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return 获取头像
     */
    public String getLitpic() {
        return litpic;
    }

    /**
     * 设置头像
     *
     * @param litpic
     */
    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    /**
     * @return 获取昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return 获取token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * 设置token
     *
     * @param =
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取联合登录信息
     *
     * @return
     */
    public HashMap<String, String> getUnitedLoginInfo() {
        return unitedLoginInfo;
    }

    /**
     * 设置联合登录信息
     *
     * @param unitedLoginInfo
     */
    public void setUnitedLoginInfo(HashMap<String, String> unitedLoginInfo) {
        this.unitedLoginInfo = unitedLoginInfo;
    }

    /**
     * 获取0:普通用户;1:大V用户
     */
    public int getVipStatus() {
        return vipStatus;
    }

    /**
     * 设置0:普通用户;1:大V用户
     *
     * @param vipStatus
     */
    public void setVipStatus(int vipStatus) {
        this.vipStatus = vipStatus;
    }

    /**
     * 获取地址
     */
    public String getAddress() {
        if (address == null) {
            address = "";
        }
        return address;
    }

    /**
     * 设置地址
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取个人简介
     */
    public String getPersonalIntro() {
        if (personalIntro == null) {
            personalIntro = "";
        }
        return personalIntro;
    }

    /**
     * 设置个人简介
     *
     * @param personalIntro
     */
    public void setPersonalIntro(String personalIntro) {
        this.personalIntro = personalIntro;
    }

    /**
     * 获取当前纬度
     */
    public double getCurrLat() {
        return currLat;
    }

    /**
     * 设置当前纬度
     *
     * @param currLat
     */
    public void setCurrLat(double currLat) {
        this.currLat = currLat;
    }

    /**
     * 获取当前经度
     */
    public double getCurrLng() {
        return currLng;
    }

    /**
     * 设置当前经度
     *
     * @param currLng
     */
    public void setCurrLng(double currLng) {
        this.currLng = currLng;
    }

    /**
     * 获取用户渠道名
     */
    public String getUserPlaceName() {
        if (userPlaceName == null) {
            userPlaceName = "";
        }
        return userPlaceName;
    }

    /**
     * 设置用户渠道名
     *
     * @param userPlaceName
     */
    public void setUserPlaceName(String userPlaceName) {
        this.userPlaceName = userPlaceName;
    }

    public int getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(int realStatus) {
        this.realStatus = realStatus;
    }

    public boolean isRegist() {
        return isRegist;
    }

    public void setRegist(boolean regist) {
        isRegist = regist;
    }

    public String getChannel() {
        if (channel == null) {
            channel = "";
        }
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecommendCode() {
        if (recommendCode == null) {
            recommendCode = "";
        }
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocationInfo getLocationInfo() {
        if (locationInfo == null) {
            locationInfo = new LocationInfo();
        }
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
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

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopBg() {
        return shopBg;
    }

    public void setShopBg(String shopBg) {
        this.shopBg = shopBg;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getRentingNo() {
        return rentingNo;
    }

    public void setRentingNo(int rentingNo) {
        this.rentingNo = rentingNo;
    }

    public String getMerchantGeneralizeCode() {
        return merchantGeneralizeCode;
    }

    public void setMerchantGeneralizeCode(String merchantGeneralizeCode) {
        this.merchantGeneralizeCode = merchantGeneralizeCode;
    }
}
