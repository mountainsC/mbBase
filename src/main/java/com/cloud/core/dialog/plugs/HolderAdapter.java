package com.cloud.core.dialog.plugs;

import android.widget.BaseAdapter;

public interface HolderAdapter extends Holder {

    public void setAdapter(BaseAdapter adapter);

    public void setOnItemClickListener(OnHolderListener listener);
}
