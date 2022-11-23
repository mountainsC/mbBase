package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.logger.Logger;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.StorageUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/4
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseCConfig {

    private static RxCoreConfigItems coreConfigItems = null;
    private static ToastThemeProperties toastThemeProperties = null;
    private static BaseCConfig baseCConfig = new BaseCConfig();

    public static BaseCConfig getInstance() {
        return baseCConfig;
    }

    /**
     * 获取核心包配置项
     *
     * @param context  上下文
     * @param listener 获取url监听
     * @return
     */
    public RxCoreConfigItems getConfigItems(Context context) {
        if (coreConfigItems == null) {
            getAssetsConfigsByRxCore(context);
        }
        return coreConfigItems;
    }

    private void getAssetsConfigsByRxCore(Context context) {
        try {
            RxNames names = RxCloud.getInstance().getNames();
            String fileName = names.getCoreConfigFileName();
            String configContent = StorageUtils.readAssetsFileContent(context, fileName);
            if (TextUtils.isEmpty(configContent)) {
                return;
            }
            coreConfigItems = JsonUtils.parseT(configContent, RxCoreConfigItems.class);
        } catch (Exception e) {
            Logger.L.error(e);
        } finally {
            if (coreConfigItems == null) {
                coreConfigItems = new RxCoreConfigItems();
            }
        }
    }

    public ToastThemeProperties getToastThemeProperties(Context context) {
        if (toastThemeProperties == null) {
            try {
                RxNames names = RxCloud.getInstance().getNames();
                String fileName = names.getToastConfigFileName();
                String configContent = StorageUtils.readAssetsFileContent(context, fileName);
                if (!TextUtils.isEmpty(configContent)) {
                    toastThemeProperties = JsonUtils.parseT(configContent, ToastThemeProperties.class);
                }
            } catch (Exception e) {
                Logger.L.error(e);
            } finally {
                if (toastThemeProperties == null) {
                    toastThemeProperties = new ToastThemeProperties();
                }
            }
        }
        return toastThemeProperties;
    }
}
