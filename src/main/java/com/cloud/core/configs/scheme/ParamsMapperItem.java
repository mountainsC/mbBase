package com.cloud.core.configs.scheme;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/25
 * @Description:参数映射项
 * @Modifier:
 * @ModifyContent:
 */
public class ParamsMapperItem {
    /**
     * scheme参数名
     */
    private String key = "";
    /**
     * 业务参数名
     */
    private String value = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
