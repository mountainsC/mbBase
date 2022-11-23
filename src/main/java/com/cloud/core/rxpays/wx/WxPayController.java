package com.cloud.core.rxpays.wx;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Author Gs
 * @Email:gs_12@foxmail.com
 * @CreateTime:2017/3/14
 * @Description: 微信支付
 * @Modifier:
 * @ModifyContent:
 */

public class WxPayController {

    private Context context;
    private IWXAPI api;

    public String APP_ID = "";

    public WxPayController(Context context, String APP_ID) {
        this.context = context;
        if (TextUtils.isEmpty(APP_ID)) {
            Toast.makeText(context, "APP_ID不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        this.APP_ID = APP_ID;
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.registerApp(APP_ID);
    }

    public void payOrder(WxPayBean wxPayBean) {
        if (wxPayBean != null) {
            PayReq req = new PayReq();
            req.appId = wxPayBean.getAppid();
            req.partnerId = wxPayBean.getPartnerid();
            req.prepayId = wxPayBean.getPrepayid();
            req.nonceStr = wxPayBean.getNoncestr();
            req.timeStamp = wxPayBean.getTimestamp();
            req.packageValue = wxPayBean.getPackageValue();
            req.sign = wxPayBean.getSign();
            req.extData = "app data";
            api.sendReq(req);
        } else {
            Log.d("WxPay", "WxPayBean参数异常");
        }
    }
}
