package com.cloud.core.bases;


import androidx.fragment.app.FragmentActivity;

import com.cloud.core.update.UpdateFlow;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/3
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseFragmentActivity extends BaseSupperFragmentActivity {

    public FragmentActivity getActivity() {
        return BaseFragmentActivity.this;
    }

    public UpdateFlow getUpdateFlow() {
        return mwoutils.getUpdateFlow();
    }
}
