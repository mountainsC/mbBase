package com.cloud.core.events;


import com.cloud.core.enums.DataProviderType;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/19
 * @Description:模块数据提供者监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnModDataProviderListener<T> {

    /**
     * 模块数据提供(本地数据将缓存在{RxCachePool}中,使用时可根据)
     *
     * @param providerType 根据类型缓存本地数据
     * @param t            泛型数据
     */
    public void onModDataProvider(DataProviderType providerType, T t);
}
