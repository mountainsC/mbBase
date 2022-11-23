package com.cloud.core.configs.scheme.jumps;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.core.beans.BasicConfigBean;
import com.cloud.core.beans.GoConfigListBean;
import com.cloud.core.configs.APIConfigProcess;
import com.cloud.core.configs.BuildConfig;
import com.cloud.core.enums.ActivityNames;
import com.cloud.core.enums.H5LoadType;
import com.cloud.core.utils.RoutesUtils;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.PathsUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/9
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class GoPagerUtils extends GoPagerTransformer {

    /**
     * 根据配置信息启动Activity
     *
     * @param activity         当前窗口
     * @param t                配置数据项
     * @param isCheckGoVersion true-如果currVersionName为空不检测否则需检测版本号;
     *                         false-不管currVersionName是否为空均不检测版本号;
     * @param <T>              数据类型
     */
    public <T extends BaseGoBean> void startActivity(final Activity activity, final T t, boolean isCheckGoVersion) {
        //模块开发时此参数值为空
        String currVersionName = BuildConfig.getInstance().getBuildString(activity, "currVersionName");
        //true-如果currVersionName为空不检测否则需检测版本号;
        //false-不管currVersionName是否为空均不检测版本号;
        isCheckGoVersion = isCheckGoVersion && (TextUtils.isEmpty(currVersionName) ? false : true);
        super.startActivity(activity, t, currVersionName, isCheckGoVersion, new OnH5Callback() {
            @Override
            public void onCallback(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("URL", url);
                bundle.putInt("isHideShare", 1);
                bundle.putInt("isLevelReturn", 0);
                bundle.putInt("type", H5LoadType.UrlType.ordinal());
                RoutesUtils.startActivity(activity, ActivityNames.H5WebViewActivity, bundle);
            }
        }, new OnApiCallbackSuccessful() {
            @Override
            public String onGetConfigs(String apiResult) {
                GoConfigListBean configListBean = JsonUtils.parseT(apiResult, GoConfigListBean.class);
                if (configListBean == null || configListBean.getData() == null) {
                    return "";
                }
                return configListBean.getData().getConfigs();
            }
        }, new OnProtozoaCallback() {
            @Override
            public void onCallback(String pageName, Bundle bundle) {
                ActivityNames activityNames = ActivityNames.getActivityNames(pageName);
                RoutesUtils.startActivity(activity, activityNames, bundle);
            }
        }, new OnRequestConfigUrl() {
            @Override
            public String onUrl(String uniqueTag) {
                BasicConfigBean configBean = APIConfigProcess.getInstance().getBasicConfigBean(activity);
                return PathsUtils.combine(configBean.getApiUrl(),
                        "gatherPages/getGatherPagesAppSetting",
                        uniqueTag);
            }
        }, new OnGoReceiveParamsCallback() {
            @Override
            public GoConfigItem onGoReceiveParams(GoConfigItem goConfigItem) {
                return getGoConfigParams(t, goConfigItem);
            }
        });
    }

    /**
     * 根据配置信息启动Activity
     *
     * @param activity 当前窗口
     * @param t        配置数据项
     * @param <T>      数据类型
     */
    public <T extends BaseGoBean> void startActivity(Activity activity, T t) {
        this.startActivity(activity, t, true);
    }
}
