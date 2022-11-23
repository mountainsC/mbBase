package com.cloud.core.configs.scheme.jumps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.core.configs.scheme.SchemeConfigParamReturnProperty;
import com.cloud.core.configs.scheme.SchemeItem;
import com.cloud.core.configs.scheme.SchemesHandling;
import com.cloud.core.ObjectJudge;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.events.Action0;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action2;
import com.cloud.core.events.Action3;
import com.cloud.core.events.Func9;
import com.cloud.core.utils.BundleUtils;
import com.cloud.core.utils.RedirectUtils;
import com.cloud.core.utils.ValidUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/24
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class GoPagerTransformer extends BaseGoPagerUtils {

    /**
     * 获取activity全类名
     *
     * @param context
     * @param packageName     获取类名对应的包名
     * @param simpleClassName 短类名
     * @return
     */
    public String getActivityFullClassName(Context context, String packageName, String simpleClassName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo == null || ObjectJudge.isNullOrEmpty(packageInfo.activities)) {
                return "";
            }
            String scname = "";
            for (ActivityInfo activityInfo : packageInfo.activities) {
                String sname = activityInfo.name.substring(activityInfo.name.lastIndexOf(".") + 1);
                if (TextUtils.equals(simpleClassName, sname)) {
                    scname = activityInfo.name;
                    break;
                }
            }
            return scname;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.L.error(e);
        }
        return "";
    }

    /**
     * 获取跳转参数
     *
     * @param t
     * @param configItem 跳转配置项
     * @param <T>
     * @return
     */
    protected <T extends BaseGoBean> GoConfigItem getGoConfigParams(T t, GoConfigItem configItem) {
        HashMap<String, Object> params = configItem.getParams();
        if (!ObjectJudge.isNullOrEmpty(params)) {
            String patten = String.format(RuleParams.MatchTagBetweenContent.getValue(), "#", "#");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    String fieldName = ValidUtils.matche(patten, entry.getValue().toString());
                    if (!TextUtils.isEmpty(fieldName)) {
                        fieldName = fieldName.trim();
                        Object value = GlobalUtils.getPropertiesValue(t, fieldName);
                        params.put(entry.getKey(), value);
                    } else {
                        params.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return configItem;
    }

    /**
     * 通过包名和跳转配置启动activity
     *
     * @param activity    当前activity
     * @param packageName 目标activity对应的包名
     * @param configItem  跳转配置
     */
    public void startActivity(Activity activity, String packageName, GoConfigItem configItem) {
        try {
            if (activity == null) {
                return;
            }
            String fullClassName = getActivityFullClassName(activity, packageName, configItem.getPageName());
            if (TextUtils.isEmpty(fullClassName)) {
                return;
            }
            Bundle bundle = getBundleByConfigItem(activity, configItem);
            if (bundle == null) {
                return;
            }
            RedirectUtils.startActivity(activity, fullClassName, bundle);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 根据跳转配置项获取bundle对象
     *
     * @param context    当前上下文
     * @param configItem 跳转配置项
     * @return
     */
    public Bundle getBundleByConfigItem(Context context, GoConfigItem configItem) {
        if (configItem == null) {
            return null;
        }
        if (TextUtils.isEmpty(configItem.getPageName())) {
            return null;
        }
        //获取bundle
        Bundle bundle = new Bundle();
        HashMap<String, Object> params = configItem.getParams();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                BundleUtils.setBundleValue(bundle, entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

    /**
     * 根据scheme获取bundle对象
     *
     * @param context                       当前上下文
     * @param data                          目标uri
     * @param schemeHost                    scheme协议头(如mibaostore://tenancy/)
     * @param isLogin                       是否登录
     * @param packageName                   目标activity对应的包名
     * @param action                        启动目标activity回调
     * @param mainAction                    启动main activity回调
     * @param configParamReturnPropertyFunc 获取remote scheme配置回调
     * @param finishAction                  结束回调
     */
    protected void getBundleByScheme(final Context context,
                                     Uri data,
                                     String schemeHost,
                                     boolean isLogin,
                                     final String packageName,
                                     final Action2<Bundle, String> action,
                                     Action0 mainAction,
                                     Func9<SchemeConfigParamReturnProperty, Context, GoConfigItem, Uri, Boolean, SchemesHandling, Action1<GoConfigItem>, Action3<GoConfigItem, SchemeItem, Uri>, Action0, Action0> configParamReturnPropertyFunc,
                                     Action0 loginAction,
                                     Action0 finishAction) {
        getGoConfigByScheme(context, data, schemeHost, isLogin, new Action1<GoConfigItem>() {
            @Override
            public void call(GoConfigItem configItem) {
                String fullClassName = getActivityFullClassName(context, packageName, configItem.getPageName());
                if (TextUtils.isEmpty(fullClassName)) {
                    return;
                }
                Bundle bundle = getBundleByConfigItem(context, configItem);
                if (bundle == null) {
                    return;
                }
                if (action != null) {
                    action.call(bundle, fullClassName);
                }
            }
        }, mainAction, configParamReturnPropertyFunc, loginAction, finishAction);
    }

    /**
     * 根据scheme获取intent对象
     *
     * @param context                       当前上下文
     * @param data                          目标uri
     * @param schemeHost                    scheme协议头(如mibaostore://tenancy/)
     * @param isLogin                       是否登录
     * @param packageName                   目标activity对应的包名
     * @param intentAction                  启动intent回调
     * @param mainAction                    启动main回调
     * @param configParamReturnPropertyFunc 获取remote scheme配置回调
     * @param finishAction                  结束回调
     */
    public void getIntentByScheme(final Context context,
                                  Uri data,
                                  String schemeHost,
                                  boolean isLogin,
                                  String packageName,
                                  final Action1<IntentItem> intentAction,
                                  Action0 mainAction,
                                  Func9<SchemeConfigParamReturnProperty, Context, GoConfigItem, Uri, Boolean, SchemesHandling, Action1<GoConfigItem>, Action3<GoConfigItem, SchemeItem, Uri>, Action0, Action0> configParamReturnPropertyFunc,
                                  Action0 loginAction,
                                  Action0 finishAction) {
        getBundleByScheme(context, data, schemeHost, isLogin, packageName, new Action2<Bundle, String>() {
            @Override
            public void call(Bundle bundle, String classFullName) {
                if (intentAction == null) {
                    return;
                }
                IntentItem intentItem = new IntentItem();
                intentItem.setBundle(bundle);
                intentItem.setClassFullName(classFullName);
                intentAction.call(intentItem);
            }
        }, mainAction, configParamReturnPropertyFunc, loginAction, finishAction);
    }
}
