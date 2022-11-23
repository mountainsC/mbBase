package com.cloud.core.bases;


import androidx.fragment.app.FragmentManager;

import com.cloud.core.beans.TagsItem;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:分页适配器(未用系统提供的适配器，避免部分机型在7.0系统版本上快速滑动崩溃问题)
 * Modifier:
 * ModifyContent:
 */
public abstract class BaseTagsPagerAdapter extends BasePagerAdapter<TagsItem> {

    protected BaseTagsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * 添加数据项
     *
     * @param id   标识
     * @param name 名称
     */
    public void addItem(int id, String name) {
        TagsItem tagsItem = new TagsItem(id, name);
        super.addItem(tagsItem);
    }
}
