package com.cloud.core.dialog.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnSelectedListener<T extends ISelectDialogDataItem> {
    public void selected(T selectedData);
}
