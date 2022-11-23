package com.cloud.core.view.flows;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/13
 * @Description:标签属性
 * @Modifier:
 * @ModifyContent:
 */
public class TagProperties<T> {
    /**
     * 标签文本
     */
    private String text = "";
    /**
     * sku别名
     */
    private String alias = "";
    /**
     * 数据
     */
    private T data = null;
    /**
     * 是否选中
     */
    private boolean isCheck = false;
    /**
     * 是否禁用
     */
    private boolean isDisable = false;
    /**
     * 标签项默认背景
     */
    private int defaultItemBackground = 0;
    /**
     * 标签项选中背景
     */
    private int selectedItemBackground = 0;
    /**
     * 标签被禁用时背景
     */
    private int disableItemBackground = 0;
    /**
     * 默认文本颜色
     */
    private int defaultItemTextColor = 0;
    /**
     * 选中项文本颜色
     */
    private int selectedItemTextColor = 0;
    /**
     * 被禁用项文本颜色
     */
    private int disableItemTextColor = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public int getDefaultItemBackground() {
        return defaultItemBackground;
    }

    public void setDefaultItemBackground(int defaultItemBackground) {
        this.defaultItemBackground = defaultItemBackground;
    }

    public int getSelectedItemBackground() {
        return selectedItemBackground;
    }

    public void setSelectedItemBackground(int selectedItemBackground) {
        this.selectedItemBackground = selectedItemBackground;
    }

    public int getDisableItemBackground() {
        return disableItemBackground;
    }

    public void setDisableItemBackground(int disableItemBackground) {
        this.disableItemBackground = disableItemBackground;
    }

    public int getDefaultItemTextColor() {
        return defaultItemTextColor;
    }

    public void setDefaultItemTextColor(int defaultItemTextColor) {
        this.defaultItemTextColor = defaultItemTextColor;
    }

    public int getSelectedItemTextColor() {
        return selectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.selectedItemTextColor = selectedItemTextColor;
    }

    public int getDisableItemTextColor() {
        return disableItemTextColor;
    }

    public void setDisableItemTextColor(int disableItemTextColor) {
        this.disableItemTextColor = disableItemTextColor;
    }
}
