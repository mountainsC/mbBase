package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/26
 * @Description:数据通知监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDataNotifyListener<T> {

    public void onDataNotify(T t);
}
