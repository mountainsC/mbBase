package com.cloud.core.update;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/11
 * @Description:更新提醒
 * @Modifier:
 * @ModifyContent:
 */
public class UpdatePrompt {
    /**
     * 提醒类型(即弹窗类型):NORMAL
     * NORMAL:在可检测版本的页面基础上，每次打开页面都会提醒;
     * No_LONGER_DISPLAY:在可检测版本的页面基础上，根据设定的时间间隔来显示
     */
    private String type = "NORMAL";
    /**
     * 更新检测的时间间隔,单位秒；当type=No_LONGER_DISPLAY时生效;若type为NORMAL时此属性值不生效;
     */
    private int timeInterval = 0;
    /**
     * 检测loading提示文本
     */
    private String checkLoading = "检测新版本...";
    /**
     * 无网络更新提醒
     */
    private String noNetworkPrompt = "没有可用的网络,请连接后再更新";
    /**
     * 下载应用地址异常提醒
     */
    private String apkAddressErrorPrompt = "下载应用地址异常";
    /**
     * 最新版本提醒
     */
    private String lastVersionPrompt = "已经是最新版本了";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getCheckLoading() {
        return checkLoading;
    }

    public void setCheckLoading(String checkLoading) {
        this.checkLoading = checkLoading;
    }

    public String getNoNetworkPrompt() {
        return noNetworkPrompt;
    }

    public void setNoNetworkPrompt(String noNetworkPrompt) {
        this.noNetworkPrompt = noNetworkPrompt;
    }

    public String getApkAddressErrorPrompt() {
        return apkAddressErrorPrompt;
    }

    public void setApkAddressErrorPrompt(String apkAddressErrorPrompt) {
        this.apkAddressErrorPrompt = apkAddressErrorPrompt;
    }

    public String getLastVersionPrompt() {
        return lastVersionPrompt;
    }

    public void setLastVersionPrompt(String lastVersionPrompt) {
        this.lastVersionPrompt = lastVersionPrompt;
    }
}
