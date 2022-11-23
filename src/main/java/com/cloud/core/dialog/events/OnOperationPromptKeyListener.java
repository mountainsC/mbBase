package com.cloud.core.dialog.events;

import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/7
 * @Description:操作提示视图事件监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnOperationPromptKeyListener {
    /**
     * 目标视图click
     *
     * @param view 目标视图
     */
    public void onTargetViewClick(View view);
}
