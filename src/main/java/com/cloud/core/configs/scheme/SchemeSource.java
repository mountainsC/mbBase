package com.cloud.core.configs.scheme;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:scheme来源
 * @Modifier:
 * @ModifyContent:
 */
public enum SchemeSource {
    /**
     * 根据版本缓存下来的scheme
     */
    localVersionScheme,
    /**
     * 根据接口获取scheme
     */
    remoteScheme
}
