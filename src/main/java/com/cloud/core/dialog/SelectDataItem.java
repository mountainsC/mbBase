package com.cloud.core.dialog;


import com.cloud.core.dialog.events.ISelectDialogDataItem;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class SelectDataItem implements ISelectDialogDataItem {
    private int id;
    private String key;
    private CharSequence name;
    private boolean isSelected;

    public SelectDataItem() {

    }

    public SelectDataItem(int id, CharSequence name) {
        this.id = id;
        this.name = name;
    }

    public SelectDataItem(int id, String key, CharSequence name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public SelectDataItem(int id, CharSequence name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    public SelectDataItem(int id, String key, CharSequence name, boolean isSelected) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.isSelected = isSelected;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public CharSequence getShowName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }
}
