package com.cloud.core.configs.scheme;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.cloud.core.ObjectJudge;
import com.cloud.core.configs.scheme.jumps.GoConfigItem;
import com.cloud.core.events.Action0;
import com.cloud.core.events.Action1;
import com.cloud.core.events.Action3;
import com.cloud.core.events.Func9;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.ValidUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/12/22
 * @Description:scheme处理
 * @Modifier:
 * @ModifyContent:
 */
public class SchemesHandling {

    private void bindConfigPropertyByScheme(GoConfigItem configItem, SchemeItem scheme, Uri data) {
        TargetItem targets = scheme.getTargets();
        if (TextUtils.isEmpty(targets.getName())) {
            //目标名称为空
            return;
        }
        configItem.setPageName(targets.getName());
        //根据参数映射获取对应值
        HashMap<String, String> paramsMapper = scheme.getParamsMapper();
        //设置scheme参数
        configItem.setParams(getSchemeParams(paramsMapper, data));
    }

    /**
     * 根据scheme获取跳转目标配置
     *
     * @param context                       当前上下文
     * @param data                          目标uri
     * @param schemeHost                    scheme协议头(如mibaostore://tenancy/)
     * @param isLogin                       是否登录
     * @param configParamReturnPropertyFunc 获取remote scheme配置回调
     * @param configAction                  跳转配置项回调
     * @param loginAction                   登录回调
     * @param finishAction                  如果未匹配到则要结束当前页面
     * @return
     */
    public void getConfigItemByScheme(Context context,
                                      Uri data,
                                      String schemeHost,
                                      boolean isLogin,
                                      Func9<SchemeConfigParamReturnProperty, Context, GoConfigItem, Uri, Boolean, SchemesHandling, Action1<GoConfigItem>, Action3<GoConfigItem, SchemeItem, Uri>, Action0, Action0> configParamReturnPropertyFunc,
                                      Action1<GoConfigItem> configAction,
                                      Action0 loginAction,
                                      Action0 finishAction) {
        if (context == null || data == null || configAction == null || configParamReturnPropertyFunc == null) {
            return;
        }
        GoConfigItem configItem = new GoConfigItem();
        try {
            String schemePath = data.getPath();
            if (TextUtils.isEmpty(schemePath)) {
                return;
            }
            SchemeItem scheme = null;
//            if (configParamReturnPropertyFunc == null) {
//                //若回调为null则从初始scheme配置获取
//                scheme = getScheme(context, schemeConfig, schemePath);
//                if (scheme != null) {
//                    bindConfigPropertyByScheme(configItem, scheme, data);
//                    configAction.call(configItem);
//                }
//            } else {
            SchemeConfigParamReturnProperty call = configParamReturnPropertyFunc.call(context, configItem, data, isLogin, this, configAction, new Action3<GoConfigItem, SchemeItem, Uri>() {
                @Override
                public void call(GoConfigItem configItem, SchemeItem scheme, Uri data) {
                    //如果是从远程获取scheme则需要调用此回调先绑定参数
                    bindConfigPropertyByScheme(configItem, scheme, data);
                }
            }, loginAction, finishAction);
            SchemeSource schemeSource = call.getSchemeSource();
            if (schemeSource == SchemeSource.localVersionScheme) {
                //从remote返回scheme或本地根据版本缓存的scheme
                scheme = call.getScheme();
                if (scheme != null) {
                    bindConfigPropertyByScheme(configItem, scheme, data);
                    if (checkPass(configItem, isLogin)) {
                        configAction.call(configItem);
                    } else {
                        if (loginAction != null) {
                            loginAction.call();
                        }
                    }
                }
            }
//            else if (schemeSource == SchemeSource.remoteScheme) {
//                //当前类型由外面调用
//                //如果通过接口获取则接口返回:
//                //1.先转化成SchemeItem对象再缓存;
//                //2.调用bindConfigPropertyAction绑定相应参数;
//                //3.调用configAction;
//            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    //检测是否通过;如果参数包含isNeedLogin=true,则根据传入的登录状态来判断跳转目标页前是否需要登录
    public boolean checkPass(GoConfigItem configItem, boolean isLogin) {
        if (isLogin) {
            return true;
        }
        HashMap<String, Object> params = configItem.getParams();
        if (params.containsKey("isNeedLogin")) {
            Object isNeedLogin = params.get("isNeedLogin");
            boolean loginStatus = ObjectJudge.isTrue(isNeedLogin);
            if (loginStatus) {
                //需要跳登录页面
                return false;
            } else {
                //正常流程
                return true;
            }
        } else {
            return true;
        }
    }

    private HashMap<String, Object> getSchemeParams(HashMap<String, String> paramsMapper, Uri data) {
        //接收参数
        HashMap<String, Object> params = new HashMap<String, Object>();
        //参数值不能通过data.getQueryParameter获取,前端传过来的带路由的url做为参数无法获取
        HashMap<String, String> urlParams = GlobalUtils.getUrlParams(data.toString());
        for (Map.Entry<String, String> entry : paramsMapper.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            if (urlParams.containsKey(entry.getKey())) {
                String parameter;
                //从scheme url中获取参数
                parameter = urlParams.get(entry.getKey());
                try {
                    parameter = URLDecoder.decode(parameter, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (parameter.length() >= 10) {
                    params.put(entry.getValue(), parameter);
                } else {
                    if (ValidUtils.valid("^\\d+$", parameter)) {
                        params.put(entry.getValue(), ConvertUtils.toInt(parameter));
                    } else if (ValidUtils.valid("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$", parameter)) {
                        params.put(entry.getValue(), ConvertUtils.toDouble(parameter));
                    } else {
                        params.put(entry.getValue(), parameter);
                    }
                }
            } else {
                if (entry.getValue().length() >= 10) {
                    params.put(entry.getKey(), entry.getValue());
                } else {
                    if (ValidUtils.valid("^\\d+$", entry.getValue())) {
                        params.put(entry.getKey(), ConvertUtils.toInt(entry.getValue()));
                    } else if (ValidUtils.valid("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$", entry.getValue())) {
                        params.put(entry.getKey(), ConvertUtils.toDouble(entry.getValue()));
                    } else {
                        params.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return params;
    }
}
