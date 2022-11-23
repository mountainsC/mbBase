package com.cloud.core.update;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/28
 * @Description:下载方式
 * @Modifier:
 * @ModifyContent:
 */
public enum DownType {
    Notify(2),
    Dialog(1);

    private int value = 0;

    private DownType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
