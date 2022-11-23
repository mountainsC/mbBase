package com.cloud.core.configs;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.core.enums.ServiceType;
import com.cloud.core.logger.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/8
 * @Description:获取项目BuildConfig值
 * @Modifier:
 * @ModifyContent:
 */
public class BuildConfig extends BaseBuildConfig {

    private static BuildConfig buildConfig = null;

    public static BuildConfig getInstance() {
        return buildConfig == null ? buildConfig = new BuildConfig() : buildConfig;
    }

    /**
     * 获取当前工程包名
     *
     * @param context
     * @return
     */
    public String getPackageName(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext.getPackageName();
    }

    /**
     * 是否模块开发
     *
     * @param context 当前上下文
     * @return
     */
    public boolean isModule(Context context) {
        return !getBuildBoolean(context, "isMainProject");
    }

    /**
     * 获取项目标识
     * MB_USER-客户端;MB_MER-商户端;
     *
     * @param context
     * @return
     */
    public String getProjectTag(Context context) {
        String value = getBuildString(context, "client");
        if (TextUtils.isEmpty(value)) {
            //返回客户端标识
            return "MB_USER";
        } else {
            return value;
        }
    }

    /**
     * 是否release环境
     *
     * @param context
     * @return
     */
    public boolean isRelease(Context context) {
        return getBuildBoolean(context, "isRelease");
    }

    /**
     * 获取api版本
     *
     * @param context
     * @return
     */
    public String apiVersion(Context context) {
        return getBuildString(context, "apiVersion");
    }

    /**
     * 获取api service type
     *
     * @param context    上下文
     * @param projectTag 项目标识
     * @return
     */
    public ServiceType getApiServiceType(Context context, String projectTag) {
        if (TextUtils.isEmpty(projectTag)) {
            projectTag = getProjectTag(context);
        }
        if (TextUtils.equals(projectTag, "MB_USER")) {
            return ServiceType.Mibao;
        } else {
            return ServiceType.MibaoMer;
        }
    }

    /**
     * 获取api service type
     *
     * @param context 上下文
     * @return
     */
    public ServiceType getApiServiceType(Context context) {
        return getApiServiceType(context, "");
    }

    /**
     * 获取主包下对应的scheme协议头
     *
     * @param context
     * @return
     */
    public String getRawSchemeHost(Context context) {
        String baseSchemeHost = getBuildString(context, "schemeHost");
        return baseSchemeHost;
    }

    /**
     * 获取scheme协议头地址(如果是马甲包已带对应标识)
     *
     * @param context
     * @return
     */
    public String getSchemeHost(Context context) {
        String baseSchemeHost = getBuildString(context, "schemeHost").trim();
        String hostTag = getBuildString(context, "vestPackageSchemeHostTag");
        //baseSchemeHost最后带有/则去掉
        if (baseSchemeHost.endsWith("/")) {
            baseSchemeHost = baseSchemeHost.substring(0, baseSchemeHost.length() - 1);
        }
        return baseSchemeHost + hostTag;
    }

    /**
     * 判断包是否马甲标识
     *
     * @return true-马甲包;false-正常应用包;
     */
    public boolean isVestTag(Context context) {
        String vestTags = getBuildString(context, "vestTags");
        if (TextUtils.isEmpty(vestTags)) {
            return false;
        }
        List<String> tags = Arrays.asList(vestTags.split(","));
        String channelName = getChannelName(context);
        return tags.contains(channelName);
    }

    /**
     * 获取渠道标识
     * (先获取UMENG_CHANNEL中配置的值,如果为空则获取项目下BuildConfig中FLAVOR值)
     *
     * @param context
     * @return
     */
    public String getChannelName(Context context) {
        String channel = "";
        Context applicationContext = context.getApplicationContext();
        PackageManager packageManager = applicationContext.getPackageManager();
        String packageName = applicationContext.getPackageName();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            if (metaData != null && metaData.containsKey("UMENG_CHANNEL")) {
                channel = metaData.getString("UMENG_CHANNEL");
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
        if (!TextUtils.isEmpty(channel)) {
            return channel;
        }
        String flavor = getBuildString(context, "FLAVOR");
        return flavor;
    }

    /**
     * scheme key前缀(用于区分每个端,如key="/m/"则协议头为mibaostore://tenancy/m/)
     *
     * @return
     */
    public String getSchemePlatformKey(Context context) {
        return getBuildString(context, "schemePlatformKey");
    }

    /**
     * 请求scheme接口组标识[USER_APP、MERCHANT_APP](用于区分主包、马甲包、商户包)
     *
     * @param context
     * @return
     */
    public String getSchemeGroup(Context context) {
        return getBuildString(context, "schemeGroup");
    }
}
