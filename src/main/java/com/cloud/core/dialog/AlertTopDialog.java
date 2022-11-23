package com.cloud.core.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.beans.CmdItem;
import com.cloud.core.utils.PixelUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/5/16
 * @Description:弹出顶部dialog
 * @Modifier:
 * @ModifyContent:
 */
public class AlertTopDialog {

    private String title = "";
    private String content = "";
    private CmdItem[] buttons = null;
    private AlertDialog dialog = null;

    protected void onButtonClick(AlertDialog dialog, CmdItem item) {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setButtons(CmdItem[] buttons) {
        this.buttons = buttons;
    }

    public void show(Context context) {
        if (context == null || ObjectJudge.isNullOrEmpty(buttons)) {
            return;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        int version = android.os.Build.VERSION.SDK_INT;
        if (version < 15 || version >= 19) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        View view = View.inflate(context, R.layout.alert_top_dialog_view, null);
        //设置标题
        TextView titleView = (TextView) view.findViewById(R.id.dialog_title_tv);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        //设置内容
        TextView contentView = (TextView) view.findViewById(R.id.dialog_content_tv);
        if (!TextUtils.isEmpty(content)) {
            contentView.setText(content);
        }
        //设置按钮
        LinearLayout btnll = (LinearLayout) view.findViewById(R.id.dialog_buttons_ll);
        for (int i = 0; i < buttons.length; i++) {
            CmdItem button = buttons[i];
            TextView btn = createButton(context, button, i > 0 && (i + 1) < buttons.length);
            btnll.addView(btn);
        }
        dialog.setView(view);
        dialog.show();
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
                onButtonClick(dialog, item);
            }
        });
        return button;
    }
}
