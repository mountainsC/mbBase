package com.cloud.core.configs;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/17
 * @Description:cloud框架初始化
 * @Modifier:
 * @ModifyContent:
 */
public class RxCloud {

    private static RxCloud cloud = null;
    private RxNames names = null;

    private RxCloud() {
        names = new RxNames();
    }

    public static RxCloud getInstance() {
        return cloud == null ? cloud = new RxCloud() : cloud;
    }

    public RxNames builder() {
        if (names == null) {
            names = new RxNames();
        }
        return names;
    }

    public RxNames getNames() {
        if (this.names == null) {
            this.names = new RxNames();
        }
        return this.names;
    }
}
