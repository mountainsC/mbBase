package com.cloud.core.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:重定向配置数据
 * @Modifier:
 * @ModifyContent:
 */
public class RedirectionConfigItem extends BaseDataBean<RedirectionConfigItem> {
    /**
     * 数据id
     */
    private int id = 0;
    /**
     * 设备类型android;ios;
     */
    private String deviceType = "";
    /**
     * 配置的json字符串
     */
    private String jsonConfig = "";
    /**
     * 版本编号
     */
    private int versionCode = 0;
    /**
     * scheme url完整路径
     */
    private String schemeUrl = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(String jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSchemeUrl() {
        return schemeUrl;
    }

    public void setSchemeUrl(String schemeUrl) {
        this.schemeUrl = schemeUrl;
    }
}
