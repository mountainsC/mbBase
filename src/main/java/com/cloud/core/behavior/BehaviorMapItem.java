package com.cloud.core.behavior;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/30
 * @Description:行为map数据
 * @Modifier:
 * @ModifyContent:
 */
public class BehaviorMapItem {

    private HashMap<String, String> map = null;

    private int du = 0;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public int getDu() {
        return du;
    }

    public void setDu(int du) {
        this.du = du;
    }
}
