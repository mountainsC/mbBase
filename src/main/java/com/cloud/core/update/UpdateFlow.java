/**
 * @Title: UpdateBLL.java
 * @Description: apk更新业务处理
 * @author: lijinghuan
 * @data: 2015年4月30日 上午11:59:11
 */
package com.cloud.core.update;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Message;
import android.text.TextUtils;

import com.cloud.core.bases.BaseApplication;
import com.cloud.core.beans.DeviceInfo;
import com.cloud.core.configs.ConfigItem;
import com.cloud.core.configs.UpdateConfig;
import com.cloud.core.constants.Updatekeys;
import com.cloud.core.dialog.LoadingDialog;
import com.cloud.core.enums.RequestState;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action3;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.OkRxManager;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.AppInfoUtils;
import com.cloud.core.utils.BaseCommonUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.NetworkUtils;
import com.cloud.core.utils.SharedPrefUtils;
import com.cloud.core.utils.StorageUtils;
import com.cloud.core.utils.ToastUtils;
import com.cloud.core.utils.ValidUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UpdateFlow<T extends UpdateCaseOne> {

    private boolean CheckCompleteFlag = true;
    private VersionUpdateProperties versionUpdateProperties = new VersionUpdateProperties();
    private LoadingDialog mloading = new LoadingDialog();
    //检测次数
    private int checkCount = 0;
    private Message msgh = new Message();
    private OnDownloadListener onDownloadListener = null;
    private OnVersionUpdateListener onVersionUpdateListener = null;
    private T updateCase = null;
    public int INSTALL_REQUEST_CODE = 5079;

    public void setUpdateCase(T updateCase) {
        this.updateCase = updateCase;
    }

    private void onCheckCompleted() {
        BaseCommonUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mloading != null) {
                    mloading.dismiss();
                }
            }
        });
        checkCount = 0;
        CheckCompleteFlag = true;
    }

    public boolean isCheckComplete() {
        return CheckCompleteFlag;
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        this.onDownloadListener = listener;
    }

    public void setOnVersionUpdateListener(OnVersionUpdateListener listener) {
        this.onVersionUpdateListener = listener;
    }

    public File getApkDir() {
        return StorageUtils.getApksDir();
    }

    public String getApkFileName(String packageName) {
        return String.format("%s14s4db.apk", packageName);
    }

    /**
     * 检测版本更新
     *
     * @param properties 版本更新属性
     */
    public void checkVersionUpdate(VersionUpdateProperties properties) {
        this.versionUpdateProperties = properties;
        //判断版本检测是否完成
        if (CheckCompleteFlag) {
            try {
                CheckCompleteFlag = false;
                //防止并发执行2次以上
                checkCount++;
                if (checkCount > 1) {
                    checkCount = 0;
                    onCheckCompleted();
                    return;
                }
                DefaultUpdateCase.getInstance().instance(properties.getActivity(), this);
                UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(properties.getActivity());
                ConfigItem versionCheck = configItems.getAppVersionCheck();
                if (versionCheck.isState()) {
                    UpdateConfigItems updateConfigItems = UpdateConfig.getInstance().getUpdateConfigItems(properties.getActivity());
                    if (!properties.getIsAutoUpdate()) {
                        //非自动检测显示loading提醒
                        mloading.showDialog(properties.getActivity(),
                                updateConfigItems.getUpdatePrompt().getCheckLoading(),
                                null);
                    }
                    if (TextUtils.isEmpty(properties.getCheckUpdateUrl())) {
                        onCheckCompleted();
                    } else {
                        OkRxManager.getInstance().get(properties.getActivity(),
                                properties.getCheckUpdateUrl(),
                                properties.getHttpHeaders(),
                                properties.getHttpParams(),
                                false,
                                "",
                                0,
                                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                                    @Override
                                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                                        BaseCommonUtils.post(new UpdateInfoDealwithAndStart(response));
                                    }
                                },
                                null,
                                null,
                                new Action1<RequestState>() {
                                    @Override
                                    public void call(RequestState requestState) {
                                        if (requestState == RequestState.Completed) {
                                            onCheckCompleted();
                                        }
                                    }
                                }, null, "", null);
                    }
                } else {
                    onCheckCompleted();
                }
            } catch (Exception e) {
                onCheckCompleted();
            }
        }
    }

    private class UpdateInfoDealwithAndStart implements Runnable {

        private String response = "";

        public UpdateInfoDealwithAndStart(String response) {
            this.response = response;
        }

        @Override
        public void run() {
            try {
                UpdateInfo updateInfo = JsonUtils.parseT(response, UpdateInfo.class);
                if (updateInfo == null) {
                    return;
                }
                UpdateInfo result = updateInfo.getData();
                if (result == null) {
                    return;
                }
                result.setPackageName(BaseApplication.getInstance().getPackageName());
                onCheckAppUpdate(result);
            } catch (Exception e) {
                onCheckCompleted();
            }
        }
    }

    private void onCheckAppUpdate(UpdateInfo updateInfo) {
        try {
            Activity activity = versionUpdateProperties.getActivity();
            UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
            if (NetworkUtils.isConnected(activity)) {
                updateCheck(updateInfo, configItems);
            } else {
                ToastUtils.showLong(activity, configItems.getUpdatePrompt().getNoNetworkPrompt());
            }
        } catch (Exception e) {
            onCheckCompleted();
        }
    }

    //获取当前应用版本
    private int getLocalVersionCode() {
        try {
            BaseApplication application = BaseApplication.getInstance();
            PackageInfo packageInfo = AppInfoUtils.getPackageInfo(application);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    private void updateCheck(UpdateInfo updateInfo, UpdateConfigItems configItems) {
        Activity activity = versionUpdateProperties.getActivity();
        int versionCode = getLocalVersionCode();
        if (updateInfo.getVersionCode() > versionCode) {
            //若服务器端版本大于当前版本表示有更新
            if (updateInfo.isEnablePart()) {
                //一般正式发布所有用户前做灰度更新用
                if (TextUtils.isEmpty(updateInfo.getUpdateUsers())) {
                    //若为局部更新且更新用户空时则结束更新
                    onCheckCompleted();
                    return;
                }
                DeviceInfo mdinfo = AppInfoUtils.getDeviceInfo(activity);
                List<String> separators = configItems.getUpdateUsersSeparators();
                String splits = "";
                for (String splitItem : separators) {
                    splits += String.format("|%s", splitItem);
                }
                if (splits.length() > 0) {
                    splits = splits.substring(1);
                }
                String[] uuas = updateInfo.getUpdateUsers().split(splits);
                List<String> uulst = Arrays.asList(uuas);
                boolean flag = uulst.contains(mdinfo.getImei());
                //包含在灰度更新中则提示更新;反之结束更新;
                if (flag) {
                    if (onVersionUpdateListener != null) {
                        if (updateInfo.getUpdateType() == 1 || versionCode < updateInfo.getMinVersionCode()) {
                            onVersionUpdateListener.hasVersion(updateInfo, true);
                        } else {
                            onVersionUpdateListener.hasVersion(updateInfo, false);
                        }
                    }
                } else {
                    onCheckCompleted();
                    return;
                }
            } else {
                if (onVersionUpdateListener != null) {
                    if (updateInfo.getUpdateType() == 1 || versionCode < updateInfo.getMinVersionCode()) {
                        onVersionUpdateListener.hasVersion(updateInfo, true);
                    } else {
                        onVersionUpdateListener.hasVersion(updateInfo, false);
                    }
                }
            }
        } else {
            if (!versionUpdateProperties.getIsAutoUpdate()) {
                ToastUtils.showLong(activity, configItems.getUpdatePrompt().getLastVersionPrompt());
            }
        }
    }

    public void startDownloadApk(UpdateInfo updateInfo) {
        Activity activity = versionUpdateProperties.getActivity();
        if (ValidUtils.valid(RuleParams.Url.getValue(), updateInfo.getApkUrl())) {
            downloadApk(updateInfo);
        } else {
            UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
            ToastUtils.showLong(activity, configItems.getUpdatePrompt().getApkAddressErrorPrompt());
        }
    }

    /**
     * 下载apk
     *
     * @param updateInfo 版本更新信息
     */
    private void downloadApk(UpdateInfo updateInfo) {
        downloadApk(updateInfo, false);
    }

    private void downloadApk(final UpdateInfo updateInfo, final boolean isSilent) {
        try {
            Activity activity = versionUpdateProperties.getActivity();
//            if (SharedPrefUtils.getPrefBoolean(activity, Updatekeys.IS_DOWNLOADING_KEY)) {
//                return;
//            }
            SharedPrefUtils.setPrefBoolean(activity, Updatekeys.IS_DOWNLOADING_KEY, true);
            String fileName = getApkFileName(updateInfo.getPackageName());
            File apkFile = new File(getApkDir(), fileName);
            if (apkFile.exists()) {
                apkFile.delete();
            }
            apkFile.createNewFile();
            if (onDownloadListener != null) {
                onDownloadListener.onPreDownload();
            }
            if (updateInfo.getDownloadType() == DownType.Notify.getValue()) {
                BaseCommonUtils.downApp(activity, updateInfo.getApkUrl(), "下载择机汇应用");
                return;
            }
            OkRxManager.getInstance().download(activity,
                    updateInfo.getApkUrl(),
                    null,
                    null,
                    apkFile,
                    new Action1<Float>() {
                        @Override
                        public void call(Float progress) {
                            if (progress == null || isSilent) {
                                return;
                            }
                            if (onDownloadListener != null) {
                                onDownloadListener.onDownloadProgress(progress * 100,
                                        updateInfo.getDownloadType() == 1 ? DownType.Dialog : DownType.Notify);
                            }
                        }
                    },
                    new Action1<File>() {
                        @Override
                        public void call(final File file) {
                            BaseCommonUtils.post(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPrefUtils.setPrefBoolean(versionUpdateProperties.getActivity(),
                                            Updatekeys.IS_DOWNLOADING_KEY, false);
                                    if (!isSilent) {
                                        installApk(file, versionUpdateProperties.getActivity());
                                    }
                                }
                            });
                        }
                    }, new Action1<RequestState>() {
                        @Override
                        public void call(final RequestState requestState) {
                            BaseCommonUtils.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestState == RequestState.Error) {
                                        Activity activity = versionUpdateProperties.getActivity();
                                        UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
                                        ToastUtils.showLong(activity, configItems.getUpdatePrompt().getApkAddressErrorPrompt());
                                    } else {
                                        if (!isSilent) {
                                            onCheckCompleted();
                                            if (updateCase != null) {
                                                updateCase.onCompleted();
                                            }
                                        }
                                        SharedPrefUtils.setPrefBoolean(versionUpdateProperties.getActivity(), Updatekeys.IS_DOWNLOADING_KEY, false);
                                    }
                                }
                            });
                        }
                    },
                    "",
                    null);
        } catch (Exception e) {
            onCheckCompleted();
            SharedPrefUtils.setPrefBoolean(versionUpdateProperties.getActivity(), Updatekeys.IS_DOWNLOADING_KEY, false);
        }
    }

    public void againInstall() {
        if (updateCase != null) {
            updateCase.onStartInstall();
        }
    }

    public void installApk(File apkFile, Activity activity) {
        try {
            Intent installIntent = GlobalUtils.getInstallIntent(apkFile.getAbsolutePath());
            activity.startActivityForResult(installIntent, INSTALL_REQUEST_CODE);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 如果版本检测中有不再提醒按钮或稍候更新按钮则点击后需要调用此方法
     */
    public void intervalPromptInit() {
        SharedPrefUtils.setPrefLong(versionUpdateProperties.getActivity(), Updatekeys.TIME_INTERVAL_KEY, System.currentTimeMillis());
        onCheckCompleted();
    }

    /**
     * 在版本检测时需要调用此方法,用于一段时间不提醒;
     * 一般与intervalPromptInit一起使用
     *
     * @return true:继续版本更新流程;false:以return方式结束更新流程;
     */
    public boolean intervalPromptCheck(UpdateInfo updateInfo) {
        if (updateInfo == null) {
            return false;
        }
        Activity activity = versionUpdateProperties.getActivity();
        long pretime = SharedPrefUtils.getPrefLong(activity, Updatekeys.TIME_INTERVAL_KEY);
        long currtime = System.currentTimeMillis();
        long difftime = (currtime - pretime) / 1000;
        UpdateConfigItems configItems = UpdateConfig.getInstance().getUpdateConfigItems(activity);
        if (TextUtils.equals(configItems.getUpdatePrompt().getType(), "No_LONGER_DISPLAY")) {
            if (difftime < configItems.getUpdatePrompt().getTimeInterval()) {
                //如果类型No_LONGER_DISPLAY且在不提醒的时间范围内则直接返回
                //如果difftime-configItems.getUpdatePrompt().getTimeInterval()
                // 大于总时间间隔的1/4秒则开启静默下载
//                long dtime = Math.abs(configItems.getUpdatePrompt().getTimeInterval() - difftime);
//                if (dtime >= 10) {
//                    downloadApk(updateInfo, true);
//                }
//                return false;
            }
        }
        return true;
    }
}
