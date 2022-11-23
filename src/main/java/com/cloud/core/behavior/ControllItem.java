package com.cloud.core.behavior;

import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ControllItem {
    /**
     * 控件id名称
     */
    private String ctrlIdName = "";
    /**
     * 当前视图
     */
    private View view = null;

    public String getCtrlIdName() {
        return ctrlIdName;
    }

    public void setCtrlIdName(String ctrlIdName) {
        this.ctrlIdName = ctrlIdName;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
