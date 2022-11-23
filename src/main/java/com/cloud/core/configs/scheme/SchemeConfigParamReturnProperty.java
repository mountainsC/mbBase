package com.cloud.core.configs.scheme;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:scheme配置参数返回属性
 * @Modifier:
 * @ModifyContent:
 */
public class SchemeConfigParamReturnProperty {
    /**
     * scheme来源
     */
    private SchemeSource schemeSource = null;
    /**
     * scheme配置项
     */
    private SchemeItem scheme = null;

    public SchemeSource getSchemeSource() {
        return schemeSource;
    }

    public void setSchemeSource(SchemeSource schemeSource) {
        this.schemeSource = schemeSource;
    }

    public SchemeItem getScheme() {
        return scheme;
    }

    public void setScheme(SchemeItem scheme) {
        this.scheme = scheme;
    }
}
