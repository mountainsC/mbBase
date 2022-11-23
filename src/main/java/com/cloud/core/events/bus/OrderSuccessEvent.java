package com.cloud.core.events.bus;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/29
 * @Description:下单成功
 * @Modifier:
 * @ModifyContent:
 */
public class OrderSuccessEvent<T> {
    /**
     * 接收key
     */
    public String key = "";
    /**
     * 授权来源(下单时传回)
     */
    public int authSource = 0;
    /**
     * 扩展数据
     */
    public T data = null;
}
