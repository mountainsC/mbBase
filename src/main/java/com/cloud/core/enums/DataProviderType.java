package com.cloud.core.enums;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/19
 * @Description:数据提供类型
 * @Modifier:
 * @ModifyContent:
 */
public enum DataProviderType {
    /**
     * 用户信息(对应接口users/personalInformation)
     */
    userInfo("b311b2ee0efe4e9986b943479ae3fc00"),
    /**
     * 新增地址状态(当状态=true，使用时即可获取)
     */
    addAddressStatus("4edf0964eedf44ef99e5c7b281ba5c1a"),
    /**
     * 编辑地址状态(当状态=true，使用时即可获取)
     */
    editAddressStatus("b15e65c7896c4b57862ee7fea5d7cd3e");

    private String cacheKey = "";

    private DataProviderType(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }
}
