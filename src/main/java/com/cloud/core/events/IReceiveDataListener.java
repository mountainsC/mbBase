package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/22
 * @Description:获取其它模块数据
 * @Modifier:
 * @ModifyContent:
 */
public interface IReceiveDataListener<T> {

    public void onReceived(String dataType, T data);
}
