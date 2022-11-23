package com.cloud.core.view.tabindicator;

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
public interface OnTablayoutIndicatorScorllListener {
    public void onPageSelected(int position, TabItem tabItem, Fragment fragment);
}
