package com.cloud.core.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.cloud.core.ObjectJudge;
import com.cloud.core.beans.ProviderItem;
import com.cloud.core.configs.scheme.CacheConfigAction;
import com.cloud.core.configs.scheme.SchemeTransform;
import com.cloud.core.enums.ActionOperations;
import com.cloud.core.enums.ActivityNames;
import com.cloud.core.enums.MatchActions;
import com.cloud.core.events.Action1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/25
 * @Description:路由工具类
 * @Modifier:
 * @ModifyContent:
 */
public class RoutesUtils {

    private static HashMap<String, Object> objlist = new HashMap<String, Object>();

    public static HashMap<String, Object> getObjlist() {
        return objlist;
    }

    public static void setRoutesListener(String key, Object listener) {
        objlist.put(key, listener);
    }

    public static <T> T getRoutesListener(String key) {
        if (objlist.containsKey(key)) {
            Object listener = objlist.get(key);
            return (T) listener;
        }
        return null;
    }

    /**
     * 移除缓存列表中的对象
     *
     * @param objKey 对象key
     */
    public static void remove(String objKey) {
        if (TextUtils.isEmpty(objKey)) {
            return;
        }
        if (objlist.containsKey(objKey)) {
            objlist.remove(objKey);
        }
    }

//    /**
//     * 调用buildProviderUri添加BaseContentProvider提供的uriMatcher
//     *
//     * @param modules          当前实现BaseContentProvider模块
//     * @param matchActions     匹配操作数据的action
//     * @param actionOperations 对当前action所进行的add get update clear操作
//     * @return
//     */
//    public static UriMatcherItem buildProviderUri(Modules modules, MatchActions matchActions, ActionOperations actionOperations) {
//        UriMatcherItem matcherItem = new UriMatcherItem();
//        if (modules != null && matchActions != null && actionOperations != null) {
//            if (actionOperations == ActionOperations.none) {
//                matcherItem.setUri(String.format("content://%s/%s", modules.getPageName(), matchActions.name()));
//                matcherItem.setAction(String.format("%s", matchActions.name()));
//            } else {
//                matcherItem.setUri(String.format("content://%s/%s_%s", modules.getPageName(), matchActions.name(), actionOperations.name()));
//                matcherItem.setAction(String.format("%s_%s", matchActions.name(), actionOperations.name()));
//            }
//            matcherItem.setCode(actionOperations.ordinal());
//        }
//        return matcherItem;
//    }

//    /**
//     * 添加contentProvider uri匹配action
//     *
//     * @param uriMatcher     由BaseContentProvider提供
//     * @param uriMatcherItem 调用{@link RoutesUtils}方法获取
//     */
//    public static void addUriMatcher(UriMatcher uriMatcher, UriMatcherItem uriMatcherItem) {
//        if (uriMatcher == null ||
//                uriMatcherItem == null ||
//                TextUtils.isEmpty(uriMatcherItem.getAction())) {
//            return;
//        }
//        uriMatcher.addUri(uriMatcherItem.getUri(), uriMatcherItem.getAction(), uriMatcherItem.getCode());
//    }

//    /**
//     * 获取contentProvider内容
//     *
//     * @param context
//     * @param modules      模块
//     * @param matchActions 匹配操作数据的action
//     * @return
//     */
//    public static String getRemoteContent(Context context, Modules modules, MatchActions matchActions) {
//        String query = BaseContentProviderUtils.query(context,
//                modules.getPageName(),
//                String.format("%s_%s", matchActions.name(), ActionOperations.get.name()));
//        return query;
//    }

    /**
     * 获取uri action
     *
     * @param matchActions     匹配操作数据的action
     * @param actionOperations 对当前action所进行的操作项
     * @return
     */
    public static String getUriAction(MatchActions matchActions, ActionOperations actionOperations) {
        if (actionOperations == ActionOperations.none) {
            return matchActions.name();
        } else {
            return String.format("%s_%s", matchActions.name(), actionOperations.name());
        }
    }

    /**
     * 获取uri action
     *
     * @param matchActions 匹配操作数据的action
     * @return
     */
    public static String getUriAction(MatchActions matchActions) {
        return getUriAction(matchActions, ActionOperations.none);
    }

    /**
     * 启动activity
     *
     * @param context       上下文
     * @param activityNames 要启动的activity名称
     * @param bundle        数据
     */
    public static void startActivity(final Context context, final ActivityNames activityNames, final Bundle bundle) {
        if (context == null || activityNames == null) {
            return;
        }
        //如果是商户端前面加/m/
        String schemekey = addPlatformSchemeTag(context, activityNames);
        CacheConfigAction configAction = new CacheConfigAction();
        activityJump(context, schemekey, activityNames, bundle);

//        configAction.getSchemeUrlByKey(context, schemekey, new Action1<String>() {
//            @Override
//            public void call(String schemeUrl) {
//                if (TextUtils.isEmpty(schemeUrl)) {
//                    return;
//                }
//            }
//        });
    }

