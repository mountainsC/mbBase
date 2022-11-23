package com.cloud.core.update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.beans.CmdItem;
import com.cloud.core.dialog.BaseMessageBox;
import com.cloud.core.enums.DialogButtonsEnum;
import com.cloud.core.enums.MsgBoxClickButtonEnum;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.NetworkUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.RxCachePool;
import com.cloud.core.utils.ToastUtils;
import com.cloud.core.utils.ValidUtils;


/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/26
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class DefaultUpdateCase extends UpdateCaseOne {
    private static DefaultUpdateCase defaultUpdateCase = null;
    private VersionUpdateViewHolder holder = null;
    private boolean isCompulsoryUpdate = false;
    private int NOTIFY_ID = 1200462805;
    private String UPDATE_DIALOG_ID = "708996907";
    private String AGAIN_UPDATE_DIALOG_ID = "353061030";
    private UpdateInfo updateInfo = null;

    public static DefaultUpdateCase getInstance() {
        return defaultUpdateCase == null ? defaultUpdateCase = new DefaultUpdateCase() : defaultUpdateCase;
    }

    private interface VersionUpdateStatus {
        public final int START_DOWNLOAD = 1617859133;
        public final int DOWNLOADING = 1772750794;
    }

    private BaseMessageBox vubox = new BaseMessageBox() {
        @Override
        public boolean onItemClickListener(View v, String cmdid, String target, Object extraData) {
            if (TextUtils.equals(target, "update_step_1")) {
                if (TextUtils.equals(cmdid, "lastUpdate")) {
                    getUpdateFlow().intervalPromptInit();
                    return true;
                } else if (TextUtils.equals(cmdid, "nowUpdate")) {
                    if (extraData instanceof UpdateInfo) {
                        UpdateInfo updateInfo = (UpdateInfo) extraData;
                        nowUpdate(v.getContext(), updateInfo);
                    }
                }
            }
            return false;
        }
    };

    private BaseMessageBox wifibox = new BaseMessageBox() {
        @Override
        public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum, String target, Object extraData) {
            if (TextUtils.equals(target, "UPDATE_NETWORK_REMIND")) {
                if (mcbenum == MsgBoxClickButtonEnum.Yes) {
                    //重新显示更新提示并开始下载
                    if (extraData instanceof UpdateInfo) {
                        UpdateInfo updateInfo = (UpdateInfo) extraData;
                        if (updateInfo.getDownloadType() == DownType.Dialog.getValue()) {
                            remindUpdate(UPDATE_DIALOG_ID, updateInfo, isCompulsoryUpdate, true);
                        }
                        getInstance().startDownloadApk(updateInfo);
                    }
                }
            }
            return true;
        }
    };

    private void nowUpdate(Context context, UpdateInfo updateInfo) {
        try {
            if (updateInfo == null ||
                    TextUtils.isEmpty(updateInfo.getApkUrl()) ||
                    !ValidUtils.valid(RuleParams.Url.getValue(), updateInfo.getApkUrl())) {
                ToastUtils.showLong(context, "应用包下载地址错误");
                vubox.dismiss();
                return;
            }
            vubox.setEnabled(0, false);
            vubox.setEnabled(1, false);
            if (NetworkUtils.getConnectedType(context) == ConnectivityManager.TYPE_WIFI) {
                holder.getUpdateHandler().obtainMessage(VersionUpdateStatus.START_DOWNLOAD).sendToTarget();
                getInstance().startDownloadApk(updateInfo);
                if (updateInfo.getDownloadType() == DownType.Notify.getValue()) {
                    vubox.dismiss();
                }
            } else {
                vubox.dismiss();
                wifibox.setTitle(String.format("%s-更新", updateInfo.getApkName()));
                wifibox.setContentGravity(Gravity.CENTER);
                wifibox.setContent("当前网络非wifi状态,是否仍继续更新?");
                wifibox.setShowTitle(true);
                wifibox.setShowClose(false);
                wifibox.setCancelable(false);
                wifibox.setTarget("UPDATE_NETWORK_REMIND", updateInfo);
                wifibox.setDialogId("1480122000");
                wifibox.setPadding(true);
                wifibox.show(context, DialogButtonsEnum.YesNo);
            }
        } catch (Exception e) {
            Logger.L.error(e);
            vubox.dismiss();
            wifibox.dismiss();
        }
    }

    @Override
    protected void hasNewVersion(UpdateInfo updateInfo, boolean isCompulsoryUpdate) {
        this.isCompulsoryUpdate = isCompulsoryUpdate;
        this.updateInfo = updateInfo;
        RxCachePool.getInstance().putBoolean("HAS_NEW_VERSION", true);
        remindUpdate(UPDATE_DIALOG_ID, updateInfo, isCompulsoryUpdate, false);
    }

    private void remindUpdate(String dialogId, UpdateInfo updateInfo, boolean isCompulsoryUpdate, boolean isOnlyDownload) {
        try {
            vubox.setTitle(String.format("%s-更新", updateInfo.getApkName()));
            vubox.setShowClose(false);
            vubox.setShowTitle(true);
            holder = new VersionUpdateViewHolder();
            holder.setContent(updateInfo.getUpdateDescription());
            vubox.setContentView(holder.getContentView());
            vubox.setContentGravity(Gravity.LEFT);
            if (isCompulsoryUpdate) {
                vubox.setButtons(new CmdItem[]{
                        new CmdItem("nowUpdate", "立即更新")
                });
            } else {
                vubox.setButtons(new CmdItem[]{
                        new CmdItem("lastUpdate", "稍候更新"),
                        new CmdItem("nowUpdate", "立即更新")
                });
            }
            vubox.setCancelable(false);
            vubox.setTarget("update_step_1", updateInfo);
            vubox.setDialogId(dialogId);
            vubox.setPadding(false);
            vubox.setContentPadding(PixelUtils.dip2px(getContext(), 6), 0, PixelUtils.dip2px(getContext(), 6), 0);
            vubox.show(getContext(), DialogButtonsEnum.Custom);
            if (isOnlyDownload) {
                vubox.setEnabled(0, false);
                vubox.setEnabled(1, false);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private class VersionUpdateViewHolder {

        private View contentView = null;
        private TextView versionUpdateContentTv = null;
        private RelativeLayout updateProgressRl = null;
        private ProgressBar updateProgressBar = null;
        private TextView updatePercentageTv = null;


        public View getContentView() {
            return contentView;
        }

        public VersionUpdateViewHolder() {
            contentView = View.inflate(getContext(), R.layout.version_update_progress_view, null);
            versionUpdateContentTv = (TextView) contentView.findViewById(R.id.version_update_content_tv);
            updateProgressRl = (RelativeLayout) contentView.findViewById(R.id.update_progress_rl);
            updateProgressBar = (ProgressBar) contentView.findViewById(R.id.update_progress_bar);
            updatePercentageTv = (TextView) contentView.findViewById(R.id.update_percentage_tv);
        }

        public void setContent(String content) {
            if (versionUpdateContentTv == null) {
                return;
            }
            versionUpdateContentTv.setText(content);
        }

        public Handler getUpdateHandler() {
            return updateHandler;
        }

        private Handler updateHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case VersionUpdateStatus.START_DOWNLOAD:
                        updateProgressRl.setVisibility(View.VISIBLE);
                        updateProgressBar.setProgress(0);
                        updatePercentageTv.setText("0%");
                        break;
                    case VersionUpdateStatus.DOWNLOADING:

                        break;
                }
            }
        };
    }

    private Handler progressHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private class ProgressRunnable implements Runnable {

        double progress = 0;

        public ProgressRunnable(double progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            int value = (int) progress;
            holder.updateProgressBar.setProgress(value);
            holder.updatePercentageTv.setText(value + "%");
        }
    }

    @Override
    protected void onPreDownloadCall() {

    }

    @Override
    protected void onProgressCall(final double progress, DownType downType) {
        progressHandler.postDelayed(new ProgressRunnable(progress), 10);
    }

    @Override
    protected void onCompleted() {
        super.onCompleted();
        vubox.dismiss();
        wifibox.dismiss();
    }

    @Override
    public void onStartInstall() {
        if (updateInfo != null) {
            remindUpdate(AGAIN_UPDATE_DIALOG_ID, updateInfo, isCompulsoryUpdate, false);
        }
    }
}
