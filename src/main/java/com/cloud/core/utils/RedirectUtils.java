package com.cloud.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.utils.BaseRedirectUtils;


/**
 * @Author lijinghuan
 * @Email: ljh0576123@163.com
 * @CreateTime:2016/4/1 18:54
 * @Description:对象跳转工具类
 * @Modifier:
 * @ModifyContent:
 */
public class RedirectUtils extends BaseRedirectUtils {

    /**
     * 启动activity
     *
     * @param context 上下文
     * @param cls     要启动activity的类对象
     */
    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity    提供上下文的activity
     * @param cls         要启动activity的类对象
     * @param requestCode 回调onActivityResult中接收的requestCode参数
     */
    public static void startActivityForResult(Activity activity,
                                              Class<?> cls,
                                              int requestCode) {
        startActivityForResult(activity, cls, null, requestCode);
    }

    /**
     * 打开App设置页面
     *
     * @param context 上下文
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 启动指定包下的activity
     *
     * @param context       提供上下文的context
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     */
    public static void startPkgActivity(Context context,
                                        String packageName,
                                        String classFullName) {
        startPkgActivity(context, packageName, classFullName, null);
    }

    /**
     * 启动指定包下的activity
     *
     * @param context       提供上下文的context
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param bundle        传入的bundle数据
     */
    public static void startPkgActivity(Context context,
                                        String packageName,
                                        String classFullName,
                                        Bundle bundle) {
        if (TextUtils.isEmpty(classFullName)) {
            return;
        }

        Intent intent = new Intent();
        intent.setClassName(packageName, classFullName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(context, classFullName, false)) {
            context.startActivity(intent);
        }
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数
     */
    public static void startPkgActivityForResult(Activity activity,
                                                 String packageName,
                                                 String classFullName,
                                                 int requestCode) {
        startPkgActivityForResult(activity, packageName, classFullName, null, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param bundle        传入的bundle数据
     * @param requestCode   回调onActivityResult中接收的requestCode参数
     */
    public static void startPkgActivityForResult(Activity activity,
                                                 String packageName,
                                                 String classFullName,
                                                 Bundle bundle,
                                                 int requestCode) {
        if (TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(packageName, classFullName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(activity, classFullName, false)) {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
