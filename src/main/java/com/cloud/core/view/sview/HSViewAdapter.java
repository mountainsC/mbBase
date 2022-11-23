package com.cloud.core.view.sview;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.cloud.core.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/7
 * @Description:横向滚动视图适配器
 * @Modifier:
 * @ModifyContent:
 */
public class HSViewAdapter<SVH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<SVH> {

    private List<T> dataList = new ArrayList<T>();
    private OnViewHolderListener<SVH, T> onViewHolderListener = null;
    private OnItemClickListener<T> onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public void setOnViewHolderListener(OnViewHolderListener<SVH, T> listener) {
        this.onViewHolderListener = listener;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public SVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (onViewHolderListener != null) {
            return onViewHolderListener.onCreateView(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SVH holder, int position) {
        if (onViewHolderListener != null) {
            T item = getItem(position);
            onViewHolderListener.onBindView(holder, item);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int pos = ConvertUtils.toInt(v.getTag());
                        onItemClickListener.onItemClick(v, getItem(pos));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
