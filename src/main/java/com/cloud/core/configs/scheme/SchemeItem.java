package com.cloud.core.configs.scheme;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/25
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class SchemeItem {
    /**
     * scheme path
     */
    private String schemePath = "";
    /**
     * 参数映射
     */
    HashMap<String, String> paramsMapper = null;
    /**
     * 目标对象
     */
    private TargetItem targets = null;

    public String getSchemePath() {
        return schemePath;
    }

    public void setSchemePath(String schemePath) {
        this.schemePath = schemePath;
    }

    public HashMap<String, String> getParamsMapper() {
        if (paramsMapper == null) {
            paramsMapper = new HashMap<String, String>();
        }
        return paramsMapper;
    }

    public void setParamsMapper(HashMap<String, String> paramsMapper) {
        this.paramsMapper = paramsMapper;
    }

    public TargetItem getTargets() {
        if (targets == null) {
            targets = new TargetItem();
        }
        return targets;
    }

    public void setTargets(TargetItem targets) {
        this.targets = targets;
    }
}
