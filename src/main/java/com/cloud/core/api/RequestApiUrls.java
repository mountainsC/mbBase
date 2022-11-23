package com.cloud.core.api;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.beans.BasicConfigBean;
import com.cloud.core.configs.APIConfigProcess;
import com.cloud.core.constants.ServiceAPI;
import com.cloud.core.bases.BaseApplication;
import com.cloud.core.events.OnRequestApiUrl;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/8
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class RequestApiUrls implements OnRequestApiUrl {
    @Override
    public String onBaseUrl(String apiUrlTypeName) {
        Context context = BaseApplication.getInstance().getApplicationContext();
        if (TextUtils.equals(apiUrlTypeName, ServiceAPI.Normal)) {
            BasicConfigBean basicConfigBean = APIConfigProcess.getInstance().getBasicConfigBean(context);
            return basicConfigBean.getApiUrl();
        } else if (TextUtils.equals(apiUrlTypeName, ServiceAPI.SHARE_URL)) {
            BasicConfigBean basicConfigBean = APIConfigProcess.getInstance().getBasicConfigBean(context);
            return basicConfigBean.getH5Url();
        } else if (TextUtils.equals(apiUrlTypeName, ServiceAPI.CLIENT)) {
            BasicConfigBean basicConfigBean = APIConfigProcess.getInstance().getBasicConfigBean(context, "MB_USER");
            return basicConfigBean.getApiUrl();
        } else if (TextUtils.equals(apiUrlTypeName, ServiceAPI.MER)) {
            BasicConfigBean basicConfigBean = APIConfigProcess.getInstance().getBasicConfigBean(context, "MB_MER");
            return basicConfigBean.getApiUrl();
        } else if (TextUtils.equals(apiUrlTypeName, ServiceAPI.ETH)) {
            return "https://api.etherscan.io/";
        }
        return "";
    }
}
