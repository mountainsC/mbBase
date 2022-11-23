package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.RxCachePool;
import com.cloud.core.utils.StorageUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/5
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseBConfig extends BaseRConfig {

    private static BaseBConfig baseBConfig = new BaseBConfig();

    public static BaseBConfig getInstance() {
        return baseBConfig;
    }

    private <T> T getAssetsConfigs(Context context, String configName, Class<T> typeClass) {
        String configContent = StorageUtils.readAssetsFileContent(context, configName);
        if (TextUtils.isEmpty(configContent)) {
            return JsonUtils.newNull(typeClass);
        }
        T t = JsonUtils.parseT(configContent, typeClass);
        return t;
    }

    public <T> T getAssetsConfigsByName(Context context, String configName, Class<T> typeClass) {
        if (typeClass == null) {
            return null;
        }
        if (context == null || TextUtils.isEmpty(configName)) {
            return JsonUtils.newNull(typeClass);
        }
        String objectValue = RxCachePool.getInstance().getString(configName);
        if (TextUtils.isEmpty(objectValue)) {
            T configs = getAssetsConfigs(context, configName, typeClass);
            RxCachePool.getInstance().putString(configName, JsonUtils.toStr(configs));
            return configs;
        } else {
            T value = JsonUtils.parseT(objectValue, typeClass);
            if (value == null) {
                T configs = getAssetsConfigs(context, configName, typeClass);
                RxCachePool.getInstance().putString(configName, JsonUtils.toStr(configs));
                return configs;
            } else {
                return value;
            }
        }
    }
}
