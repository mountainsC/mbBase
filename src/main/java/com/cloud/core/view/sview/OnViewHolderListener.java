package com.cloud.core.view.sview;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/7
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnViewHolderListener<SVH extends RecyclerView.ViewHolder, T> {

    public SVH onCreateView(ViewGroup parent, int viewType);

    public void onBindView(SVH holder, T item);
}
