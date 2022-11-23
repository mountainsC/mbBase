package com.cloud.core.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.bases.BaseActivity;
import com.cloud.core.beans.CmdItem;
import com.cloud.core.utils.PixelUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/5/23
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseGloblaSSODialogActivity extends BaseActivity {

    private TextView dialogTitleTv = null;
    private TextView dialogContentTv = null;
    private LinearLayout dialogButtonsLl = null;

    protected abstract void onButtonClick(CmdItem item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_globla_dialog_view);
        dialogTitleTv = (TextView) findViewById(R.id.dialog_title_tv);
        dialogContentTv = (TextView) findViewById(R.id.dialog_content_tv);
        dialogButtonsLl = (LinearLayout) findViewById(R.id.dialog_buttons_ll);
        init();
    }

    protected Bundle getDialogBundle(String title, String content) {
        Bundle bundle = new Bundle();
        bundle.putString("TITLE_KEY", title);
        bundle.putString("CONTENT_KEY", content);
        return bundle;
    }

    private void init() {
        dialogTitleTv.setText(getStringBundle("TITLE_KEY"));
        dialogContentTv.setText(getStringBundle("CONTENT_KEY"));

        dialogButtonsLl.removeAllViews();
        CmdItem[] cmds = {new CmdItem("relogin", "重新登录")};
        for (int i = 0; i < cmds.length; i++) {
            CmdItem button = cmds[i];
            TextView btn = createButton(getActivity(), button, i > 0 && (i + 1) < cmds.length);
            dialogButtonsLl.addView(btn);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public TextView createButton(Context context, CmdItem cmdItem, boolean isAddSpace) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                PixelUtils.dip2px(context, 28)
        );
        params.weight = 1;
        if (isAddSpace) {
            params.setMargins(0, 0, PixelUtils.dip2px(context, 12), 0);
        }
        params.gravity = Gravity.CENTER_VERTICAL;
        TextView button = new TextView(context);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.dialog_button_bg);
        button.setTextColor(Color.WHITE);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setText(cmdItem.getCommandName());
        button.setTag(cmdItem);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null || !(v.getTag() instanceof CmdItem)) {
                    return;
                }
                CmdItem item = (CmdItem) v.getTag();
                onButtonClick(item);
            }
        });
        return button;
    }
}
