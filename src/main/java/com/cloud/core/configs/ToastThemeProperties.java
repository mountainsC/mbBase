package com.cloud.core.configs;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/18
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ToastThemeProperties {
    /**
     * 背影
     */
    private ConfigItem backgroundResource = null;
    /**
     * 文本颜色
     */
    private String textColor = "";
    /**
     * 文本大小
     */
    private int textSize = 0;

    public ConfigItem getBackgroundResource() {
        if (backgroundResource == null) {
            backgroundResource = new ConfigItem();
        }
        return backgroundResource;
    }

    public void setBackgroundResource(ConfigItem backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
