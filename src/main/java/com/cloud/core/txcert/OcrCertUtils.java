//package com.cloud.core.txcert;
//
//import android.Manifest;
//import android.app.Activity;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.cloud.core.txcert.event.OnSignResultCall;
//import com.cloud.core.utils.AppInfoUtils;
//import com.cloud.core.utils.ToastUtils;
//import com.webank.faceaction.tools.WbCloudFaceVerifySdk;
//import com.webank.mbank.ocr.WbCloudOcrSDK;
//import com.webank.mbank.ocr.net.EXIDCardResult;
//import com.webank.mbank.ocr.tools.ErrorCode;
//
///**
// * Author lijinghuan
// * Email:ljh0576123@163.com
// * CreateTime:2018/11/8
// * Description:ocr认证 https://cloud.tencent.com/document/product/655/14084#.E8.BA.AB.E4.BB.BD.E8.AF.81.E8.AF.86.E5.88.AB.E7.BB.93.E6.9E.9C.E7.B1.BB
// * Modifier:
// * ModifyContent:
// */
//public class OcrCertUtils implements OnSignResultCall, WbCloudOcrSDK.OcrLoginListener {
//
//    private Activity activity = null;
//    private String txCertAppId = "";
//    private LoadingDialog loading = new LoadingDialog();
//    private OnOcrCertSuccessCall onOcrCertSuccessCall = null;
//    private CertSignInfo certSignInfo = null;
//    private WbCloudOcrSDK.WBOCRTYPEMODE wbocrtypemode = WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeNormal;
//
//    private OcrCertUtils() {
//        //init
//    }
//
//    public static OcrCertUtils getInstance() {
//        return new OcrCertUtils();
//    }
//
//    /**
//     * 设置ocr认证结果回调
//     *
//     * @param onOcrCertSuccessCall ocr认证结果回调
//     */
//    public void setOnOcrCertSuccessCall(OnOcrCertSuccessCall onOcrCertSuccessCall) {
//        this.onOcrCertSuccessCall = onOcrCertSuccessCall;
//    }
//
//    /**
//     * ocr认证
//     * step 1
//     *
//     * @param context         上下文
//     * @param txCertAppId     认证appid
//     * @param wbocrtypemode   认证模式
//     * @param signRequestCall 签名请求回调
//     */
//    public void faceCert(Activity activity, String txCertAppId, WbCloudOcrSDK.WBOCRTYPEMODE wbocrtypemode, OnSignRequestCall signRequestCall) {
//        this.activity = activity;
//        this.txCertAppId = txCertAppId;
//        this.wbocrtypemode = wbocrtypemode;
//        if (signRequestCall != null) {
//            if (EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA)) {
//                signRequestCall.onSignRequest(txCertAppId, "", "");
//            } else {
//                EasyPermissions.requestPermissions(activity, "此功能需要获取摄像头权限",
//                        FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE,
//                        Manifest.permission.CAMERA);
//            }
//        }
//    }
//
//    public void onPermissionsGranted(int requestCode, OnSignRequestCall signRequestCall) {
//        if (requestCode == FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
//            faceCert(activity, txCertAppId, wbocrtypemode, signRequestCall);
//        }
//    }
//
//    public void onPermissionsDenied(int requestCode) {
//        if (requestCode == FaceCodes.FACE_EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
//            ToastUtils.showLong(activity, "获取相机权限失败");
//        }
//    }
//
//    @Override
//    public void onSignResult(CertSignInfo certSignInfo) {
//        this.certSignInfo = certSignInfo;
//        Bundle data = new Bundle();
//        WbCloudOcrSDK.InputData inputData = new WbCloudOcrSDK.InputData(
//                certSignInfo.getOrderNo(),
//                certSignInfo.getCertAppId(),
//                certSignInfo.getVersion(),
//                certSignInfo.getNonce(),
//                certSignInfo.getUserId(),
//                certSignInfo.getSign(),
//                String.format("ip=%s", AppInfoUtils.getLocalIpAddress()),
//                String.format("lgt=%s;lat=%s", certSignInfo.getLongitude(), certSignInfo.getLatitude()));
//        data.putSerializable(WbCloudOcrSDK.INPUT_DATA, inputData);
//        data.putString(WbCloudOcrSDK.TITLE_BAR_COLOR, WbCloudFaceVerifySdk.WHITE);
//        data.putString(WbCloudOcrSDK.TITLE_BAR_CONTENT, "身份证识别");
//        data.putString(WbCloudOcrSDK.WATER_MASK_TEXT, "仅供本次业务使用");
//        data.putLong(WbCloudOcrSDK.SCAN_TIME, 20000);
//        data.putString(WbCloudOcrSDK.OCR_FLAG, "1");
//        //启动SDK，进入SDK界面
//        loading.showDialog(activity, "认证中请稍候", null);
//        WbCloudOcrSDK.getInstance().init(activity, data, this);
//    }
//
//    @Override
//    public void onLoginSuccess() {
//        loading.dismiss();
//        WbCloudOcrSDK.getInstance().startActivityForOcr(activity, new WbCloudOcrSDK.IDCardScanResultListener() {
//            //证件结果回调接口
//            @Override
//            public void onFinish(String resultCode, String resultMsg) {
//                // resultCode为0，则刷脸成功；否则刷脸失败
//                if (!TextUtils.equals(resultCode, "0")) {
//                    ToastUtils.showLong(activity, "识别失败");
//                    return;
//                }
//                if (onOcrCertSuccessCall == null) {
//                    return;
//                }
//                EXIDCardResult result = WbCloudOcrSDK.getInstance().getResultReturn();
//                bindOcrCertResult(result);
//            }
//        }, wbocrtypemode);
//    }
//
//    @Override
//    public void onLoginFailed(String errorCode, String errorMsg) {
//        loading.dismiss();
//        if (TextUtils.equals(errorCode, ErrorCode.IDOCR_LOGIN_PARAMETER_ERROR)) {
//            ToastUtils.showLong(activity, errorMsg);
//        } else {
//            ToastUtils.showLong(activity, "登录OCR sdk失败");
//        }
//    }
//
//    private void bindOcrCertResult(EXIDCardResult result) {
//        OcrCertResult certResult = new OcrCertResult();
//        //正面信息
//        certResult.setType(result.type);
//        certResult.setCardNum(result.cardNum);
//        certResult.setName(result.name);
//        certResult.setSex(result.sex);
//        certResult.setAddress(result.address);
//        certResult.setNation(result.nation);
//        certResult.setBirth(result.birth);
//        certResult.setFrontFullImageSrc(result.frontFullImageSrc);
//        certResult.setFrontWarning(result.frontWarning);
//        //反面信息
//        certResult.setOffice(result.office);
//        certResult.setValidDate(result.validDate);
//        certResult.setBackFullImageSrc(result.backFullImageSrc);
//        certResult.setBackWarning(result.backWarning);
//        //每次请求返回信息
//        certResult.setSign(result.sign);
//        certResult.setOrderNo(result.orderNo);
//        certResult.setOcrId(result.ocrId);
//        onOcrCertSuccessCall.onOcrCertSuccess(certResult);
//    }
//}
