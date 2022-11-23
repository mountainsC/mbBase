package com.cloud.core.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cloud.core.R;
import com.cloud.core.dialog.events.ISelectDialogDataItem;
import com.cloud.core.dialog.events.OnMultiSelectedListener;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.events.Action3;
import com.cloud.core.icons.IconView;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectionDialog {
    private Context context;
    private OnMultiSelectedListener<SelectDataItem> onMultiSelectedListener;
    private DialogPlus dialogPlus;

    @Nullable
    private Holder allSelectHolder;
    @Nullable
    private SelectDataItem allSelectDataItem;
    private DataAdapter adapter;

    /**
     * 设置选择项监听
     *
     * @param listener
     */
    public void setOnMultiSelectedListener(OnMultiSelectedListener<SelectDataItem> listener) {
        this.onMultiSelectedListener = listener;
    }

    /**
     * 显示多选数据的对话框
     *
     * @param allSelect 放在顶部的全选项
     * @param dataItems 选项的数据集，不包括全选项
     */
    public void showDialog(Context context, SelectDataItem allSelect, List<SelectDataItem> dataItems) {
        this.context = context;
        allSelectDataItem = allSelect;
        DialogManager.DialogManagerBuilder<List<SelectDataItem>> builder = DialogManager.getInstance().builder(context, R.layout.simple_dialog_selection_multiple);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        builder.setExtra(dataItems);
        builder.show(new Action3<View, DialogPlus, List<SelectDataItem>>() {
            @Override
            public void call(View view, DialogPlus dialogPlus, List<SelectDataItem> dataItems) {
                build(view, dialogPlus, dataItems);
            }
        });
    }

    private void build(View view, DialogPlus dialogPlus, List<SelectDataItem> dataItems) {
        this.dialogPlus = dialogPlus;
        adapter = new DataAdapter(dataItems);
        View titleVG = view.findViewById(R.id.select_all);
        if (allSelectDataItem != null) {
            titleVG.setBackgroundResource(R.drawable.simple_dialog_selection_background);
            titleVG.setVisibility(View.VISIBLE);
            Holder holder = new Holder(titleVG);
            holder.textView.setText(allSelectDataItem.getShowName());
            holder.line.setVisibility(View.GONE);

            allSelectHolder = holder;
            setSelected(holder, allSelectDataItem);
            titleVG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean allSelect = !allSelectDataItem.isSelected();
                    allSelectDataItem.setSelected(allSelect);
                    setSelected(allSelectHolder, allSelectDataItem);
                    adapter.setSelectAll(allSelect);
                }
            });
        } else {
            titleVG.setVisibility(View.GONE);
        }
        ListView vg = view.findViewById(R.id.select_data);
        vg.setAdapter(adapter);
        vg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectDataItem item = (SelectDataItem) parent.getItemAtPosition(position);
                boolean isSelected = !item.isSelected();
                item.setSelected(isSelected);
                adapter.notifyDataSetChanged();

                //更新顶部的状态
                if (allSelectDataItem != null) {
                    boolean allSelect = item.isSelected();
                    if (allSelect) {
                        allSelect = adapter.isAllSelected();
                    }
                    allSelectDataItem.setSelected(allSelect);
                    setSelected(allSelectHolder, allSelectDataItem);
                }
            }
        });

        view.findViewById(R.id.select_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMultiSelectedListener != null) {
                    onMultiSelectedListener.selected(adapter.getSelectedData());
                }
                MultipleSelectionDialog.this.dialogPlus.dismiss();
            }
        });
    }

    private void setSelected(Holder holder, ISelectDialogDataItem item) {
        if (item.isSelected()) {
            holder.iconView.setIconUcode("&#xe635;");
            holder.iconView.setTextColor(0xFF2395FF);
        } else {
            holder.iconView.setIconUcode("&#xe637;");
            holder.iconView.setTextColor(0xFFBFC5C9);
        }
    }

    private class DataAdapter extends BaseAdapter {
        private List<SelectDataItem> data;

        private DataAdapter(List<SelectDataItem> data) {
            this.data = data;
        }

        private void setSelectAll(boolean isSelect) {
            for (SelectDataItem t : data) {
                t.setSelected(isSelect);
            }
            notifyDataSetChanged();
        }

        /**
         * 现在是否已经全部选中
         */
        private boolean isAllSelected() {
            for (int i = 0; i < getCount(); i++) {
                SelectDataItem t = getItem(i);
                //如果有一个和刚刚改变的不一样，那么肯定取消顶部的选中
                if (!t.isSelected()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 返回选中的数据
         */
        private List<SelectDataItem> getSelectedData() {
            List<SelectDataItem> selectedData = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                SelectDataItem t = getItem(i);
                if (t.isSelected()) {
                    selectedData.add(t);
                }
            }
            return selectedData;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public SelectDataItem getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.simple_dialog_selection_multiple_item, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.line.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            SelectDataItem item = getItem(position);
            holder.textView.setText(item.getShowName());
            setSelected(holder, item);
            return convertView;
        }
    }

    private class Holder {
        private IconView iconView;
        private TextView textView;
        private View line;

        private Holder(View convertView) {
            this.iconView = convertView.findViewById(R.id.select_icon);
            this.textView = convertView.findViewById(R.id.select_text);
            this.line = convertView.findViewById(R.id.line_bottom);
        }
    }
}
