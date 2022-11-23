package com.cloud.core.configs.scheme.jumps;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.core.configs.scheme.SchemeConfigParamReturnProperty;
import com.cloud.core.configs.scheme.SchemeItem;
import com.cloud.core.configs.scheme.SchemesHandling;
import com.cloud.core.R;
import com.cloud.core.utils.RxCachePool;
import com.cloud.core.ObjectJudge;
import com.cloud.core.logger.Logger;
import com.cloud.core.okrx.OkRxManager;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.enums.RequestState;
import com.cloud.core.events.Action0;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action3;
import com.cloud.core.events.Func9;
import com.cloud.core.utils.BundleUtils;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.dialog.LoadingDialog;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/8
 * @Description:页面路由工具类
 * @Modifier:
 * @ModifyContent:
 */
public class BaseGoPagerUtils {

    private LoadingDialog mloading = new LoadingDialog();

    /**
     * @param activity                  当前上下文
     * @param t                         数据对象
     * @param currGoAppVersion          当前跳转app版本
     * @param isCheckGoVersion          是否检测版本
     * @param onH5Callback              启动h5回调
     * @param onApiCallbackSuccessful   请求api成功回调
     * @param onProtozoaCallback        启动activity回调
     * @param onRequestConfigUrl        除uniqueTag外url全路径
     * @param onGoReceiveParamsCallback 接收参数回调
     * @param <T>
     */
    public <T extends BaseGoBean> void startActivity(Activity activity,
                                                     T t,
                                                     String currGoAppVersion,
                                                     boolean isCheckGoVersion,
                                                     OnH5Callback onH5Callback,
                                                     OnApiCallbackSuccessful onApiCallbackSuccessful,
                                                     OnProtozoaCallback onProtozoaCallback,
                                                     OnRequestConfigUrl onRequestConfigUrl,
                                                     OnGoReceiveParamsCallback onGoReceiveParamsCallback) {
        try {
            if (activity == null || t == null) {
                return;
            }
            if (t.isToH5()) {
                if (onH5Callback != null) {
                    onH5Callback.onCallback(t.getUrl());
                }
            } else {
                if (ObjectJudge.isNullOrEmpty(t.getConfigs())) {
                    if (TextUtils.isEmpty(t.getUniqueTag()) ||
                            onRequestConfigUrl == null ||
                            onApiCallbackSuccessful == null ||
                            onProtozoaCallback == null) {
                        return;
                    }
                    String requestUrl = onRequestConfigUrl.onUrl(t.getUniqueTag());
                    mloading.showDialog(activity, R.string.processing_just, null);
                    requestGoConfig(activity,
                            requestUrl,
                            onProtozoaCallback,
                            onApiCallbackSuccessful,
                            onGoReceiveParamsCallback,
                            currGoAppVersion,
                            isCheckGoVersion);
                } else {
                    //兼容版本,待后面处理
                    if (onProtozoaCallback != null) {
                        goConfigDealwith(onProtozoaCallback,
                                onGoReceiveParamsCallback,
                                t.getConfigs(),
                                currGoAppVersion,
                                isCheckGoVersion);
                    }
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void requestGoConfig(Activity activity,
                                 String requestConfigUrl,
                                 final OnProtozoaCallback onProtozoaCallback,
                                 final OnApiCallbackSuccessful onApiCallbackSuccessful,
                                 final OnGoReceiveParamsCallback onGoReceiveParamsCallback,
                                 final String currGoAppVersion,
                                 final boolean isCheckGoVersion) {
        OkRxManager.getInstance().get(activity,
                requestConfigUrl,
                null,
                null,
                false,
                "",
                0,
                new Action3<String, String, HashMap<String, ReqQueueItem>>() {
                    @Override
                    public void call(String response, String apiRequestKey, HashMap<String, ReqQueueItem> reqQueueItemHashMap) {
                        if (onApiCallbackSuccessful == null) {
                            return;
                        }
                        String configs = onApiCallbackSuccessful.onGetConfigs(response);
                        if (TextUtils.isEmpty(configs)) {
                            return;
                        }
                        List<GoConfigItem> configItems = JsonUtils.parseArray(configs, GoConfigItem.class);
                        if (ObjectJudge.isNullOrEmpty(configItems)) {
                            return;
                        }
                        goConfigDealwith(onProtozoaCallback,
                                onGoReceiveParamsCallback,
                                configItems,
                                currGoAppVersion,
                                isCheckGoVersion);
                    }
                },
                "",
                null,
                new Action1<RequestState>() {
                    @Override
                    public void call(RequestState requestState) {
                        if (requestState == RequestState.Completed) {
                            mloading.dismiss();
                        }
                    }
                }, null, "", null);
    }

    private void goConfigDealwith(OnProtozoaCallback onProtozoaCallback,
                                  OnGoReceiveParamsCallback onGoReceiveParamsCallback,
                                  List<GoConfigItem> configItems,
                                  String currGoAppVersion,
                                  boolean isCheckGoVersion) {
        try {
            GoConfigItem configItem = null;
            for (GoConfigItem item : configItems) {
                if (item.isEnabled() && item.getDeviceType() == DeviceType.Android.getValue() &&
                        (!isCheckGoVersion || item.getVersion().contains(currGoAppVersion))) {
                    configItem = item;
                    break;
                }
            }
            if (configItem == null) {
                return;
            }
            if (onGoReceiveParamsCallback != null) {
                GoConfigItem goConfigItem = onGoReceiveParamsCallback.onGoReceiveParams(configItem);
                if (goConfigItem != null) {
                    configItem = goConfigItem;
                }
            }
            //获取bundle
            Bundle bundle = new Bundle();
            HashMap<String, Object> params = configItem.getParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                BundleUtils.setBundleValue(bundle, entry.getKey(), entry.getValue());
            }
            onProtozoaCallback.onCallback(configItem.getPageName(), bundle);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private Uri getH5Uri(String schemeFullAddress, Uri data) {
        StringBuffer pturlsb = new StringBuffer();
        pturlsb.append(schemeFullAddress.substring(0, schemeFullAddress.indexOf("?")));
        String parastr = schemeFullAddress.substring(schemeFullAddress.indexOf("?") + 1);
        if (!TextUtils.isEmpty(parastr)) {
            pturlsb.append("?");
            String[] paraslst = parastr.split("&");
            if (!ObjectJudge.isNullOrEmpty(paraslst)) {
                for (String s : paraslst) {
                    String[] paras = s.split("=");
                    pturlsb.append(String.format("%s=%s&", paras[0], URLEncoder.encode(paras[1])));
                }
                pturlsb = pturlsb.replace(pturlsb.length() - 1, pturlsb.length(), "");
            }
        }
        return Uri.parse(pturlsb.toString());
    }

    /**
     * 根据scheme获取配置项
     *
     * @param context                       当前上下文
     * @param data                          目标uri
     * @param schemeHost                    scheme协议头(如mibaostore://tenancy/)
     * @param isLogin                       是否登录
     * @param goAction                      启动目标activity回调
     * @param mainAction                    启动main activity回调
     * @param configParamReturnPropertyFunc 获取remote scheme配置回调
     * @param loginAction                   登录回调
     * @param finishAction                  结束回调
     */
    public void getGoConfigByScheme(Context context,
                                    Uri data,
                                    String schemeHost,
                                    boolean isLogin,
                                    final Action1<GoConfigItem> goAction,
                                    final Action0 mainAction,
                                    final Func9<SchemeConfigParamReturnProperty, Context, GoConfigItem, Uri, Boolean, SchemesHandling, Action1<GoConfigItem>, Action3<GoConfigItem, SchemeItem, Uri>, Action0, Action0> configParamReturnPropertyFunc,
                                    Action0 loginAction,
                                    final Action0 finishAction) {
        if (data == null ||
                TextUtils.isEmpty(schemeHost) ||
                configParamReturnPropertyFunc == null) {
            if (mainAction != null) {
                mainAction.call();
            }
            return;
        }
        String schemeFullAddress = data.toString();
        if (!schemeFullAddress.startsWith(schemeHost)) {
            //如果非scheme地址再看是否url中的参数
            if (schemeFullAddress.contains("?")) {
                data = getH5Uri(schemeFullAddress, data);
            }
        }
        SchemesHandling handling = new SchemesHandling();
        //如果协议不匹配则直接退出应用
        if (!schemeFullAddress.startsWith(schemeHost) && finishAction != null) {
            finishAction.call();
            return;
        }
        handling.getConfigItemByScheme(context,
                data,
                schemeHost,
                isLogin,
                configParamReturnPropertyFunc,
                new Action1<GoConfigItem>() {
                    @Override
                    public void call(GoConfigItem configItem) {
                        //如果目标名称不为空则启动，否则启动main页面
                        if (!TextUtils.isEmpty(configItem.getPageName()) && goAction != null) {
                            goAction.call(configItem);
                            //移除缓存中的跳转标记
                            RxCachePool.getInstance().clearObject(configItem.getPageName());
                        } else if (mainAction != null) {
                            mainAction.call();
                        }
                    }
                }, loginAction, finishAction);
    }
}
