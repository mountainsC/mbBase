package com.cloud.core.view.sview;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View view, T item);
}
