package com.cloud.core.view.vlayout;

import android.view.View;

import com.cloud.core.view.sview.SpaceItem;


/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/6
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class OnSubViewListener<SVH extends BaseViewHolder<IVH>, IVH extends BaseItemViewHolder, T> {

    public abstract SVH onCreateHolder(String subKey);

    public abstract void onBindHolder(String subKey, IVH holder, T entity);

    public void onItemClick(String subKey, View itemView, T entity) {

    }

    public void onHSViewSpace(String subKey, SpaceItem spaceItem) {

    }
}
