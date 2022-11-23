package com.cloud.core.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cloud.core.R;
import com.cloud.core.beans.BaseBean;
import com.cloud.core.logger.Logger;
import com.cloud.core.refresh.api.RefreshLayout;
import com.cloud.core.refresh.footer.ClassicsFooter;
import com.cloud.core.refresh.header.ClassicsHeader;
import com.cloud.core.refresh.listener.OnRefreshLoadmoreListener;
import com.cloud.core.utils.ResUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/23
 * @Description:listview刷新视图
 * @Modifier:
 * @ModifyContent:
 */
public class XRefreshListView extends RelativeLayout {

    private SmartRefreshLayout xRefreshSrfl = null;
    private ListView xRefreshListLv = null;
    private OnXListViewListener mListViewListener;
    private OnXListViewItemClickListener onXListViewItemClickListener = null;

    public XRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            View view = View.inflate(context, R.layout.x_refresh_list_view, null);
            xRefreshSrfl = (SmartRefreshLayout) view.findViewById(R.id.x_refresh_srfl);
            xRefreshSrfl.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    if (mListViewListener != null) {
                        mListViewListener.onLoadMore();
                    }
                }

                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    if (mListViewListener != null) {
                        mListViewListener.onRefresh();
                    }
                }
            });
            xRefreshListLv = (ListView) view.findViewById(R.id.x_refresh_list_lv);
            xRefreshListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onXListViewItemClickListener != null) {
                        onXListViewItemClickListener.onItemClick(position);
                    }
                }
            });
            LayoutParams rlparam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(view, rlparam);
            int[] androidAttrs = new int[]{
                    android.R.attr.divider,
                    android.R.attr.dividerHeight
            };
            TypedArray a = context.obtainStyledAttributes(attrs, androidAttrs);
            int resourceId = a.getResourceId(0, 0);
            Drawable dividerDrawable = ResUtils.getDrawable(context, resourceId);
            if (dividerDrawable != null) {
                xRefreshListLv.setDivider(dividerDrawable);
            }
            float dividerHeight = a.getDimension(1, 0);
            xRefreshListLv.setDividerHeight((int) dividerHeight);
            Drawable bgDrawable = getBackground();
            if (bgDrawable == null) {
                xRefreshSrfl.setBackgroundColor(Color.parseColor("#efefef"));
            } else {
                xRefreshSrfl.setBackground(bgDrawable);
            }
            a.recycle();
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    public void setXListViewListener(OnXListViewListener listener) {
        this.mListViewListener = listener;
    }

    public ClassicsHeader getRefreshHeader() {
        ClassicsHeader refreshHeader = (ClassicsHeader) xRefreshSrfl.getRefreshHeader();
        return refreshHeader;
    }

    public ClassicsFooter getClassicsFooter() {
        ClassicsFooter refreshFooter = (ClassicsFooter) xRefreshSrfl.getRefreshFooter();
        return refreshFooter;
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        return xRefreshSrfl;
    }

    public void setAdapter(ListAdapter adapter) {
        if (xRefreshListLv == null || adapter == null) {
            return;
        }
        xRefreshListLv.setAdapter(adapter);
    }

    public void setPullRefreshEnable(boolean enable) {
        xRefreshSrfl.setEnableRefresh(enable);
    }

    public void setPullLoadEnable(boolean enable) {
        xRefreshSrfl.setEnableLoadmore(enable);
    }

    /**
     * 刷新列表
     */
    public void refresh() {
        if (mListViewListener != null) {
            mListViewListener.onRefresh();
        }
    }

    /**
     * 数据加载完后需要调用此方法进行刷新动作初始化
     */
    public void initRL() {
        xRefreshSrfl.finishRefresh();
        xRefreshSrfl.finishLoadmore();
    }

    /**
     * 从adapter中获取数据项
     *
     * @param position 数据索引
     * @param <T>      数据类型
     * @return
     */
    public <T> T getItem(int position) {
        Object item = xRefreshListLv.getAdapter().getItem(position);
        return (T) item;
    }

    /**
     * 检测实图控件加载状态(业务数据完成后调用此方法)
     *
     * @param baseBean
     * @param isEnableLoad 是否启用加载
     */
    public void checkViewLoadStatus(BaseBean baseBean, boolean isEnableLoad) {
        try {
            if (baseBean == null) {
                this.setPullLoadEnable(false);
                return;
            }
            if (baseBean.isLastPage() && !baseBean.isHasNextPage()) {
                this.setPullLoadEnable(false);
            } else {
                if (baseBean.getPageNum() > baseBean.getPages()) {
                    this.setPullLoadEnable(false);
                } else {
                    if (baseBean.getPageNum() == baseBean.getLastPage()) {
                        this.setPullLoadEnable(false);
                    } else {
                        if (!isEnableLoad) {
                            this.setPullLoadEnable(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 检测实图控件加载状态(业务数据完成后调用此方法)
     *
     * @param baseBean
     */
    public void checkViewLoadStatus(BaseBean baseBean) {
        checkViewLoadStatus(baseBean, true);
    }

    public void setOnXListViewItemClickListener(OnXListViewItemClickListener listener) {
        this.onXListViewItemClickListener = listener;
    }

    public ListView getListView() {
        return this.xRefreshListLv;
    }

    /**
     * 更新列表数据
     */
    public void notifyDataSetChanged() {
        if (xRefreshListLv.getAdapter() instanceof BaseAdapter) {
            BaseAdapter adapter = (BaseAdapter) xRefreshListLv.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }
}
