package com.cloud.core.view.sview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/7
 * @Description:间距
 * @Modifier:
 * @ModifyContent:
 */
public class SpaceItem {

    private Rect outRect = null;

    private View view = null;

    private RecyclerView parent = null;

    private RecyclerView.State state = null;

    public Rect getOutRect() {
        return outRect;
    }

    public void setOutRect(Rect outRect) {
        this.outRect = outRect;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public RecyclerView getParent() {
        return parent;
    }

    public void setParent(RecyclerView parent) {
        this.parent = parent;
    }

    public RecyclerView.State getState() {
        return state;
    }

    public void setState(RecyclerView.State state) {
        this.state = state;
    }
}
