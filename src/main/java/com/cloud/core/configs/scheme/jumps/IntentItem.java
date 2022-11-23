package com.cloud.core.configs.scheme.jumps;

import android.os.Bundle;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/11
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class IntentItem {
    /**
     * 数据对象
     */
    private Bundle bundle = null;
    /**
     * 类全名
     */
    private String classFullName = "";

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }
}
