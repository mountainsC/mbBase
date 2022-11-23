package com.cloud.core.configs.x5;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/5/2
 * @Description:初始化用户代理监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnInitUserAgentListener {

    /**
     * 向userAgents添加额外的参数
     *
     * @param userAgents
     */
    public void addUserAgent(List<String> userAgents);
}
