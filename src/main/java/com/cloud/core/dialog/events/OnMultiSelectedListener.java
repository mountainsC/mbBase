package com.cloud.core.dialog.events;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnMultiSelectedListener<T extends ISelectDialogDataItem> {
    public void selected(List<T> selectedDataItems);
}