    private static void transformScheme(Context context, ActivityNames activityNames, String scheme, int requestCode) {
        //如果所要转换的scheme对象未被调用则处理跳转流程
        //在跳转完成或页面销毁的时候移除
        if (RxCachePool.getInstance().containerObjectKey(activityNames.name())) {
            RxCachePool.getInstance().clearObject(activityNames.name());
            if (ObjectJudge.isRunningActivity(context, activityNames.name(), true)) {
                return;
            }
        }
        RxCachePool.getInstance().putObject(activityNames.name(), scheme);
        //跳转处理
        ProviderItem providerItem = new ProviderItem();
        //设置activity名称
        providerItem.setActivityClassName(activityNames.name());
        //设置scheme完整路径
        providerItem.setScheme(scheme);
        //设置页面配置的参数与scheme参数映射关系
        providerItem.setParamList(getActivityParams(activityNames));
        providerItem.setRequestCode(requestCode);
        //通知跳转
//        SchemeProvider schemeProvider = new SchemeProvider();
//        schemeProvider.setProviderItem(providerItem);
//        schemeProvider.setResultCode(false);
        SchemeTransform transform = new SchemeTransform(context, providerItem, false);
        transform.run();
    }

    private static String addPlatformSchemeTag(Context context, ActivityNames activityNames) {
        //如果对应的scheme key不为空则在协议头后加此标识
        String schemekey = activityNames.getSchemeKey();
        com.cloud.core.configs.BuildConfig config = com.cloud.core.configs.BuildConfig.getInstance();
        String platformKey = config.getSchemePlatformKey(context);
        if (!TextUtils.isEmpty(platformKey) && !schemekey.startsWith(platformKey)) {
            if (TextUtils.equals(schemekey.substring(0, 1), "/")) {
                if (platformKey.substring(platformKey.length() - 1).equals("/")) {
                    schemekey = platformKey.substring(0, platformKey.length() - 1) + schemekey;
                } else {
                    schemekey = platformKey + schemekey;
                }
            } else {
                if (platformKey.substring(platformKey.length() - 1).equals("/")) {
                    schemekey = platformKey.substring(0, platformKey.length() - 1) + schemekey;
                } else {
                    schemekey = platformKey + schemekey;
                }
            }
        }
        return schemekey;
    }

    /**
     * 启动activity
     *
     * @param activity      上下文
     * @param activityNames 要启动的activity名称
     * @param bundle        数据
     * @param requestCode   If >= 0, this code will be returned in
     *                      onActivityResult() when the activity exits
     */
    public static void startActivityForResult(final Activity activity, final ActivityNames activityNames, final Bundle bundle, final int requestCode) {
        //如果是商户端前面加/m/
        String schemekey = addPlatformSchemeTag(activity, activityNames);
        //通过回调返回缓存或api scheme url
        CacheConfigAction configAction = new CacheConfigAction();
        configAction.getSchemeUrlByKey(activity, schemekey, new Action1<String>() {
            @Override
            public void call(String schemeUrl) {
                if (TextUtils.isEmpty(schemeUrl)) {
                    return;
                }
                activityJumpForResult(activity, schemeUrl, activityNames, bundle, requestCode);
            }
        });
    }

