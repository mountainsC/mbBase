package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.beans.BasicConfigBean;
import com.cloud.core.enums.EnvironmentType;
import com.cloud.core.enums.ServiceType;
import com.cloud.core.picker.utils.CloudUtils;
import com.cloud.core.utils.PathsUtils;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class APIConfigProcess {

    private static APIConfigProcess apiConfigProcess = null;
    private HashMap<ServiceType, BasicConfigBean> apiList = new HashMap<ServiceType, BasicConfigBean>();

    public static APIConfigProcess getInstance() {
        return apiConfigProcess == null ? apiConfigProcess = new APIConfigProcess() : apiConfigProcess;
    }

    /**
     * 获取基础配置信息
     *
     * @param context       上下文
     * @param projectTag    项目标识
     * @param hasApiVersion true-包含api版本;false-不包含;
     * @return
     */
    public BasicConfigBean getBasicConfigBean(Context context, String projectTag, boolean hasApiVersion) {
        boolean isRelease = BuildConfig.getInstance().isRelease(context);
        String apiVersion = hasApiVersion ? BuildConfig.getInstance().apiVersion(context) : "";
        ServiceType serviceType = BuildConfig.getInstance().getApiServiceType(context, projectTag);
        if (ObjectJudge.isNullOrEmpty(apiList) ||
                !apiList.containsKey(serviceType) || !hasApiVersion) {
            BasicConfigBean configBean = initNotRemoteInfoConfig(context, isRelease, apiVersion, serviceType, hasApiVersion);
            if (hasApiVersion) {
                apiList.put(serviceType, configBean);
            }
            return configBean;
        } else {
            BasicConfigBean configBean = apiList.get(serviceType);
            if (configBean == null || TextUtils.isEmpty(configBean.getApiUrl())) {
                configBean = initNotRemoteInfoConfig(context, isRelease, apiVersion, serviceType, hasApiVersion);
                apiList.put(serviceType, configBean);
            }
            return configBean;
        }
    }

    /**
     * 获取基础配置信息
     *
     * @param context    上下文
     * @param projectTag 项目标识
     * @return
     */
    public BasicConfigBean getBasicConfigBean(Context context, String projectTag) {
        return getBasicConfigBean(context, projectTag, true);
    }

    /**
     * 获取基础配置信息
     *
     * @param context 上下文
     * @return
     */
    public BasicConfigBean getBasicConfigBean(Context context) {
        return getBasicConfigBean(context, "");
    }

    private BasicConfigBean initNotRemoteInfoConfig(Context context, boolean isRelease, String apiVersion, ServiceType serviceType, boolean hasApiVersion) {
        BasicConfigBean configBean = new BasicConfigBean();

        EnvironmentType environmentType = isRelease ? EnvironmentType.Official : EnvironmentType.Testing;

        String testApiUrl = BuildConfig.getInstance().getBuildString(context, "testApiUrl");
        if (!isRelease && !TextUtils.isEmpty(testApiUrl)) {
            //如果isRelease==false且testApiUrl不为空，则接口请求时此基地址为准;
            configBean.setApiUrl(testApiUrl);
        } else {
            configBean.setApiUrl(CloudUtils.getConfigBasicUrl(serviceType.getValue(), environmentType.getValue()));
        }
        if (!configBean.getApiUrl().contains(apiVersion) && hasApiVersion) {
            configBean.setApiUrl(PathsUtils.combine(configBean.getApiUrl(), apiVersion));
        }
        //img
        configBean.setImgUrl(CloudUtils.getImgBasicUrl(serviceType.getValue(), environmentType.getValue()));
        //h5
        configBean.setH5Url(CloudUtils.getH5BasicUrl(serviceType.getValue(), environmentType.getValue()));
        return configBean;
    }
}
