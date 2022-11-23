package com.cloud.core.api.services;

import android.content.Context;


import com.cloud.core.annotations.APIUrlInterfaceClass;
import com.cloud.core.annotations.ApiCheckAnnotation;
import com.cloud.core.annotations.ReturnCodeFilter;
import com.cloud.core.api.OnRetCodesListening;
import com.cloud.core.api.RequestApiUrls;
import com.cloud.core.api.annotations.IAgreementAPI;
import com.cloud.core.beans.BaseDataBean;
import com.cloud.core.beans.RedirectionConfigItem;
import com.cloud.core.beans.RetrofitParams;
import com.cloud.core.configs.BaseOkrxService;
import com.cloud.core.configs.scheme.SchemeConfigProperty;
import com.cloud.core.events.Func1;
import com.cloud.core.okrx.BaseSubscriber;
import com.cloud.core.okrx.events.OnSuccessfulListener;

import java.io.File;
import java.io.FileInputStream;


/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
@APIUrlInterfaceClass(RequestApiUrls.class)
@ReturnCodeFilter(retCodes = {"4001", "4002", "200"}, retCodesListeningClass = OnRetCodesListening.class)
public class AgreementService extends BaseOkrxService {

    /**
     * 根据key获取重定向配置
     *
     * @param context
     * @param schemePath  schemePath
     * @param versionCode 配置版本号
     * @param group       每个端标识(USER_APP MERCHANT_APP)
     * @param listener    监听
     */
    @ApiCheckAnnotation(IsNetworkValid = true, IsTokenValid = false)
    public void requestRedirectionConfigByKey(Context context,
                                              final String schemePath,
                                              final int versionCode,
                                              final String group,
                                              SchemeConfigProperty configProperty,
                                              final OnSuccessfulListener<RedirectionConfigItem> listener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<RedirectionConfigItem, AgreementService>(context, this) {
            @Override
            protected void onSuccessful(RedirectionConfigItem redirectionConfigItem, String reqKey, Object extra) {
                bindCall(listener, redirectionConfigItem, reqKey, extra);
            }
        };
        baseSubscriber.setReqKey(schemePath);
        baseSubscriber.setExtra(configProperty);
        requestObject(context, IAgreementAPI.class, this, baseSubscriber, new Func1<IAgreementAPI, RetrofitParams>() {
            @Override
            public RetrofitParams call(IAgreementAPI agreementAPI) {
                return agreementAPI.requestRedirectionConfigByKV(schemePath, versionCode > 0 ? versionCode : Integer.MAX_VALUE, group);
            }
        });
    }
    /**
     * 上传图片
     */
    @ApiCheckAnnotation(IsNetworkValid = true, IsTokenValid = true)
    public void requestImgFileUpload(Context context,
                                     final String type,
                                     final File file,
                                     final OnSuccessfulListener<BaseDataBean> onSuccessfulListener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<BaseDataBean, AgreementService>(context, this) {
            @Override
            protected void onSuccessful(BaseDataBean baseDataBean, String reqKey) {
                bindCall(onSuccessfulListener, baseDataBean);
            }
        };
        requestObject(context, IAgreementAPI.class, this, baseSubscriber, new Func1<IAgreementAPI, RetrofitParams>() {
            @Override
            public RetrofitParams call(IAgreementAPI orderAPI) {
                return orderAPI.requestImgFileUpload(type, file);
            }
        });
    }
    /**
     * 上传用户补充资料图片
     */
    @ApiCheckAnnotation(IsNetworkValid = true, IsTokenValid = true)
    public void requestIdImgFileUpload(Context context,
                                     final String type,
                                     final File file,
                                     final OnSuccessfulListener<BaseDataBean> onSuccessfulListener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<BaseDataBean, AgreementService>(context, this) {
            @Override
            protected void onSuccessful(BaseDataBean baseDataBean, String reqKey) {
                bindCall(onSuccessfulListener, baseDataBean);
            }
        };
        requestObject(context, IAgreementAPI.class, this, baseSubscriber, new Func1<IAgreementAPI, RetrofitParams>() {
            @Override
            public RetrofitParams call(IAgreementAPI orderAPI) {
                return orderAPI.requestIdImgFileUpload(type, file);
            }
        });
    }
    /**
     * 上传用户补充资料图片
     */
    @ApiCheckAnnotation(IsNetworkValid = true, IsTokenValid = true)
    public void requestVideoFileUpload(Context context,
                                     final String type,
                                     final byte[] videoFile,
                                     final OnSuccessfulListener<BaseDataBean> onSuccessfulListener) {
        BaseSubscriber baseSubscriber = new BaseSubscriber<BaseDataBean, AgreementService>(context, this) {
            @Override
            protected void onSuccessful(BaseDataBean baseDataBean, String reqKey) {
                bindCall(onSuccessfulListener, baseDataBean);
            }
        };
        requestObject(context, IAgreementAPI.class, this, baseSubscriber, new Func1<IAgreementAPI, RetrofitParams>() {
            @Override
            public RetrofitParams call(IAgreementAPI orderAPI) {
                return orderAPI.requestVideoFileUpload(type, videoFile);
            }
        });
    }
}
