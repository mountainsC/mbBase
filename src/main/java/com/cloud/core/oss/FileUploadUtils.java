package com.cloud.core.oss;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.blankj.utilcode.util.FileIOUtils;
import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.api.services.AgreementService;
import com.cloud.core.beans.ALiYunOssRole;
import com.cloud.core.beans.BaseDataBean;
import com.cloud.core.dialog.BaseMessageBox;
import com.cloud.core.dialog.LoadingDialog;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.enums.DateFormatEnum;
import com.cloud.core.enums.DialogButtonsEnum;
import com.cloud.core.enums.MsgBoxClickButtonEnum;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.events.OnSuccessfulListener;
import com.cloud.core.piceditors.OnUploadCompletedListener;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.DateUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.NetworkUtils;
import com.cloud.core.utils.PathsUtils;
import com.cloud.core.utils.ToastUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/4/13
 * @Description:文件上传工具类
 * @Modifier:
 * @ModifyContent:
 */
abstract class FileUploadUtils {

    private LoadingDialog mloading = new LoadingDialog();
    private final int START_SHOW_UPLOAD = 1308754678;
    private final int DISMISS_LOADING = 1311980761;
    private final int SHOW_UPLOADING = 825700957;
    private final int UPLOADING_WITH = 476990333;
    private final String UPLOAD_FILE_TAG = "2021553808";
    private final int UPLOAD_NOT_NETWORK_FLAG = 270292957;
    private List<OssUploadFileItem> fileItems = null;
    private String uploadDisplayText = "";
    private Object extra = null;
    private final String UPLOAD_DIALOG_ID = "1820554346";
    private DialogPlus dialogPlus = null;
    private Activity activity = null;
    ALiYunOssRole aLiYunOssRole = new ALiYunOssRole();
    private long startRequestRoleTime = 0;
    private int count = 0;
    private int currUploadCount = 0;
    //是否显示上传进度
    private boolean isShowUploadProgress = true;
    private OnUploadCompletedListener onUploadCompletedListener = null;

    public void setOnUploadCompletedListener(OnUploadCompletedListener onUploadCompletedListener) {
        this.onUploadCompletedListener = onUploadCompletedListener;
    }

    protected void onUploadProgress(float progress, String uploadKey) {

    }

    protected abstract void onUploadSuccess(int position, String relativeUrl, String uploadKey, Object extra);

