package com.cloud.core.view.vlayout;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseViewHolder<VH extends BaseItemViewHolder> extends RecyclerView.ViewHolder {
    private VH vh = null;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setVH(VH vh) {
        this.vh = vh;
    }

    public VH getVH() {
        return this.vh;
    }
}
