package com.cloud.core.rxpays.wx;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * @Author Gs
 * @Email:gs_12@foxmail.com
 * @CreateTime:2017/4/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */

public class WxCheckInstalled {

    private IWXAPI api;
    private static WxCheckInstalled wxCheckInstalled;

    public static WxCheckInstalled getInstantiate(Context context) {
        if (wxCheckInstalled == null) {
            wxCheckInstalled = new WxCheckInstalled(context);
        }
        return wxCheckInstalled;
    }

    private WxCheckInstalled(Context context) {
        api = WXAPIFactory.createWXAPI(context, "");
    }

    public boolean isWxCheckInstalled() {
        return api.isWXAppInstalled();
    }

}
