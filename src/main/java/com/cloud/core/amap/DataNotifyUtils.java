package com.cloud.core.amap;

import com.cloud.core.ebus.EBus;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/30
 * Description:数据通知工具类
 * Modifier:
 * ModifyContent:
 */
public class DataNotifyUtils {

    /**
     * 通知已选择城市
     *
     * @param cityName 城市名称
     */
    public static void notifySelectedCity(String cityName) {
        EBus.getInstance().post("cl_amap_selected_city_name", cityName);
    }
}
