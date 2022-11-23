package com.cloud.core.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.dialog.events.OnSelectedListener;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.events.Action3;

import java.util.List;

public class SingleSelectionDialog {
    private Context context;
    private OnSelectedListener<SelectDataItem> onSelectedListener;
    private DialogPlus dialogPlus;
    private List<SelectDataItem> dataItems = null;
    private Object extras = null;

    /**
     * 设置选择项监听
     *
     * @param listener
     */
    public void setOnSelectedListener(OnSelectedListener<SelectDataItem> listener) {
        this.onSelectedListener = listener;
    }

    /**
     * 显示单选数据的对话框
     *
     * @param context   上下文
     * @param dataItems 选项的数据集
     * @param extras    扩展数据
     */
    public <Extra> void showDialog(Context context, List<SelectDataItem> dataItems, Extra extras) {
        this.context = context;
        this.dataItems = dataItems;
        DialogManager.DialogManagerBuilder<Extra> builder = DialogManager.getInstance().builder(context, R.layout.simple_dialog_selection_single);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        builder.setExtra(extras);
        builder.show(new Action3<View, DialogPlus, Extra>() {
            @Override
            public void call(View view, DialogPlus dialogPlus, Extra extras) {
                SingleSelectionDialog.this.extras = extras;
                build(view, dialogPlus);
            }
        });
    }

    /**
     * 显示单选数据的对话框
     *
     * @param context   上下文
     * @param dataItems 选项的数据集
     */
    public void showDialog(Context context, List<SelectDataItem> dataItems) {
        showDialog(context, dataItems, null);
    }

    /**
     * 获取扩展数据
     *
     * @param <Extra>
     * @return
     */
    public <Extra> Extra getExtra() {
        return (Extra) extras;
    }

    private void build(View view, DialogPlus dialogPlus) {
        this.dialogPlus = dialogPlus;

        ViewGroup vg = view.findViewById(R.id.select_data);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataItem item = (SelectDataItem) v.getTag();
                if (onSelectedListener != null) {
                    onSelectedListener.selected(item);
                }
                SingleSelectionDialog.this.dialogPlus.dismiss();
            }
        };
        for (int i = 0; i < dataItems.size(); i++) {
            if (i > 0) {
                View.inflate(context, R.layout.line_horizontal, vg);
            }
            SelectDataItem item = dataItems.get(i);
            TextView ifv = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_dialog_selection_single_item, vg, false);
            ifv.setTag(item);
            ifv.setText(item.getShowName());
            ifv.setOnClickListener(clickListener);
            vg.addView(ifv);
        }
        view.findViewById(R.id.select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleSelectionDialog.this.dialogPlus.dismiss();
            }
        });
    }
}
