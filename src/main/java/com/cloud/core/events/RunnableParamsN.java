package com.cloud.core.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/21
 * @Description:N个参数执行类
 * @Modifier:
 * @ModifyContent:
 */
public abstract class RunnableParamsN<Params> implements Runnable {

    protected Params[] params;

    public RunnableParamsN(Params... params) {
        this.params = params;
    }
}
