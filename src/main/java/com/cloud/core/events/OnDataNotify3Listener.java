package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/29
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDataNotify3Listener<T1, T2, T3> {
    public void onDataNotify(T1 t1, T2 t2, T3 t3);
}
