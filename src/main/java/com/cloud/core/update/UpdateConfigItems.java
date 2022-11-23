package com.cloud.core.update;

import com.cloud.core.configs.ConfigItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/11
 * @Description:更新配置项
 * @Modifier:
 * @ModifyContent:
 */
public class UpdateConfigItems {
    /**
     * 更新提醒
     */
    private UpdatePrompt updatePrompt = null;
    /**
     * 更新用户分隔符
     */
    private List<String> updateUsersSeparators = null;
    /**
     * app版本检测
     */
    private ConfigItem appVersionCheck = null;
    /**
     * 更新检测Activity名称
     */
    private List<String> updateCheckActivityNames = null;
    /**
     * true:选择稍候更新或不再提醒且当前网络为wifi的情况下，
     * 当检测到新版本后先下载但不安装;待下载检测时用户点击"立即更新"进行安装;
     * false:不检测;
     */
    private boolean silentDownload = false;

    public UpdatePrompt getUpdatePrompt() {
        if (updatePrompt == null) {
            updatePrompt = new UpdatePrompt();
        }
        return updatePrompt;
    }

    public void setUpdatePrompt(UpdatePrompt updatePrompt) {
        this.updatePrompt = updatePrompt;
    }

    public List<String> getUpdateUsersSeparators() {
        if (updateUsersSeparators == null) {
            updateUsersSeparators = new ArrayList<String>();
        }
        return updateUsersSeparators;
    }

    public void setUpdateUsersSeparators(List<String> updateUsersSeparators) {
        this.updateUsersSeparators = updateUsersSeparators;
    }

    public ConfigItem getAppVersionCheck() {
        if (appVersionCheck == null) {
            appVersionCheck = new ConfigItem();
        }
        return appVersionCheck;
    }

    public void setAppVersionCheck(ConfigItem appVersionCheck) {
        this.appVersionCheck = appVersionCheck;
    }

    public List<String> getUpdateCheckActivityNames() {
        if (updateCheckActivityNames == null) {
            updateCheckActivityNames = new ArrayList<String>();
        }
        return updateCheckActivityNames;
    }

    public void setUpdateCheckActivityNames(List<String> updateCheckActivityNames) {
        this.updateCheckActivityNames = updateCheckActivityNames;
    }

    public boolean isSilentDownload() {
        return silentDownload;
    }

    public void setSilentDownload(boolean silentDownload) {
        this.silentDownload = silentDownload;
    }
}
