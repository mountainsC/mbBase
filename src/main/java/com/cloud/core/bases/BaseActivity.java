package com.cloud.core.bases;

import android.app.Activity;

import com.cloud.core.update.UpdateFlow;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/4
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseActivity extends BaseSupperActivity {
    /**
     * 获取当前Activity对象
     *
     * @return
     */
    public Activity getActivity() {
        return BaseActivity.this;
    }

    public UpdateFlow getUpdateFlow() {
        return mwoutils.getUpdateFlow();
    }
}
