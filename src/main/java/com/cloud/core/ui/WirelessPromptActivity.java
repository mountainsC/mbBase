package com.cloud.core.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.bases.BaseActivity;
import com.cloud.core.bases.BaseApplication;
import com.cloud.core.configs.BaseBConfig;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.RedirectUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2015-11-23 下午2:35:11
 * @Description: 网络操作提示
 * @Modifier:
 * @ModifyContent:
 */
public class WirelessPromptActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wireless_prompt_view);
        init();
    }

    private void init() {
        try {
            BaseApplication currapp = BaseApplication.getInstance();
            View returnib = findViewById(R.id.return_ib);
            returnib.setVisibility(View.VISIBLE);
            RxCoreConfigItems configItems = BaseBConfig.getInstance().getConfigItems(getActivity());
            if (!TextUtils.isEmpty(configItems.getThemeColor())) {
                View headview = findViewById(R.id.head_layout_rl);
                headview.setBackgroundColor(Color.parseColor(configItems.getThemeColor()));
            }
            returnib.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RedirectUtils.finishActivity(WirelessPromptActivity.this);
                }
            });
            TextView nonetworkconntv = (TextView) findViewById(R.id.subject_tv);
            nonetworkconntv.setText(R.string.no_network_connection);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
}
