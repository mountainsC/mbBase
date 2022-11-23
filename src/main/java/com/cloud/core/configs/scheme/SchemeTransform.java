package com.cloud.core.configs.scheme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cloud.core.beans.ProviderItem;
import com.cloud.core.configs.BuildConfig;
import com.cloud.core.configs.UserDataCache;
import com.cloud.core.configs.scheme.jumps.GoConfigItem;
import com.cloud.core.configs.scheme.jumps.GoPagerUtils;
import com.cloud.core.enums.ActivityNames;
import com.cloud.core.events.Action0;
import com.cloud.core.events.Action1;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.RedirectUtils;
import com.cloud.core.utils.RoutesUtils;
import com.cloud.core.utils.RxCachePool;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class SchemeTransform implements Runnable {

    private Context context = null;
    private ProviderItem providerItem = null;
    private boolean isResultCode = false;

    public SchemeTransform(Context context, ProviderItem providerItem, boolean isResultCode) {
        this.context = context;
        this.providerItem = providerItem;
        this.isResultCode = isResultCode;
    }

    @Override
    public void run() {
        try {
            CacheConfigAction configAction = new CacheConfigAction();
            configAction.setGoParamList(providerItem.getParamList());
            Uri schemeUri = Uri.parse(providerItem.getScheme());
            if (BuildConfig.getInstance().isModule(context)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(schemeUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //移除缓存中的跳转标记
                RxCachePool.getInstance().clearObject(providerItem.getActivityClassName());
            }
            GoPagerUtils pagerUtils = new GoPagerUtils();
            pagerUtils.getGoConfigByScheme(context,
                    schemeUri,
                    BuildConfig.getInstance().getSchemeHost(context),
                    !UserDataCache.getDefault().isEmptyCache(context),
                    new Action1<GoConfigItem>() {
                        @Override
                        public void call(GoConfigItem goConfigItem) {
                            GoPagerUtils pagerUtils = new GoPagerUtils();
                            //获取bundle
                            Bundle bundle = pagerUtils.getBundleByConfigItem(context, goConfigItem);
                            //获取全类名
                            Context applicationContext = context.getApplicationContext();
                            String packageName = applicationContext.getPackageName();
                            String fullClassName = pagerUtils.getActivityFullClassName(context, packageName, providerItem.getActivityClassName());
                            //转换目标
                            if (isResultCode && (context instanceof Activity) && providerItem.getRequestCode() > 0) {
                                RedirectUtils.startPkgActivityForResult((Activity) context, packageName, fullClassName, bundle, providerItem.getRequestCode());
                            } else {
                                RedirectUtils.startActivity(context, fullClassName, bundle);
                            }
                        }
                    },
                    null,
                    configAction,
                    new Action0() {
                        @Override
                        public void call() {
                            RoutesUtils.startActivity(context, ActivityNames.LoginActivity, null);
                        }
                    },
                    new Action0() {
                        @Override
                        public void call() {
                            //移除缓存中的跳转标记
                            RxCachePool.getInstance().clearObject(providerItem.getActivityClassName());
                        }
                    });
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
}
