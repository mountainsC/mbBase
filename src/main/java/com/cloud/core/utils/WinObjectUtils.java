package com.cloud.core.utils;

import android.app.Activity;
import com.cloud.core.ObjectJudge;
import com.cloud.core.bases.BaseApplication;
import com.cloud.core.configs.BaseBConfig;
import com.cloud.core.configs.ConfigItem;
import com.cloud.core.configs.OnConfigItemUrlListener;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.configs.UpdateConfig;
import com.cloud.core.enums.ResFolderType;
import com.cloud.core.update.UpdateConfigItems;
import com.cloud.core.update.UpdateFlow;
import com.cloud.core.update.VersionUpdateProperties;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016-2-1 上午11:27:15
 * @Description: 用于BaseActivity、BaseFragment、BaseFragmentActivity、
 * BaseGestureActivity共用方法或变量定义
 * @Modifier:
 * @ModifyContent:
 */
public class WinObjectUtils {

    private UpdateFlow updateFlow = new UpdateFlow();

    public UpdateFlow getUpdateFlow() {
        return updateFlow;
    }

    /**
     * 在页面进入onResume时回调
     *
     * @param activity activity
     */
    public void onResume(Activity activity) {
        String className = activity.getLocalClassName();
        int sindex = className.indexOf("ui.");
        if (sindex >= 0) {
            className = className.substring(sindex + 3);
        } else {
            className = className.substring(className.lastIndexOf(".") + 1);
        }
        UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
        if (!ObjectJudge.isNullOrEmpty(configItems.getUpdateCheckActivityNames())) {
            if (configItems.getUpdateCheckActivityNames().contains(className)) {
                checkVersionUpdate(activity);
            }
        }
    }

    private void checkVersionUpdate(Activity activity) {
        if (updateFlow.isCheckComplete()) {
            VersionUpdateProperties properties = new VersionUpdateProperties();
            properties.setActivity(activity);
            RxCoreConfigItems configItems = BaseBConfig.getInstance().getConfigItems(activity);
            UpdateConfigItems updateConfigItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
            ConfigItem versionCheck = updateConfigItems.getAppVersionCheck();
            if (versionCheck.isState()) {
                Object urlListener = RxCachePool.getInstance().getObject(versionCheck.getUrlType());
                if (urlListener != null && urlListener instanceof OnConfigItemUrlListener) {
                    OnConfigItemUrlListener listener = (OnConfigItemUrlListener) urlListener;
                    String url = listener.getUrl(versionCheck.getUrlType());
                    ConfigItem appIcon = configItems.getAppIcon();
                    ResFolderType folderType = ResFolderType.getResFolderType(appIcon.getType());
                    int appIconRresId = ResUtils.getResource(activity, appIcon.getName(), folderType);
                    properties.setAppIcon(appIconRresId);
                    properties.setIsAutoUpdate(true);
                    properties.setCheckUpdateUrl(url);
                    //添加头信息
                    properties.getHttpHeaders().put("Device-type", "android");
                    //添加参数
                    Object metaObject = AppInfoUtils.getMetaObject(BaseApplication.getInstance(), "UMENG_CHANNEL");
                    if (metaObject != null) {
                        properties.getHttpParams().put("channel", String.valueOf(metaObject));
                    }
                    updateFlow.checkVersionUpdate(properties);
                }
            }
        }
    }
}