    private static void activityJump(Context context, String schemeUrl, ActivityNames activityNames, Bundle bundle) {
        String scheme = matchSchemeParams(context, schemeUrl, activityNames, bundle);
        Uri uri = Uri.parse(scheme);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        boolean isModule = com.cloud.core.configs.BuildConfig.getInstance().isModule(context);
        if (isModule) {
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if (componentName != null) {
                transformScheme(context, activityNames, scheme, 0);
            } else {
                Toast toast = Toast.makeText(context, "本地未安装择机汇应用", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            transformScheme(context, activityNames, scheme, 0);
        }
    }

    //activity跳转
    private static void activityJumpForResult(Activity activity, String schemeUrl, ActivityNames activityNames, Bundle bundle, int requestCode) {
        String scheme = matchSchemeParams(activity, schemeUrl, activityNames, bundle);
        Uri uri = Uri.parse(scheme);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        boolean isModule = com.cloud.core.configs.BuildConfig.getInstance().isModule(activity);
        if (isModule) {
            ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
            if (componentName != null) {
                transformScheme(activity, activityNames, scheme, requestCode);
            } else {
                Toast toast = Toast.makeText(activity, "本地未安装择机汇应用", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            transformScheme(activity, activityNames, scheme, requestCode);
        }
    }

    private static HashMap<String, String> getActivityParams(ActivityNames activityNames) {
        HashMap<String, String> params = new HashMap<String, String>();
        String[] mappers = activityNames.getMappers();
        for (String mapper : mappers) {
            String[] fields = mapper.split("=");
            if (fields.length != 2) {
                continue;
            }
            params.put(fields[0], fields[1]);
        }
        return params;
    }

    private static String matchSchemeParams(Context context, String schemeUrl, ActivityNames activityNames, Bundle bundle) {
        int reqindex = schemeUrl.indexOf("?");
        if (reqindex > 0) {
            //去除scheme原有参数(一般情况下原有参数供app外部跳转使用)
            schemeUrl = schemeUrl.substring(0, reqindex);
        }
        //如果是马甲包需要替换scheme协议头
        com.cloud.core.configs.BuildConfig config = com.cloud.core.configs.BuildConfig.getInstance();
        if (config.isVestTag(context)) {
            String schemeHost = config.getSchemeHost(context);
            //如果schemeUrl已经包含对应马甲scheme协议头则不处理
            if (!schemeUrl.startsWith(schemeHost)) {
                String rawSchemeHost = config.getRawSchemeHost(context);
                //如果不包含原协议头无需处理
                if (schemeUrl.startsWith(rawSchemeHost)) {
                    //先去掉原协议头
                    String sub = schemeUrl.substring(rawSchemeHost.length());
                    //拼接当前协议头
                    schemeUrl = PathsUtils.combine(schemeHost, sub);
                }
            }
        }
        //把bundle中的参数添加至schemeUrl中
        StringBuilder scheme = new StringBuilder(schemeUrl);
        if (bundle == null) {
            return scheme.toString();
        }
        String[] mappers = activityNames.getMappers();
        if (mappers == null || mappers.length == 0) {
            return scheme.toString();
        }
        appendSchemeParams(mappers, bundle, activityNames, scheme);
        return scheme.toString();
    }

    private static void appendSchemeParams(String[] mappers, Bundle bundle, ActivityNames activityNames, StringBuilder scheme) {
        for (String mapper : mappers) {
            String[] fields = mapper.split("=");
            if (fields.length != 2) {
                continue;
            }
            if (bundle.containsKey(fields[1])) {
                Object value = bundle.get(fields[1]);
                String paramValue = getParamsValue(value);
                //如果是h5则对url进行url加密
                if (activityNames == ActivityNames.H5WebViewActivity && TextUtils.equals(fields[0], "url")) {
                    try {
                        paramValue = URLEncoder.encode(paramValue, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (scheme.toString().contains("?")) {
                    scheme.append(String.format("&%s=%s", fields[0], paramValue));
                } else {
                    scheme.append(String.format("?%s=%s", fields[0], paramValue));
                }
            }
        }
    }

    private static String getParamsValue(Object value) {
        if (value instanceof Boolean ||
                value instanceof Double ||
                value instanceof Float ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Short ||
                value instanceof String) {
            return value + "";
        } else {
            return JsonUtils.toStr(value);
        }
    }

//    private static ClassIdentity getFragmentStorageKey(Class<?> fragmentHostClass) {
//        ClassIdentity identity = null;
//        Annotation[] annotations = fragmentHostClass.getDeclaredAnnotations();
//        for (Annotation annotation : annotations) {
//            if (annotation instanceof ClassIdentity) {
//                identity = (ClassIdentity) annotation;
//                break;
//            }
//        }
//        return identity;
//    }

//    /**
//     * 获取模块中fragment
//     *
//     * @param context           当前上下文
//     * @param fragmentHostClass fragment宿主
//     * @param targetModules     目标模块
//     * @param fragmentName      要获取的fragment名称
//     * @param positionFunc      若需要则可以通过此回调返回当前要获取的fragment索引(一般用于相同fragment类)
//     * @return
//     */
//    @Deprecated
//    public static Fragment getFragment(Context context,
//                                       Class<?> fragmentHostClass,
//                                       Modules targetModules,
//                                       String fragmentName,
//                                       Func1<String, String> positionFunc) {
//        if (fragmentHostClass == null || TextUtils.isEmpty(fragmentName)) {
//            return null;
//        }
//        ClassIdentity identity = getFragmentStorageKey(fragmentHostClass);
//        //准备fragment查询条件
//        StringBuffer sb = new StringBuffer(identity.value());
//        ObjectPosition objPosition = identity.objPosition();
//        if (objPosition.enable() && positionFunc != null) {
//            sb.append("_" + positionFunc.call(objPosition.key()));
//        }
//        sb.append("_" + fragmentName);
//        ContentProviderQueryWhereItem whereItem = new ContentProviderQueryWhereItem();
//        whereItem.setKey(sb.toString());
//        whereItem.setValue(fragmentName);
//        whereItem.setExtrasQuery(true);
//        //若缓存中存在当前fragment则取出返回
//        if (objlist.containsKey(whereItem.getKey())) {
//            return (Fragment) objlist.get(whereItem.getKey());
//        } else {
//            //从对应的模块中获取
//            List<ContentProviderQueryWhereItem> items = new ArrayList<ContentProviderQueryWhereItem>();
//            items.add(whereItem);
//            //获取fragment
//            String action = String.format("%s_%s", MatchActions.fragmentObject.name(), ActionOperations.get.name());
//            String query = BaseContentProviderUtils.query(context,
//                    targetModules.getPageName(),
//                    action,
//                    items);
//            if (TextUtils.equals(query, "success")) {
//                //代表fragment已添加到集合中
//                if (objlist.containsKey(whereItem.getKey())) {
//                    return (Fragment) objlist.get(whereItem.getKey());
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }
//    }

    private static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
