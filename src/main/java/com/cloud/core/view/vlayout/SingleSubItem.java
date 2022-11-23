package com.cloud.core.view.vlayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class SingleSubItem<T> {
    /**
     * 数据集
     */
    private List<T> subItems = null;
    /**
     * 列数
     */
    private int spanCount = 1;

    public List<T> getSubItems() {
        if (subItems == null) {
            subItems = new ArrayList<T>();
        }
        return subItems;
    }

    public void setSubItems(List<T> subItems) {
        this.subItems = subItems;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }
}
