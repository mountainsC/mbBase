package com.cloud.core.dialog;

import android.view.View;

import com.cloud.core.dialog.plugs.DialogPlus;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/11
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class OnDialogBuildListener {

    protected void onBuilded(View contentView) {
        //构建完成回调
    }

    protected abstract void onClickListener(DialogPlus dialog, View view);
}
