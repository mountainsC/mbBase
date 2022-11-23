package com.cloud.core.beans.user;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/9/25
 * @Description:用户联系信息数据项
 * @Modifier:
 * @ModifyContent:
 */
public class UserContactItem {

    /**
     * id
     */
    private int id = 0;
    /**
     * 用户id
     */
    private long userId = 0;
    /**
     * 联系人名称
     */
    private String name = "";
    /**
     * 性
     */
    private String firstName = "";
    /**
     * 名
     */
    private String lastName = "";
    /**
     * 电话号码
     */
    private String phone = "";
    /**
     * 国籍
     */
    private String country = "";
    /**
     * 省
     */
    private String provice = "";
    /**
     * 市
     */
    private String city = "";
    /**
     * 区
     */
    private String regoin = "";
    /**
     * 详细地址
     */
    private String text = "";
    /**
     * true:默认地址
     */
    private boolean defaulted = false;
    /**
     * 创建时间
     */
    private long createTime = 0;
    /**
     * 更新时间
     */
    private long updateTime = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegoin() {
        return regoin;
    }

    public void setRegoin(String regoin) {
        this.regoin = regoin;
    }

    public boolean isDefaulted() {
        return defaulted;
    }

    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
