package com.cloud.core.configs.scheme;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.api.services.AgreementService;
import com.cloud.core.beans.RedirectionConfigItem;
import com.cloud.core.configs.BuildConfig;
import com.cloud.core.configs.scheme.jumps.GoConfigItem;
import com.cloud.core.configs.scheme.jumps.GoPagerUtils;
import com.cloud.core.daos.DaoManager;
import com.cloud.core.daos.SchemeCacheItemDao;
import com.cloud.core.dialog.LoadingDialog;
import com.cloud.core.events.Action0;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action3;
import com.cloud.core.events.Func9;
import com.cloud.core.okrx.events.OnSuccessfulListener;
import com.cloud.core.utils.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class CacheConfigAction implements Func9<SchemeConfigParamReturnProperty, Context, GoConfigItem, Uri, Boolean, SchemesHandling, Action1<GoConfigItem>, Action3<GoConfigItem, SchemeItem, Uri>, Action0, Action0> {

    private LoadingDialog mloading = new LoadingDialog();
    private Action1<GoConfigItem> configItemAction = null;
    private Action3<GoConfigItem, SchemeItem, Uri> goConfigItemSchemeItemUriAction = null;
    private Action0 loginAction = null;
    private Action0 finishAction = null;
    private int templateId = 0;
    private int requestVersion = 0;
    //远程配置是否获取成功
    private boolean isReqRemoteConfigSuccess = false;
    private Context context = null;
    //scheme参数与页面定义参数映射
    private HashMap<String, String> goParamList = null;
    //scheme url 回调
    private Action1<String> schemeUrlCall = null;
    //是否跳转前获取scheme url;true-跳转前获取;false-跳转后获取
    private boolean isBeforeRequest = false;
    //scheme处理名柄
    SchemesHandling handling = null;
    //是否登录
    private boolean isLogin = false;

    public void setGoParamList(HashMap<String, String> paramList) {
        this.goParamList = paramList;
    }

    public CacheConfigAction() {

    }

    public CacheConfigAction(int templateId, int requestVersion) {
        this.templateId = templateId;
        this.requestVersion = requestVersion;
    }

    private AgreementService agreementService = new AgreementService() {
        @Override
        protected void onRequestCompleted() {
            if (!isBeforeRequest) {
                //跳转后请求
                if (isReqRemoteConfigSuccess) {
                    return;
                }
                if (finishAction == null) {
                    return;
                }
                finishAction.call();
            }
            mloading.dismiss();
        }
    };

    @Override
    public SchemeConfigParamReturnProperty call(Context context, GoConfigItem goConfigItem, Uri data, Boolean isLogin, SchemesHandling handling, Action1<GoConfigItem> configItemAction, Action3<GoConfigItem, SchemeItem, Uri> goConfigItemSchemeItemUriAction, Action0 loginAction, Action0 finishAction) {
        this.context = context;
        this.configItemAction = configItemAction;
        this.isLogin = isLogin;
        this.handling = handling;
        this.goConfigItemSchemeItemUriAction = goConfigItemSchemeItemUriAction;
        this.loginAction = loginAction;
        this.finishAction = finishAction;
        SchemeConfigParamReturnProperty property = new SchemeConfigParamReturnProperty();
        //scheme path
        String schemePath = getSpecifPath(data.getPath());
        if (TextUtils.isEmpty(schemePath)) {
            property.setSchemeSource(null);
            return property;
        }

        SchemeCacheItem cacheScheme = getLocalCacheScheme(schemePath);
        //如果当前缓存scheme为null则请求接口获取
        if (cacheScheme == null || TextUtils.isEmpty(cacheScheme.getSchemeUrl()) || TextUtils.isEmpty(cacheScheme.getSchemeJson())) {
            return checkVersionAndUpdate(context, property, cacheScheme, goConfigItem, data, schemePath, null, true);
        }

        SchemeItem schemeItem = JsonUtils.parseT(cacheScheme.getSchemeJson(), SchemeItem.class);
        if (schemeItem == null) {
            return checkVersionAndUpdate(context, property, cacheScheme, goConfigItem, data, schemePath, schemeItem, true);
        }

        //如果本地缓存的参数与要跳转的参数不匹配也需要更新
        if (!checkParams(schemeItem)) {
            return checkVersionAndUpdate(context, property, cacheScheme, goConfigItem, data, schemePath, schemeItem, true);
        }

        //如果本地版本比较配置文件中提供的版本小再调用接口请求
        return checkVersionAndUpdate(context, property, cacheScheme, goConfigItem, data, schemePath, schemeItem, false);
    }

    /**
     * 检测参数(检测的参数列表为空时返回true)
     *
     * @return true-匹配成功;false-匹配失败;
     */
    private boolean checkParams(SchemeItem schemeItem) {
        if (ObjectJudge.isNullOrEmpty(goParamList)) {
            return true;
        }
        boolean flag = true;
        HashMap<String, String> mapper = schemeItem.getParamsMapper();
        for (Map.Entry<String, String> entry : goParamList.entrySet()) {
            if (mapper.containsKey(entry.getKey())) {
                String mapperField = mapper.get(entry.getKey());
                if (!TextUtils.equals(mapperField, entry.getValue())) {
                    flag = false;
                    break;
                }
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    //scmap-缓存参数集合;
    //parameterNames-scheme参数集合
    private boolean checkParamNames(HashMap<String, String> scmap, Set<String> parameterNames) {
        boolean flag = true;
        for (Map.Entry<String, String> entry : scmap.entrySet()) {
            if (!parameterNames.contains(entry.getKey())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private SchemeConfigParamReturnProperty checkVersionAndUpdate(Context context,
                                                                  SchemeConfigParamReturnProperty property,
                                                                  SchemeCacheItem cacheScheme,
                                                                  GoConfigItem goConfigItem,
                                                                  Uri data,
                                                                  String schemePath,
                                                                  SchemeItem schemeItem,
                                                                  boolean isDirectlyUpdate) {
        if (isDirectlyUpdate) {
            updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
            return property;
        }
        int schemeVersion = BuildConfig.getInstance().getBuildInt(context, "schemeVersion");
        //如果模块开发时schemeVersion==0，一般需要每次均调用接口来更新scheme缓存配置；
        //集成后根据schemeVersion来判断;
        if (schemeVersion <= 0 || schemeVersion > cacheScheme.getSchemeVersion()) {
            //一般首次（或本地缓存清除）请求或主框架更新时均会进入此回调
            updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
            return property;
        } else {
            //参数个数变化时更新
            //scheme中的参数个数与缓存中的参数个数进行比较
            Set<String> parameterNames = data.getQueryParameterNames();
            int schemeParamCount = parameterNames.size();
            int cacheParamCount = ObjectJudge.isNullOrEmpty(schemeItem.getParamsMapper()) ? 0 : schemeItem.getParamsMapper().size();
            if (schemeParamCount != cacheParamCount) {
                updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
                return property;
            } else {
                HashMap<String, String> scmap = JsonUtils.parseT(cacheScheme.getSchemeJson(), HashMap.class);
                if (scmap == null || !scmap.containsKey("schemePath")) {
                    //如果缓存数据为空或不包含scheme路径则更新并缓存配置
                    updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
                    return property;
                } else {
                    if (checkParamNames(scmap, parameterNames)) {
                        String path = scmap.get("schemePath");
                        if (!TextUtils.equals(path, schemePath)) {
                            //如果scheme路径不符则更新并缓存配置
                            updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
                            return property;
                        }
                    } else {
                        updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
                        return property;
                    }
                }
            }
        }
        //检测目标名称在当前清单中是否存在，如果不存在则更新接口
        if (checkTargetIsExist(context, schemeItem.getTargets().getName())) {
            property.setScheme(schemeItem);
            property.setSchemeSource(SchemeSource.localVersionScheme);
        } else {
            updateAndCacheConfig(context, property, goConfigItem, data, schemePath);
        }
        return property;
    }

    private boolean checkTargetIsExist(Context context, String activityClassName) {
        GoPagerUtils pagerUtils = new GoPagerUtils();
        Context applicationContext = context.getApplicationContext();
        String packageName = applicationContext.getPackageName();
        String fullClassName = pagerUtils.getActivityFullClassName(context, packageName, activityClassName);
        if (TextUtils.isEmpty(fullClassName)) {
            return false;
        } else {
            return true;
        }
    }

    private void updateAndCacheConfig(Context context, SchemeConfigParamReturnProperty property, GoConfigItem configItem, Uri data, String schemePath) {
        property.setSchemeSource(SchemeSource.remoteScheme);
        //请求接口
        SchemeConfigProperty configProperty = new SchemeConfigProperty();
        configProperty.setConfigItem(configItem);
        configProperty.setData(data);
        isReqRemoteConfigSuccess = false;
        isBeforeRequest = false;
        //模块开发或获取到schemeVersion为空时，则取schemeVersion最大的配置信息
        int schemeVersion = BuildConfig.getInstance().getBuildInt(context, "schemeVersion");
        String schemeGroup = BuildConfig.getInstance().getSchemeGroup(context);
        agreementService.requestRedirectionConfigByKey(context, schemePath, schemeVersion, schemeGroup, configProperty, redirectionConfigSuccessfulListener);
    }

    private OnSuccessfulListener<RedirectionConfigItem> redirectionConfigSuccessfulListener = new OnSuccessfulListener<RedirectionConfigItem>() {
        @Override
        public void onSuccessful(RedirectionConfigItem redirectionConfigItem, String schemePath, Object extras) {
            if (redirectionConfigItem == null || extras == null || !(extras instanceof SchemeConfigProperty)) {
                return;
            }
            isReqRemoteConfigSuccess = true;
            SchemeConfigProperty configProperty = (SchemeConfigProperty) extras;
            //缓存配置
            cacheSchemeByPath(context, schemePath, redirectionConfigItem);
            //调用goConfigItemSchemeItemUriAction进行参数绑定
            if (goConfigItemSchemeItemUriAction == null || TextUtils.isEmpty(redirectionConfigItem.getJsonConfig())) {
                return;
            }
            SchemeItem schemeItem = JsonUtils.parseT(redirectionConfigItem.getJsonConfig(), SchemeItem.class);
            //跳转配置属性
            GoConfigItem configItem = configProperty.getConfigItem();
            goConfigItemSchemeItemUriAction.call(configItem, schemeItem, configProperty.getData());
            if (handling.checkPass(configItem, isLogin)) {
                configItemAction.call(configItem);
            } else {
                if (loginAction != null) {
                    loginAction.call();
                }
            }
        }
    };

    private String getSpecifPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        String startSub = path.trim().substring(0, 1);
        if (!TextUtils.equals(startSub, "/")) {
            path = "/" + path;
        }
        String endSub = path.substring(path.trim().length() - 1);
        if (!TextUtils.equals(endSub, "/")) {
            path = path + "/";
        }
        return path;
    }

    private SchemeCacheItem getLocalCacheScheme(final String schemePath) {
     DaoManager daoManager = new DaoManager();
        final SchemeCacheItem[] schemes = new SchemeCacheItem[1];
        SchemeCacheItemDao itemDao = daoManager.getSchemeCacheItemDao();
        if (itemDao == null) {
            return null;
        }
        QueryBuilder<SchemeCacheItem> builder = itemDao.queryBuilder();
        builder.where(SchemeCacheItemDao.Properties.SchemePath.eq(schemePath));
        builder.orderDesc(SchemeCacheItemDao.Properties.SchemeVersion);
        QueryBuilder<SchemeCacheItem> limit = builder.limit(1);
        if (limit != null) {
            schemes[0] = limit.unique();
        }

        SchemeCacheItem scheme = schemes[0];
        return scheme;
    }

    private void cacheSchemeByPath(Context context, String schemePath, RedirectionConfigItem redirectionConfigItem) {
        if (redirectionConfigItem == null) {
            return;
        }
        context = context == null ? CacheConfigAction.this.context : context;
        SchemeCacheItem cacheItem = new SchemeCacheItem();
        cacheItem.setSchemePath(schemePath);
        //当前包schemeVersion版本比较大的（后期做配置更新）
        int schemeVersion = BuildConfig.getInstance().getBuildInt(context, "schemeVersion");
        if (schemeVersion > redirectionConfigItem.getVersionCode()) {
            cacheItem.setSchemeVersion(schemeVersion);
            //此属性设置为true时，下次检测时如果超过24小时则重新请求
            cacheItem.setNeedCheckUpdate(true);
            cacheItem.setCacheTime(System.currentTimeMillis());
        } else {
            cacheItem.setSchemeVersion(redirectionConfigItem.getVersionCode());
        }
        cacheItem.setSchemeJson(redirectionConfigItem.getJsonConfig());
        cacheItem.setSchemeUrl(redirectionConfigItem.getSchemeUrl());

        DaoManager daoManager = new DaoManager();
        SchemeCacheItemDao itemDao = daoManager.getSchemeCacheItemDao();
        if (itemDao != null) {
            itemDao.insertOrReplaceInTx(cacheItem);
        }
    }

    /**
     * 根据schemeKey获取scheme完整路径
     *
     * @param schemeKey scheme key
     */
    public void getSchemeUrlByKey(final Context context, String schemeKey, Action1<String> schemeUrlAction) {
        if (schemeUrlAction == null) {
            return;
        }
        this.schemeUrlCall = schemeUrlAction;
        String schemePath = getSpecifPath(schemeKey);
        if (TextUtils.isEmpty(schemePath)) {
            return;
        }
        SchemeCacheItem cacheScheme = getLocalCacheScheme(schemePath);
        if (cacheScheme == null || TextUtils.isEmpty(cacheScheme.getSchemeUrl())) {
            isBeforeRequest = true;
            mloading.showDialog(context, R.string.processing_just, null);
            //模块开发或获取到schemeVersion为空时，则取schemeVersion最大的配置信息
            int schemeVersion = BuildConfig.getInstance().getBuildInt(context, "schemeVersion");
            String schemeGroup = BuildConfig.getInstance().getSchemeGroup(context);
            agreementService.requestRedirectionConfigByKey(context, schemePath, schemeVersion, schemeGroup, null, new OnSuccessfulListener<RedirectionConfigItem>() {
                @Override
                public void onSuccessful(RedirectionConfigItem redirectionConfigItem, String schemePath, Object extras) {
                    cacheSchemeByPath(context, schemePath, redirectionConfigItem);
                    if (schemeUrlCall != null && redirectionConfigItem != null) {
                        schemeUrlCall.call(redirectionConfigItem.getSchemeUrl());
                    }
                }
            });
            return;
        }
        if (schemeUrlCall != null) {
            schemeUrlCall.call(cacheScheme.getSchemeUrl());
        }
    }
}
