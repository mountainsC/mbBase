//package com.cloud.core.txcert;
//
//import android.Manifest;
//import android.app.Activity;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.cloud.basicfun.permissions.EasyPermissions;
//import com.cloud.core.utils.AppInfoUtils;
//import com.cloud.core.utils.ToastUtils;
//import com.cloud.core.utils.dialog.LoadingDialog;
//import com.cloud.core.txcert.beans.CertResult;
//import com.cloud.core.txcert.beans.CertSignInfo;
//import com.cloud.core.txcert.event.OnCertSuccessCall;
//import com.cloud.core.txcert.event.OnSignRequestCall;
//import com.cloud.core.txcert.event.OnSignResultCall;
//import com.webank.faceaction.tools.ErrorCode;
//import com.webank.faceaction.tools.IdentifyCardValidate;
//import com.webank.faceaction.tools.WbCloudFaceVerifySdk;
//import com.webank.faceaction.ui.FaceVerifyStatus;
//
///**
// * Author lijinghuan
// * Email:ljh0576123@163.com
// * CreateTime:2018/11/8
// * Description:腾讯认证
// * Modifier:
// * ModifyContent:
// */
//public class CertUtils implements OnSignResultCall, WbCloudFaceVerifySdk.FaceVerifyLoginListener {
//
//    private LoadingDialog loading = new LoadingDialog();
//    private Activity activity = null;
//    private OnCertSuccessCall onCertSuccessCall = null;
//    private String txCertAppId = "";
//    private String realName = "";
//    private String idNumber = "";
//    private CertSignInfo certSignInfo = null;
//
//    private CertUtils() {
//        //init
//    }
//
//    /**
//     * 设置认证成功回调
//     *
//     * @param onCertSuccessCall 认证成功回调
//     */
//    public void setOnCertSuccessCall(OnCertSuccessCall onCertSuccessCall) {
//        this.onCertSuccessCall = onCertSuccessCall;
//    }
//
//    /**
//     * 实例
//     *
//     * @return
//     */
//    public static CertUtils getInstance() {
//        return new CertUtils();
//    }
//
//    /**
//     * 人脸认证
//     * step 1
//     *
//     * @param context         上下文
//     * @param txCertAppId     认证appid
//     * @param realName        真实姓名
//     * @param idNumber        身份证号
//     * @param signRequestCall 签名请求回调
//     */
//    public void faceCert(Activity activity, String txCertAppId, String realName, String idNumber, OnSignRequestCall signRequestCall) {
//        if (TextUtils.isEmpty(realName)) {
//            ToastUtils.showLong(activity, "用户姓名不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(idNumber)) {
//            ToastUtils.showLong(activity, "身份证号码不能为空");
//            return;
//        }
//        this.activity = activity;
//        realName = realName.trim();
//        idNumber = idNumber.trim();
//        this.txCertAppId = txCertAppId;
//        this.realName = realName;
//        this.idNumber = idNumber;
//        //如果包含x将小写转成大写
//        if (idNumber.contains("x")) {
//            idNumber = idNumber.replace("x", "X");
//        }
//        //如果返回的结果与输入的身份证号一致则认为有效
//        String effective = IdentifyCardValidate.validate_effective(idNumber);
//        if (TextUtils.equals(effective, idNumber)) {
//            if (signRequestCall != null) {
//                if (EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA)) {
//                    signRequestCall.onSignRequest(txCertAppId, realName, idNumber);
//                } else {
//                    EasyPermissions.requestPermissions(activity, "此功能需要获取摄像头权限",
//                            FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE,
//                            Manifest.permission.CAMERA);
//                }
//            }
//        } else {
//            ToastUtils.showLong(activity, effective);
//        }
//    }
//
//    public void onPermissionsGranted(int requestCode, OnSignRequestCall signRequestCall) {
//        if (requestCode == FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
//            faceCert(activity, txCertAppId, realName, idNumber, signRequestCall);
//        }
//    }
//
//    public void onPermissionsDenied(int requestCode) {
//        if (requestCode == FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
//            ToastUtils.showLong(activity, "获取相机权限失败");
//        }
//    }
//
//    //签名结果回调 step 2
//    @Override
//    public void onSignResult(CertSignInfo certSignInfo) {
//        this.certSignInfo = certSignInfo;
//        Bundle data = new Bundle();
//        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
//                certSignInfo.getRealName(),
//                "01",
//                certSignInfo.getIdNumber(),
//                certSignInfo.getOrderNo(),
//                String.format("ip=%s", AppInfoUtils.getLocalIpAddress()),
//                String.format("lgt=%s;lat=%s", certSignInfo.getLongitude(), certSignInfo.getLatitude()),
//                certSignInfo.getCertAppId(),
//                certSignInfo.getVersion(),
//                certSignInfo.getNonce(),
//                certSignInfo.getUserId(),
//                certSignInfo.getSign(),
//                false,
//                FaceVerifyStatus.Mode.MIDDLE,
//                certSignInfo.getSdkLicense());
//
//        data.putSerializable(WbCloudFaceVerifySdk.INPUT_DATA, inputData);
//
//        //个性化参数设置,可以不设置，不设置则为默认选项。
//        //是否展示刷脸成功页面，默认展示
//        data.putBoolean(WbCloudFaceVerifySdk.SHOW_SUCCESS_PAGE, false);
//        //是否展示刷脸失败页面，默认展示
//        data.putBoolean(WbCloudFaceVerifySdk.SHOW_FAIL_PAGE, true);
//        //sdk样式设置,默认黑色模式
//        data.putString(WbCloudFaceVerifySdk.COLOR_MODE, WbCloudFaceVerifySdk.WHITE);
//        //是否需要录制视频存证,默认录制
//        data.putBoolean(WbCloudFaceVerifySdk.RECORD_VIDEO, true);
//        //是否对录制视频进行检查,默认不检查
//        data.putBoolean(WbCloudFaceVerifySdk.VIDEO_CHECK, false);
//        //设置选择的比对类型  默认为公安网纹图片对比
//        //公安网纹图片比对 WbCloudFaceVerifySdk.ID_CRAD
//        //自带比对源比对  WbCloudFaceVerifySdk.SRC_IMG
//        //仅活体检测  WbCloudFaceVerifySdk.NONE
//        //默认公安网纹图片比对
//        data.putString(WbCloudFaceVerifySdk.COMPARE_TYPE, WbCloudFaceVerifySdk.ID_CARD);
//        //自带比对源设置
//        //比对源图片需要合作方转换成base64 string传入sdk
//        //上送自带的数据源信息，照片类型与照片string缺一不可
//        // 不带比对源的可不传这两个字段
//        //上送照片类型，1是水纹照  2是高清照
//        data.putString(WbCloudFaceVerifySdk.SRC_PHOTO_TYPE, "");  //1是水纹照  2是高清照
//        data.putString(WbCloudFaceVerifySdk.SRC_PHOTO_STRING, "");  //比对源照片的BASE64 string
//
//        loading.showDialog(activity, "认证中请稍候", null);
//        WbCloudFaceVerifySdk.getInstance().init(activity, data, this);
//    }
//
//    @Override
//    public void onLoginSuccess() {
//        WbCloudFaceVerifySdk.getInstance().startActivityForSecurity(new WbCloudFaceVerifySdk.FaceVerifyResultForSecureListener() {
//            @Override
//            public void onFinish(int resultCode, boolean nextShowGuide, String faceCode, String faceMsg, String sign, Bundle extendData) {
//                loading.dismiss();
//                if (resultCode != 0 || extendData == null) {
//                    if (resultCode == ErrorCode.FACEVERIFY_ERROR_DEFAULT) {
//                        ToastUtils.showLong(activity, "后台对比失败");
//                    } else {
//                        ToastUtils.showLong(activity, "刷脸失败");
//                    }
//                    return;
//                }
//                if (onCertSuccessCall == null) {
//                    return;
//                }
//                CertResult result = new CertResult();
//                result.setLiveRate(extendData.getString(WbCloudFaceVerifySdk.FACE_RESULT_LIVE_RATE));
//                result.setSimilarity(extendData.getString(WbCloudFaceVerifySdk.FACE_RESULT_SIMILIRATY));
//                result.setUserImage(extendData.getString(WbCloudFaceVerifySdk.FACE_RESULT_USER_IMG));
//                result.setCertSignInfo(certSignInfo);
//                onCertSuccessCall.onCertSuccess(result);
//            }
//        });
//    }
//
//    @Override
//    public void onLoginFailed(String errorCode, String errorMsg) {
//        loading.dismiss();
//        if (TextUtils.equals(errorCode, ErrorCode.FACEVERIFY_LOGIN_PARAMETER_ERROR)) {
//            ToastUtils.showLong(activity, errorMsg);
//        } else {
//            ToastUtils.showLong(activity, "登录刷脸sdk失败");
//        }
//    }
//}
