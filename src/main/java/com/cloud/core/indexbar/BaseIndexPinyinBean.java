package com.cloud.core.indexbar;

public abstract class BaseIndexPinyinBean extends BaseIndexBean {
    private String baseIndexPinyin;//城市的拼音
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    public String getBaseIndexPinyin() {
        return baseIndexPinyin;
    }

    public BaseIndexPinyinBean setBaseIndexPinyin(String baseIndexPinyin) {
        this.baseIndexPinyin = baseIndexPinyin;
        return this;
    }

    //是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
    //微信的头部 不需要显示索引
    //美团的头部 索引自定义
    //默认应该是需要的
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    //需要转化成拼音的目标字段
    public abstract String getTarget();

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
