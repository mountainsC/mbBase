package com.cloud.core.dialog.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface ISelectDialogDataItem {
    public long getId();

    public CharSequence getShowName();

    /**
     * 该数据的是否选中
     */
    void setSelected(boolean isSelected);

    /**
     * 该数据的是否选中
     */
    boolean isSelected();
}
