package com.cloud.core.refresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/2
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class XRefreshNoScrollListView extends XRefreshListView {
    public XRefreshNoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
