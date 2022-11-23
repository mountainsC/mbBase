package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.logger.Logger;
import com.cloud.core.update.UpdateConfigItems;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.StorageUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/11
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class UpdateConfig extends BaseBConfig {

    private static UpdateConfig updateConfig = new UpdateConfig();
    private static UpdateConfigItems updateConfigItems = null;

    public static UpdateConfig getInstance() {
        return updateConfig;
    }

    public UpdateConfigItems getUpdateConfigItems(Context context) {
        if (updateConfigItems == null) {
            this.getAssetsUpdateConfigs(context);
        }
        return updateConfigItems;
    }

    private void getAssetsUpdateConfigs(Context context) {
        try {
            RxNames names = RxCloud.getInstance().getNames();
            String fileName = names.getVerUpdateConfigFileName();
            String configContent = StorageUtils.readAssetsFileContent(context, fileName);
            if (!TextUtils.isEmpty(configContent)) {
                updateConfigItems = (UpdateConfigItems) JsonUtils.parseT(configContent, UpdateConfigItems.class);
                return;
            }
        } catch (Exception e) {
            Logger.L.error(e);
            return;
        } finally {
            if (updateConfigItems == null) {
                updateConfigItems = new UpdateConfigItems();
            }
        }
    }
}
