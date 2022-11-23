package com.cloud.core.configs.scheme;

import android.net.Uri;

import com.cloud.core.configs.scheme.jumps.GoConfigItem;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/10
 * @Description:scheme配置监听属性
 * @Modifier:
 * @ModifyContent:
 */
public class SchemeConfigProperty {

    /**
     * 跳转配置项
     */
    private GoConfigItem configItem = null;

    /**
     * 目标路径
     */
    private Uri data = null;

    /**
     * scheme路径(scheme key值)
     */
    private String schemePath = "";

    public GoConfigItem getConfigItem() {
        return configItem;
    }

    public void setConfigItem(GoConfigItem configItem) {
        this.configItem = configItem;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getSchemePath() {
        return schemePath;
    }

    public void setSchemePath(String schemePath) {
        this.schemePath = schemePath;
    }
}
