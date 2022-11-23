package com.cloud.core.beans;


/**
 * @Author: chenghailei
 * @Email: maplelucy1991@163.com
 * @CreateTime: 17/12/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */

public class ShopInfoItem extends BaseDataBean<ShopInfoItem> {

    /**
     * id : 5
     * shopAddress : 宝龙城
     * shopLogo : /merchant/20170905/f88482476a6544de90c3ea84b836b3db.png
     * shopName : 汉堡王
     * shopPhone : 110
     */

    private int id;

    private String shopAddress;
    private String shopLogo;
    private String shopName;
    private String shopPhone;
    /**
     * 店铺背景
     */
    private String backgroundPicture;
    /**
     * 在租商品
     */
    private int goodsNumberOnShelf;
    /**
     * ...
     */
    private String rongCloudUserId;
    /**
     * 记录是否已经选择门店自提
     */
    private boolean isSelect = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getBackgroundPicture() {
        return backgroundPicture;
    }

    public void setBackgroundPicture(String backgroundPicture) {
        this.backgroundPicture = backgroundPicture;
    }

    public int getGoodsNumberOnShelf() {
        return goodsNumberOnShelf;
    }

    public void setGoodsNumberOnShelf(int goodsNumberOnShelf) {
        this.goodsNumberOnShelf = goodsNumberOnShelf;
    }

    public String getRongCloudUserId() {
        return rongCloudUserId;
    }

    public void setRongCloudUserId(String rongCloudUserId) {
        this.rongCloudUserId = rongCloudUserId;
    }
}
