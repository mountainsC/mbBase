package com.cloud.core.ebus;

import java.lang.reflect.Method;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/23
 * Description:
 * Modifier:
 * ModifyContent:
 */
class EBusPostItem {
    private ThreadModeEBus threadMode = ThreadModeEBus.MAIN;

    private Method method = null;

    private Object[] args = null;

    public ThreadModeEBus getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadModeEBus threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
