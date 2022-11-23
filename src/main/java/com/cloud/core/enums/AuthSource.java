package com.cloud.core.enums;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/20
 * @Description:授权来源
 * @Modifier:
 * @ModifyContent:
 */
public enum AuthSource {
    /**
     * 商品预约(下单)页面
     */
    GoodsAppointmentOrder,
    /**
     * 地址管理页面
     */
    AddressManager,
    /**
     * 我的信用页面
     */
    MineCredit,
    /**
     * 商户白名单下单
     */
    MerchantWhiteListOrder,
    /**
     * 下单时京东授权认证
     */
    OrderJdAuthCert
}
