package com.cloud.core.configs;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/17
 * @Description:名称配置
 * @Modifier:
 * @ModifyContent:
 */
public class RxNames {
    /**
     * 基础框架配置文件名
     */
    private String coreConfigFileName = "";
    /**
     * 版本更新配置文件名
     */
    private String verUpdateConfigFileName = "";
    /**
     * 行为事件统计配置文件名
     */
    private String behaviorStatConfigFileName = "";
    /**
     * toast配置文件名
     */
    private String toastConfigFileName = "";
    /**
     * messageBox配置文件名
     */
    private String messageBoxConfigFileName = "";

    public RxNames() {

    }

    public String getCoreConfigFileName() {
        return coreConfigFileName;
    }

    public RxNames setCoreConfigFileName(String coreConfigFileName) {
        this.coreConfigFileName = coreConfigFileName;
        return this;
    }

    public String getVerUpdateConfigFileName() {
        return verUpdateConfigFileName;
    }

    public RxNames setVerUpdateConfigFileName(String verUpdateConfigFileName) {
        this.verUpdateConfigFileName = verUpdateConfigFileName;
        return this;
    }

    public String getBehaviorStatConfigFileName() {
        return behaviorStatConfigFileName;
    }

    public RxNames setBehaviorStatConfigFileName(String behaviorStatConfigFileName) {
        this.behaviorStatConfigFileName = behaviorStatConfigFileName;
        return this;
    }

    public String getToastConfigFileName() {
        return toastConfigFileName;
    }

    public RxNames setToastConfigFileName(String toastConfigFileName) {
        this.toastConfigFileName = toastConfigFileName;
        return this;
    }

    public String getMessageBoxConfigFileName() {
        return messageBoxConfigFileName;
    }

    public RxNames setMessageBoxConfigFileName(String messageBoxConfigFileName) {
        this.messageBoxConfigFileName = messageBoxConfigFileName;
        return this;
    }
}
