package com.cloud.core.configs;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/28
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BasicUrlItem {
    /**
     * 图片基础地址
     */
    private ConfigItem img = null;

    public ConfigItem getImg() {
        if (img == null) {
            img = new ConfigItem();
        }
        return img;
    }

    public void setImg(ConfigItem img) {
        this.img = img;
    }
}
