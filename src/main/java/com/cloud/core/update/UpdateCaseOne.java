package com.cloud.core.update;

import android.content.Context;

import com.cloud.core.configs.BaseBConfig;
import com.cloud.core.configs.ConfigItem;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.ResFolderType;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ResUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/26
 * @Description:更新案例1
 * @Modifier:
 * @ModifyContent:
 */
public abstract class UpdateCaseOne {

    private UpdateFlow updateFlow = null;
    private Context context = null;
    private int appIconRresId = 0;

    protected abstract void hasNewVersion(UpdateInfo updateInfo, boolean isCompulsoryUpdate);

    protected void onCompleted() {

    }

    public void onStartInstall() {

    }

    protected abstract void onPreDownloadCall();

    protected abstract void onProgressCall(double progress, DownType downType);

    public void instance(Context context, UpdateFlow updateFlow) {
        try {
            this.context = context;
            this.updateFlow = updateFlow;
            this.updateFlow.setUpdateCase(this);
            updateFlow.setOnVersionUpdateListener(versionUpdateListener);
            updateFlow.setOnDownloadListener(downloadListener);
            RxCoreConfigItems configItems = BaseBConfig.getInstance().getConfigItems(context);
            ConfigItem appIcon = configItems.getAppIcon();
            ResFolderType folderType = ResFolderType.getResFolderType(appIcon.getType());
            appIconRresId = ResUtils.getResource(context, appIcon.getName(), folderType);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    protected int getAppIconRresId() {
        return this.appIconRresId;
    }

    public Context getContext() {
        return this.context;
    }

    public UpdateFlow getUpdateFlow() {
        return updateFlow;
    }

    private OnVersionUpdateListener versionUpdateListener = new OnVersionUpdateListener() {
        @Override
        public void hasVersion(UpdateInfo updateInfo, boolean isCompulsoryUpdate) {
            try {
                if (!updateFlow.intervalPromptCheck(updateInfo)) {
                    return;
                }
                hasNewVersion(updateInfo, isCompulsoryUpdate);
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    };

    private OnDownloadListener downloadListener = new OnDownloadListener() {
        @Override
        public void onPreDownload() {
            onPreDownloadCall();
        }

        @Override
        public void onDownloadProgress(double progress, DownType downType) {
            onProgressCall(progress, downType);
        }
    };

    public void startDownloadApk(UpdateInfo updateInfo) {
        if (updateFlow == null) {
            return;
        }
        updateFlow.startDownloadApk(updateInfo);
    }
}
