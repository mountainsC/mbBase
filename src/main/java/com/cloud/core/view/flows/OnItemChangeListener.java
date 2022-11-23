package com.cloud.core.view.flows;

import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnItemChangeListener<T> {

    public void onItemChange(View v, boolean isCheck, SkuSepecItem sepecItem, SkuItem skuItem);
}
