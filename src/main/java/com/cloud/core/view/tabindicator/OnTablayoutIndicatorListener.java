package com.cloud.core.view.tabindicator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.cloud.core.beans.TabItem;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/10/26
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnTablayoutIndicatorListener {
    public Fragment onBuildFragment(int position, TabItem tabItem, Bundle bundle);
}
