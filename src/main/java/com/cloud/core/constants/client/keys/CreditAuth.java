package com.cloud.core.constants.client.keys;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/29
 * @Description:信用授权相关key
 * @Modifier:
 * @ModifyContent:
 */
public interface CreditAuth {
    /**
     * 授权来源key(授权认证页面、商品下单页面、其它页面)
     * {@AuthSource}
     */
    public String AUTH_SOURCE_KEY = "5b5404b5ae7a473b8db2f052c18ec090";

    /**
     * 授权类型(参数)
     */
    public String authType = "authType";
}
