package com.cloud.core.refresh;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ViewFlipper;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/23
 * Description: ViewFlipper listview刷新视图
 * Modifier:
 * ModifyContent:
 */
public class CusViewFlipper extends ViewFlipper {

    private BaseAdapter mAdapter;
    private boolean mAreAllItemsSelectable;
    private View mEmptyView;
    private OnDataSetChangedListener onDataSetChangedListener = null;

    public CusViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }
    };

    /**
     * Sets the data behind this CusViewFlipper.
     *
     * @param adapter The ListAdapter which is responsible for maintaining the data
     *                backing this list and for producing a view to represent an
     *                item in that data set.
     * @see #getAdapter()
     */
    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
            mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
        }

        setupChildren();
    }

    private void setupChildren() {

        removeAllViews();

        updateEmptyStatus((mAdapter == null) || mAdapter.isEmpty());

        if (mAdapter == null) {
            return;
        }

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View child = mAdapter.getView(i, null, this);
            this.addViewInLayout(child, -1, child.getLayoutParams(), true);
        }

        if (count > 0 && onDataSetChangedListener != null) {
            onDataSetChangedListener.onDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        final boolean empty = ((mAdapter == null) || mAdapter.isEmpty());
        updateEmptyStatus(empty);
    }

    /**
     * When the current adapter is empty, the LinearListView can display a special
     * view call the empty view. The empty view is used to provide feedback to
     * the user that no data is available in this LinearListView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Update the status of the list based on the empty parameter. If empty is
     * true and we have an empty view, display it. In all the other cases, make
     * sure that the layout is VISIBLE and that the empty view is GONE (if
     * it's not null).
     */
    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list
                // view is visible
                setVisibility(View.VISIBLE);
            }
        } else {
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    public interface OnDataSetChangedListener {
        /**
         * 数据改变后监听
         */
        public void onDataSetChanged();
    }

    /**
     * 设置数据改变后监听
     *
     * @param listener 数据改变监听
     */
    public void setOnDataSetChangedListener(OnDataSetChangedListener listener) {
        this.onDataSetChangedListener = listener;
    }
}