    protected void onCompleted() {

    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCurrUploadCount(int currUploadCount) {
        this.currUploadCount = currUploadCount;
    }

    public void setShowUploadProgress(boolean isShowUploadProgress) {
        this.isShowUploadProgress = isShowUploadProgress;
    }

    //检查上传环境
    private void upload(String fileName,
                        File targetFile,
                        String uploadDisplayText,
                        String assumeRoleUrl,
                        String uploadTypeFlag,
                        String uploadKey,
                        int position,
                        Object extra) {
        if (TextUtils.isEmpty(fileName) || targetFile == null) {
            return;
        }
        this.uploadDisplayText = uploadDisplayText;
        FileUploadParam fileUploadParam = new FileUploadParam();
        fileUploadParam.fileName = fileName;
        fileUploadParam.targetFile = targetFile;
        fileUploadParam.assumeRoleUrl = assumeRoleUrl;
        fileUploadParam.uploadTypeFlag = uploadTypeFlag;
        fileUploadParam.uploadKey = uploadKey;
        fileUploadParam.position = position;
        fileUploadParam.originalFileName = targetFile.getName();
        //非wifi状态提醒
        if (NetworkUtils.getConnectedType(activity) == ConnectivityManager.TYPE_WIFI ||
                isShowUploadProgress == false) {
            uploadFilePrepare(fileUploadParam);
        } else {
            msgbox.setShowTitle(true);
            msgbox.setShowClose(false);
            msgbox.setTitle("网络提醒");
            msgbox.setContentGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            msgbox.setContent("当前非wifi状态;确定要继续上传吗?");
            msgbox.setTarget("WIFI_REMIND_TAG", fileUploadParam);
            msgbox.show(activity, DialogButtonsEnum.ConfirmCancel);
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void upload(String fileName,
                       File targetFile,
                       String uploadDisplayText,
                       String assumeRoleUrl,
                       String uploadKey,
                       int position) {
        upload(fileName, targetFile, uploadDisplayText, assumeRoleUrl, "NORMAL", uploadKey, position, extra);
    }

    public void upload(String fileName,
                       File targetFile,
                       String uploadDisplayText,
                       String assumeRoleUrl,
                       String uploadKey) {
        upload(fileName, targetFile, uploadDisplayText, assumeRoleUrl, "NORMAL", uploadKey, 0, extra);
    }

    //一般的上传
    public void upload(String fileName,
                       File targetFile,
                       String uploadDisplayText,
                       String assumeRoleUrl) {
        upload(fileName, targetFile, uploadDisplayText, assumeRoleUrl, "NORMAL", "", 0, extra);
    }

    //断点续传
    public void breakPointUpload(String fileName,
                                 File targetFile,
                                 String uploadDisplayText,
                                 String assumeRoleUrl,
                                 String uploadKey) {
        upload(fileName,
                targetFile,
                uploadDisplayText,
                assumeRoleUrl,
                "BREAKPOINT",
                uploadKey,
                0,
                extra);
    }

    public void breakPointUpload(String fileName,
                                 File targetFile,
                                 String uploadDisplayText,
                                 String assumeRoleUrl) {
        upload(fileName,
                targetFile,
                uploadDisplayText,
                assumeRoleUrl,
                "BREAKPOINT",
                "",
                0,
                extra);
    }

    //追加续传
    public void appendUpload(String fileName,
                             File targetFile,
                             String uploadDisplayText,
                             String assumeRoleUrl,
                             String uploadKey) {
        upload(fileName,
                targetFile,
                uploadDisplayText,
                assumeRoleUrl,
                "APPEND",
                uploadKey,
                0,
                extra);
    }

    public void appendUpload(String fileName,
                             File targetFile,
                             String uploadDisplayText,
                             String assumeRoleUrl) {
        upload(fileName,
                targetFile,
                uploadDisplayText,
                assumeRoleUrl,
                "APPEND",
                "",
                0,
                extra);
    }

    private class FileUploadParam {
        public String fileName = "";
        public String originalFileName = "";
        public File targetFile = null;
        public String assumeRoleUrl = "";
        /**
         * 上传方式:NORMAL;BREAKPOINT;APPEND
         */
        public String uploadTypeFlag = "";
        //本次上传键
        public String uploadKey = "";
        public int position = 0;

    }

    private BaseMessageBox msgbox = new BaseMessageBox() {
        @Override
        public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum, String target, Object extraData) {
            if (TextUtils.equals(target, "WIFI_REMIND_TAG")) {
                if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                    uploadFilePrepare((FileUploadParam) extraData);
                }
            }
            return true;
        }
    };

    private void uploadFilePrepare(FileUploadParam fileUploadParam) {
        //设置上传显示文本
        if (isShowUploadProgress) {
            mhandler.obtainMessage(START_SHOW_UPLOAD, uploadDisplayText).sendToTarget();
        }
        HashMap<String, Object> uploadMap = new HashMap<String, Object>();
        uploadMap.put("TARGET", UPLOAD_FILE_TAG);
        uploadMap.put("FILE_NAME", fileUploadParam.fileName);
        uploadMap.put("FILE_PATH", fileUploadParam.targetFile.getAbsoluteFile());
        uploadMap.put("UPLOAD_TYPE_FLAG", fileUploadParam.uploadTypeFlag);
        uploadMap.put("UPLOAD_TYPE", fileUploadParam.uploadKey);
        uploadMap.put("POSITION", fileUploadParam.position);
        uploadMap.put("ORIGINAL_FILE_NAME", fileUploadParam.originalFileName);
        long timeMillis = System.currentTimeMillis();
        long diffTime = timeMillis - startRequestRoleTime;
        if (diffTime >= (10 * 60 * 1000)) {
            if (!TextUtils.isEmpty(fileUploadParam.uploadKey)) {
//                if (TextUtils.equals(fileUploadParam.uploadKey, "faceVideo")) {
//                    try {
//                        fileUpload(fileUploadParam.uploadKey,FileIOUtils.readFile2BytesByStream(fileUploadParam.fileName), fileUploadParam.position);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }else {
                    fileUpload(fileUploadParam.uploadKey, fileUploadParam.targetFile, fileUploadParam.position);

//                }
            } else {
                ossUtils.requestALiYunAssumeRole(activity, fileUploadParam.assumeRoleUrl, uploadMap);
            }
        } else {
            updateFileDealWith(uploadMap);
        }
    }
    /**
     * 这个是把文件变成二进制流
     *
     * @param imagepath
     * @return
     * @throws Exception
     */
    private   byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }
    public static ByteArrayInputStream getByteArrayInputStream(File file){
        return new ByteArrayInputStream(getByetsFromFile(file));
    }
    public static byte[] getByetsFromFile(File file){
        FileInputStream is = null;
        // 获取文件大小
        long length = file.length();
        // 创建一个数据来保存文件数据
        byte[] fileData = new byte[(int)length];

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bytesRead=0;
        // 读取数据到byte数组中
        while(bytesRead != fileData.length) {
            try {
                bytesRead += is.read(fileData, bytesRead, fileData.length - bytesRead);
                if(is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileData;
    }

    private void fileUpload(final String type, File imgFile, final int position) {
        agreementService.requestIdImgFileUpload(activity,
                type,
                imgFile,
                new OnSuccessfulListener<BaseDataBean>() {
                    @Override
                    public void onSuccessful(BaseDataBean baseDataBean, String reqKey) {
                        super.onSuccessful(baseDataBean, reqKey);
//                        uploadedUrls.put(position, (String) baseDataBean.getData());
                        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                        onUploadSuccess(position, "", type, null);
//                        if (onUploadCompletedListener != null) {
//                            onUploadCompletedListener.onUploadCompleted(null);
//                        }
                    }
                });
    }
    private void fileUpload(final String type,byte[] videoFile, final int position) {
        agreementService.requestVideoFileUpload(activity,
                type,
                videoFile,
                new OnSuccessfulListener<BaseDataBean>() {
                    @Override
                    public void onSuccessful(BaseDataBean baseDataBean, String reqKey) {
                        super.onSuccessful(baseDataBean, reqKey);
//                        uploadedUrls.put(position, (String) baseDataBean.getData());
                        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                        onUploadSuccess(position, "", type, null);
//                        if (onUploadCompletedListener != null) {
//                            onUploadCompletedListener.onUploadCompleted(null);
//                        }
                    }
                });
    }

    private AgreementService agreementService = new AgreementService() {
        @Override
        protected void onRequestCompleted() {
            super.onRequestCompleted();

        }
    };

    private void updateFileDealWith(HashMap<String, Object> uploadMap) {
        try {
            if (aLiYunOssRole == null) {
                return;
            }
            ossUtils.setContext(activity)
                    .setAccessKeyId(aLiYunOssRole.getAccessKeyId())
                    .setAccessKeySecret(aLiYunOssRole.getAccessKeySecret())
                    .setSecurityToken(aLiYunOssRole.getSecurityToken())
                    .setEndPoint(aLiYunOssRole.getEndpoint())
                    .setBucket(aLiYunOssRole.getBucket())
                    .build();
            if (uploadMap != null) {
                if (uploadMap.containsKey("TARGET") &&
                        TextUtils.equals(String.valueOf(uploadMap.get("TARGET")), UPLOAD_FILE_TAG)) {
                    if (uploadMap.containsKey("UPLOAD_TYPE_FLAG")) {
                        //拼接上传目录
                        String uploadDirectoryFormat = String.format("%s/%s/",
                                aLiYunOssRole.getDir(),
                                DateUtils.getDateTime(DateFormatEnum.YYYYMMNC));
                        //上传文件
                        String typeFlag = String.valueOf(uploadMap.get("UPLOAD_TYPE_FLAG"));
                        if (TextUtils.equals(typeFlag, "NORMAL")) {
                            uploadFilePrepare(uploadMap);
                            //拼接文件名
                            for (OssUploadFileItem fileItem : fileItems) {
                                String suffixName = GlobalUtils.getSuffixName(fileItem.getFileName());
                                String fname = String.format("%s.%s", GlobalUtils.getNewGuid(), suffixName);
                                fileItem.setFileName(PathsUtils.combine(uploadDirectoryFormat, fname));
                            }
                            ossUtils.asyncUpload(uploadDirectoryFormat, fileItems, UPLOAD_FILE_TAG, false, false);
                        } else if (TextUtils.equals(typeFlag, "BREAKPOINT")) {
                            uploadFilePrepare(uploadMap);
                            //拼接文件名
                            for (OssUploadFileItem fileItem : fileItems) {
                                String suffixName = GlobalUtils.getSuffixName(fileItem.getFileName());
                                String fname = String.format("%s.%s", GlobalUtils.getNewGuid(), suffixName);
                                fileItem.setFileName(PathsUtils.combine(uploadDirectoryFormat, fname));
                            }
                            ossUtils.asyncUpload(uploadDirectoryFormat, fileItems, UPLOAD_FILE_TAG, true, false);
                        } else if (TextUtils.equals(typeFlag, "APPEND")) {
                            uploadFilePrepare(uploadMap);
                            //拼接文件名
                            for (OssUploadFileItem fileItem : fileItems) {
                                String suffixName = GlobalUtils.getSuffixName(fileItem.getFileName());
                                String fname = String.format("%s.%s", GlobalUtils.getNewGuid(), suffixName);
                                fileItem.setFileName(PathsUtils.combine(uploadDirectoryFormat, fname));
                            }
                            ossUtils.asyncUpload(uploadDirectoryFormat, fileItems, UPLOAD_FILE_TAG, false, true);
                        } else {
                            onCompleted();
                            if (isShowUploadProgress) {
                                mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                            }
                        }
                    } else {
                        onCompleted();
                        if (isShowUploadProgress) {
                            mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                        }
                    }
                } else {
                    onCompleted();
                    if (isShowUploadProgress) {
                        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                    }
                }
            } else {
                onCompleted();
                if (isShowUploadProgress) {
                    mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private OssUtils ossUtils = new OssUtils() {
        @Override
        protected void onOssUploadProgress(PutObjectRequest request, long currentSize, long totalSize, String uploadTypeFlag, String uploadType, String target) {
            if (TextUtils.equals(target, UPLOAD_FILE_TAG)) {
                try {
                    if (NetworkUtils.isConnected(activity)) {
                        float progress = (currentSize * 100 / totalSize);
                        if (isShowUploadProgress) {
                            Thread.sleep(15);
                            Bundle bundle = new Bundle();
                            bundle.putFloat("PROGRESS_KEY", progress);
                            Message message = mhandler.obtainMessage(UPLOADING_WITH);
                            message.setData(bundle);
                            mhandler.sendMessage(message);
                        }
                        onUploadProgress(progress, uploadType);
                    } else {
                        mhandler.obtainMessage(UPLOAD_NOT_NETWORK_FLAG).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    Logger.L.info("progress thread 200 error:", e);
                }
            }
        }

        @Override
        protected void onOssUploadProgress(ResumableUploadRequest request, long currentSize, long totalSize, String uploadTypeFlag, String uploadType, String target) {
            if (TextUtils.equals(target, UPLOAD_FILE_TAG)) {
                try {
                    if (NetworkUtils.isConnected(activity)) {
                        float progress = (currentSize * 100 / totalSize);
                        if (isShowUploadProgress) {
                            Thread.sleep(15);
                            Bundle bundle = new Bundle();
                            bundle.putFloat("PROGRESS_KEY", progress);
                            Message message = mhandler.obtainMessage(UPLOADING_WITH);
                            message.setData(bundle);
                            mhandler.sendMessage(message);
                        }
                        onUploadProgress(progress, uploadType);
                    } else {
                        mhandler.obtainMessage(UPLOAD_NOT_NETWORK_FLAG).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    Logger.L.info("progress thread 200 error:", e);
                }
            }
        }

        @Override
        protected void onRequestALiYunAssumeRoleSuccess(ALiYunOssRole aLiYunOssRole, HashMap<String, Object> uploadMap) {
            FileUploadUtils.this.aLiYunOssRole = aLiYunOssRole;
            updateFileDealWith(uploadMap);
        }

        @Override
        protected void onOssUploadSuccess(List<OssResultItem> ossResultItems, String objectKey, String uploadTypeFlag, String uploadKey, String target) {
            try {
                if (TextUtils.equals(target, UPLOAD_FILE_TAG)) {
                    if (TextUtils.equals(uploadTypeFlag, "NORMAL")) {
                        for (OssResultItem ossResultItem : ossResultItems) {
                            PutObjectRequest putObjectRequest = ossResultItem.getRequest();
                            int position = 0;
                            Map<String, String> params = putObjectRequest.getCallbackParam();
                            if (params != null && params.containsKey("POSITION")) {
                                position = ConvertUtils.toInt(params.get("POSITION"));
                            }
                            onUploadSuccess(position, ossResultItem.getUrl(), uploadKey, extra);
                        }
                    } else if (TextUtils.equals(uploadTypeFlag, "BREAKPOINT")) {
                        for (OssResultItem ossResultItem : ossResultItems) {
                            ResumableUploadRequest resumableUploadRequest = ossResultItem.getResumableUploadRequest();
                            int position = 0;
                            Map<String, String> params = resumableUploadRequest.getCallbackParam();
                            if (params != null && params.containsKey("POSITION")) {
                                position = ConvertUtils.toInt(params.get("POSITION"));
                            }
                            onUploadSuccess(position, ossResultItem.getUrl(), uploadKey, extra);
                        }
                    } else if (TextUtils.equals(uploadTypeFlag, "APPEND")) {
                        for (OssResultItem ossResultItem : ossResultItems) {
                            int position = 0;
                            Map<String, String> params = ossResultItem.getRequest().getCallbackParam();
                            if (params != null && params.containsKey("POSITION")) {
                                position = ConvertUtils.toInt(params.get("POSITION"));
                            }
                            onUploadSuccess(position, ossResultItem.getUrl(), uploadKey, extra);
                        }
                    }
                }
            } catch (Exception e) {
                Logger.L.error(e);
            } finally {
                if (ObjectJudge.isNullOrEmpty(ossResultItems)) {
                    onCompleted();
                    if (isShowUploadProgress) {
                        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                    }
                } else {
                    if (count == currUploadCount) {
                        onCompleted();
                        if (isShowUploadProgress) {
                            mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
                        }
                    } else if (isShowUploadProgress) {
                        mhandler.obtainMessage(SHOW_UPLOADING, uploadDisplayText).sendToTarget();
                    }
                }
            }
        }

        @Override
        protected void onRequestALiYunAssumeRoleCompleted() {
            onCompleted();
            if (isShowUploadProgress) {
                mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
            }
        }
    };

    private void uploadFilePrepare(HashMap<String, Object> params) {
        fileItems = new ArrayList<OssUploadFileItem>();
        if (isShowUploadProgress) {
            mhandler.obtainMessage(SHOW_UPLOADING, uploadDisplayText).sendToTarget();
        }
        OssUploadFileItem ossUploadFileItem = new OssUploadFileItem();
        ossUploadFileItem.setFileName(String.valueOf(params.get("FILE_NAME")));
        ossUploadFileItem.setFilePath(String.valueOf(params.get("FILE_PATH")));
        Map<String, String> ossparams = new HashMap<String, String>();
        ossparams.put("POSITION", String.valueOf(params.get("POSITION")));
        ossparams.put("UPLOAD_TYPE_FLAG", String.valueOf(params.get("UPLOAD_TYPE_FLAG")));
        ossparams.put("UPLOAD_TYPE", String.valueOf(params.get("UPLOAD_TYPE")));
        ossparams.put("ORIGINAL_FILE_NAME", String.valueOf(params.get("ORIGINAL_FILE_NAME")));
        ossUploadFileItem.setParams(ossparams);
        fileItems.add(ossUploadFileItem);
    }

    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_SHOW_UPLOAD) {
                if (dialogPlus == null) {
                    dialogPlus = mloading.buildDialog(activity, UPLOAD_DIALOG_ID, String.valueOf(msg.obj));
                }
                mloading.setRotate(dialogPlus, true);
                mloading.setCurrentProgress(dialogPlus, 0);
                mloading.setMaxProgress(dialogPlus, 100);
                mloading.setContent(dialogPlus, String.valueOf(msg.obj));
                dialogPlus.show();
            } else if (msg.what == SHOW_UPLOADING) {
                if (dialogPlus == null) {
                    dialogPlus = mloading.buildDialog(activity, UPLOAD_DIALOG_ID, String.valueOf(msg.obj));
                }
                mloading.setRotate(dialogPlus, false);
                mloading.setCurrentProgress(dialogPlus, 0);
                mloading.setMaxProgress(dialogPlus, 100);
                mloading.setContent(dialogPlus, String.valueOf(msg.obj));
                dialogPlus.show();
            } else if (msg.what == UPLOADING_WITH) {
                if (dialogPlus != null) {
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        float progress = bundle.getFloat("PROGRESS_KEY", 0);
                        if (progress > 0) {
                            if (dialogPlus.getHolderView() != null) {
                                DonutProgress dpProgress = (DonutProgress) dialogPlus.getHolderView().findViewById(R.id.dp_progress);
                                if (dpProgress != null) {
                                    dpProgress.setProgress(progress);
                                }
                            }
                        }
                    }
                }
            } else if (msg.what == DISMISS_LOADING) {
                if (dialogPlus != null && dialogPlus.isShowing()) {
                    mloading.dismiss(dialogPlus);
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogPlus = null;
                        }
                    }, 500);
                }
            } else if (msg.what == UPLOAD_NOT_NETWORK_FLAG) {
                ToastUtils.showLong(activity, R.string.network_faild_please_check);
            }
        }
    };

    public void showLoading(String text) {
        mhandler.obtainMessage(START_SHOW_UPLOAD, text).sendToTarget();
    }

    public void hideLoading() {
        mhandler.obtainMessage(DISMISS_LOADING).sendToTarget();
    }
}
