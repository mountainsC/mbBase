package com.cloud.core.beans;


import com.cloud.core.beans.user.UserContactItem;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/24
 * @Description:t选择地址
 * @Modifier:
 * @ModifyContent:
 */
public class SelectAddressItem {
    /**
     * 发送/接收key
     */
    private String key = "";
    /**
     * 联系人信息
     */
    private UserContactItem contactItem = null;
    /**
     * 商户信息
     */
    private ShopInfoItem shopInfoItem = null;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserContactItem getContactItem() {
        return contactItem;
    }

    public void setContactItem(UserContactItem contactItem) {
        this.contactItem = contactItem;
    }

    public ShopInfoItem getShopInfoItem() {
        return shopInfoItem;
    }

    public void setShopInfoItem(ShopInfoItem shopInfoItem) {
        this.shopInfoItem = shopInfoItem;
    }
}
