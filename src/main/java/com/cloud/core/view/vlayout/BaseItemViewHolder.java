package com.cloud.core.view.vlayout;

import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseItemViewHolder {
    private View contentView = null;

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
}
