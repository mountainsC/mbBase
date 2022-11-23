package com.cloud.core.configs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/5
 * @Description:框架配置项
 * @Modifier:
 * @ModifyContent:
 */
public class RxCoreConfigItems {
    /**
     * 默认为Alibaba
     * Alibaba:图片地址后面带阿里云规则
     * Qiniu:图片地址后面带七牛规则
     */
    private String imagePlatformType = "ALIBABA";
    /**
     * api配置项
     */
    private ApiConfig apiConfigs = null;
    /**
     * 网络广播action
     */
    private String receiveNetworkAction = "";
    /**
     * 默认背影图片
     */
    private ConfigItem defaultBackgroundImage = null;
    /**
     * app icon
     */
    private ConfigItem appIcon = null;
    /**
     * 网络状态提醒需要过滤的Activity名称
     */
    private List<String> netStateRemindFilterActivityNames = null;
    /**
     * 主题颜色
     */
    private String themeColor = "";
    /**
     * token
     */
    private ConfigItem token = null;
    /**
     * 基础地址
     */
    private BasicUrlItem basicUrls = null;

    public String getImagePlatformType() {
        return imagePlatformType;
    }

    public void setImagePlatformType(String imagePlatformType) {
        this.imagePlatformType = imagePlatformType;
    }

    public ApiConfig getApiConfigs() {
        if (apiConfigs == null) {
            apiConfigs = new ApiConfig();
        }
        return apiConfigs;
    }

    public void setApiConfigs(ApiConfig apiConfigs) {
        this.apiConfigs = apiConfigs;
    }

    public String getReceiveNetworkAction() {
        return receiveNetworkAction;
    }

    public void setReceiveNetworkAction(String receiveNetworkAction) {
        this.receiveNetworkAction = receiveNetworkAction;
    }

    public ConfigItem getDefaultBackgroundImage() {
        if (defaultBackgroundImage == null) {
            defaultBackgroundImage = new ConfigItem();
        }
        return defaultBackgroundImage;
    }

    public void setDefaultBackgroundImage(ConfigItem defaultBackgroundImage) {
        this.defaultBackgroundImage = defaultBackgroundImage;
    }

    public ConfigItem getAppIcon() {
        if (appIcon == null) {
            appIcon = new ConfigItem();
        }
        return appIcon;
    }

    public void setAppIcon(ConfigItem appIcon) {
        this.appIcon = appIcon;
    }

    public List<String> getNetStateRemindFilterActivityNames() {
        if (netStateRemindFilterActivityNames == null) {
            netStateRemindFilterActivityNames = new ArrayList<String>();
        }
        return netStateRemindFilterActivityNames;
    }

    public void setNetStateRemindFilterActivityNames(List<String> netStateRemindFilterActivityNames) {
        this.netStateRemindFilterActivityNames = netStateRemindFilterActivityNames;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public ConfigItem getToken() {
        if (token == null) {
            token = new ConfigItem();
        }
        return token;
    }

    public void setToken(ConfigItem token) {
        this.token = token;
    }

    public BasicUrlItem getBasicUrls() {
        if (basicUrls == null) {
            basicUrls = new BasicUrlItem();
        }
        return basicUrls;
    }

    public void setBasicUrls(BasicUrlItem basicUrls) {
        this.basicUrls = basicUrls;
    }
}
