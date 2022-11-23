package com.cloud.core.picker.utils;

import android.text.TextUtils;

import com.cloud.core.enums.EnvironmentType;
import com.cloud.core.enums.ServiceType;

public class CloudUtils {
    public static String getConfigBasicUrl(String serviceType,String environmentType){
        if (TextUtils.equals(serviceType, ServiceType.Mibao.getValue())) {
            if (TextUtils.equals(environmentType, EnvironmentType.Official.getValue())) {
                return "https://rest.zejihui.com.cn/";
            }else {
                return "https://rest-test1210.zejihui.com.cn/";
            }
        }else {
            return "";
        }
    }
    public static String getImgBasicUrl(String serviceType,String environmentType){
        if (TextUtils.equals(serviceType, ServiceType.Mibao.getValue())) {
            if (TextUtils.equals(environmentType, EnvironmentType.Official.getValue())) {
                return "https://jkz-img.dbzubei.com/";
            }else {
                return "https://zubei-oss.oss-cn-hangzhou.aliyuncs.com/";
            }
        }else {
            return "";
        }
    }
    public static String getH5BasicUrl(String serviceType,String environmentType){
        if (TextUtils.equals(serviceType, ServiceType.Mibao.getValue())) {
            if (TextUtils.equals(environmentType, EnvironmentType.Official.getValue())) {
                return "https://rest.zejihui.com.cn/";
            }else {
                return "https://h5-test1210.zejihui.com.cn/";
            }
        }else {
            return "";
        }
    }
}
