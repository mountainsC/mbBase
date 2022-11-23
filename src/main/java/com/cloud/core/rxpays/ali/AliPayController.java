package com.cloud.core.rxpays.ali;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/24
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class AliPayController {
    private Activity activity;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    PayResult payResult = new PayResult((Map) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        AliPayController.this.payResult(PayResultType.PAY_SUCCESS, resultInfo);
                        Log.d("ALI_PAY", "支付成功");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        AliPayController.this.payResult(PayResultType.PAY_USER_CLOSE, resultInfo);
                        Log.d("ALI_PAY", "用户取消");
                    } else {
                        AliPayController.this.payResult(PayResultType.PAY_FAILURE, resultInfo);
                        Log.d("ALI_PAY", "支付失败");
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            } else if (msg.what == 2) {
                AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                String resultStatus = authResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    AliPayController.this.authResult(AuthResultType.AUTH_SUCCESS, authResult);
                } else {
                    AliPayController.this.authResult(AuthResultType.AUTH_FAILURE, authResult);
                }
            }
        }
    };

    public AliPayController(Activity activity) {
        this.activity = activity;
    }

    public void payOrder(final String orderInfo) {
        if (!TextUtils.isEmpty(orderInfo)) {
            Runnable payRunnable = new Runnable() {
                public void run() {
                    PayTask alipay = new PayTask(AliPayController.this.activity);
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    AliPayController.this.handler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else {
            Log.d("AliPay", "AliPayBean参数异常");
        }

    }

    public void payOrder(AliPayBean aliPayBean) {
        if (aliPayBean != null) {
            String orderInfo = aliPayBean.getSign() + "&sign=" + aliPayBean.getPaySigned();
            this.payOrder(orderInfo);
        } else {
            Log.d("AliPay", "AliPayBean参数异常");
        }

    }

    public void auth(final String authParams) {
        if (TextUtils.isEmpty(authParams)) {
            return;
        }
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(AliPayController.this.activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authParams, true);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = result;
                AliPayController.this.handler.sendMessage(msg);

            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    protected void payResult(PayResultType payResultType, String resultInfo) {

    }

    protected void authResult(AuthResultType authResultType, AuthResult authResult) {

    }
}
