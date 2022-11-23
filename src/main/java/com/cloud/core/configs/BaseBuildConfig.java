package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.OkAndroid;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.GlobalUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/17
 * @Description:获取项目的BuildConfig信息
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseBuildConfig {

    /**
     * 获取当前项目BuildConfig对应字段的值
     *
     * @param context   上下文
     * @param fieldName 字段名
     * @return
     */
    public String getBuildString(Context context, String fieldName) {
        if (context == null || TextUtils.isEmpty(fieldName)) {
            return "";
        }
        OkAndroid.OkAndroidBuilder builder = OkAndroid.getInstance().getBuilder(context);
        String path = String.format("%s.BuildConfig", builder.getProjectBuildConfigPackgeName());
        Object client = GlobalUtils.getBuildConfigValue(path, fieldName);
        if (client instanceof String) {
            return String.valueOf(client);
        } else {
            return "";
        }
    }

    /**
     * 获取当前项目BuildConfig对应字段的值
     *
     * @param context   上下文
     * @param fieldName 字段名
     * @return
     */
    public int getBuildInt(Context context, String fieldName) {
        if (context == null || TextUtils.isEmpty(fieldName)) {
            return 0;
        }
        OkAndroid.OkAndroidBuilder builder = OkAndroid.getInstance().getBuilder(context);
        String path = String.format("%s.BuildConfig", builder.getProjectBuildConfigPackgeName());
        Object value = GlobalUtils.getBuildConfigValue(path, fieldName);
        if (value instanceof Integer) {
            return ConvertUtils.toInt(value);
        } else {
            return 0;
        }
    }

    /**
     * 获取当前项目BuildConfig对应字段的值
     *
     * @param context   上下文
     * @param fieldName 字段名
     * @return
     */
    public boolean getBuildBoolean(Context context, String fieldName) {
        if (context == null || TextUtils.isEmpty(fieldName)) {
            return false;
        }
        OkAndroid.OkAndroidBuilder builder = OkAndroid.getInstance().getBuilder(context);
        String path = String.format("%s.BuildConfig", builder.getProjectBuildConfigPackgeName());
        Object value = GlobalUtils.getBuildConfigValue(path, fieldName);
        if (value instanceof Boolean) {
            return ObjectJudge.isTrue(value);
        } else {
            return false;
        }
    }

    /**
     * true-debug环境;false-release环境;
     *
     * @param context
     * @return
     */
    public boolean DEBUG(Context context) {
        return getBuildBoolean(context, "DEBUG");
    }

    /**
     * 获取项目版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public int getVersionCode(Context context) {
        return getBuildInt(context, "VERSION_CODE");
    }

    /**
     * 获取项目版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public String getVersionName(Context context) {
        return getBuildString(context, "VERSION_NAME");
    }
}
