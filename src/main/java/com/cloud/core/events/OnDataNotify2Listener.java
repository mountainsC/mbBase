package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/29
 * @Description:数据通知监听(两个参数)
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDataNotify2Listener<T1, T2> {
    public void onDataNotify(T1 t1, T2 t2);
}
