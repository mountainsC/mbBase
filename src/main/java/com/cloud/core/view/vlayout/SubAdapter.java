package com.cloud.core.view.vlayout;

import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.cloud.core.ObjectJudge;
import com.cloud.core.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/5
 * @Description:vlayout需用同一个适配器，否则滑动会出现卡顿现象
 * @Modifier:
 * @ModifyContent:
 */
public class SubAdapter<SVH extends BaseViewHolder<IVH>, IVH extends BaseItemViewHolder, T> extends
        DelegateAdapter.Adapter<SVH> {

    private LayoutHelper layoutHelper;
    private String subKey = "";
    private List<T> dataList = new ArrayList<T>();
    private boolean isRefresh = true;
    private OnSubViewListener<SVH, IVH, T> onSubViewListener = null;
    private final int ITEM_VIEW_DATA_TAG = 944329283;
    private VLayoutType vLayoutType = VLayoutType.LinearVertical;
    private int hsViewId = 0;
    private int spanCount = 1;
    private int hScrollViewHeight = 0;
    private int onePlusNCount = 0;
    private int VGap = 0;
    private int HGap = 0;
    private int scrollFixAlianType = 0;
    private int scrollFixXSpace = 0;
    private int scrollFixYSpace = 0;
    private int viewType = 0;
    private boolean isCreateHSView = false;
    /**
     * 适配器索引,用于构建SubAdapter顺序
     * 同一组索引唯一
     */
    private Integer adapterPosition = 0;
    /**
     * 组索引
     */
    private int groupPosition = 0;
    /**
     * 影子adapter
     */
    private Object shadowAdapter = null;

    public SubAdapter() {
        //用于横向布局的视图id
        hsViewId = GlobalUtils.getHashCodeByUUID();
    }

    public Integer getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public Object getShadowAdapter() {
        return shadowAdapter;
    }

    /**
     * 设置当前业务影子adapter即SubAdapter宿主
     *
     * @param shadowAdapter
     */
    public void setShadowAdapter(Object shadowAdapter) {
        this.shadowAdapter = shadowAdapter;
    }

    //step1 set layout helper
    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    public LayoutHelper getLayoutHelper() {
        return this.layoutHelper;
    }

    /**
     * 是否刷新(需在setDataList方法之前设置)
     *
     * @param isRefresh true添加数据集时先清空列表,false追加;
     */
    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    //step2 set datasource
    public void setDataList(List<T> dataList) {
        if (ObjectJudge.isNullOrEmpty(dataList)) {
            return;
        }
        if (isRefresh) {
            this.dataList.clear();
        }
        this.dataList.addAll(dataList);
    }

    //step2 add date item
    public void addDataItem(T dataItem) {
        if (dataItem == null) {
            return;
        }
        if (!this.dataList.contains(dataItem)) {
            this.dataList.add(dataItem);
        }
    }

    //设置单个数据条目时用
    public void setDataItem(T dataItem) {
        if (dataItem == null) {
            return;
        }
        this.dataList.clear();
        this.dataList.add(dataItem);
    }

    /**
     * 清空数据列表
     */
    public void clearDataList() {
        this.dataList.clear();
    }

    /**
     * 移除数据项
     *
     * @param position 数据索引
     * @return true移除成功, false失败
     */
    public boolean removeDataItem(int position) {
        if (position < 0) {
            return false;
        }
        if (this.dataList.size() > 0 && position < this.dataList.size()) {
            T remove = this.dataList.remove(position);
            if (remove == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取适配器唯一标识
     *
     * @return
     */
    public String getSubKey() {
        return subKey;
    }

    /**
     * step3 设置适配器唯一标识
     *
     * @param subKey 标识key,用于获取对应的适配器
     */
    public void setSubKey(String subKey) {
        if (subKey == null) {
            return;
        }
        this.subKey = subKey;
    }

    //step4 设置用于数据绑定的监听器
    public void setOnSubViewListener(OnSubViewListener<SVH, IVH, T> listener) {
        this.onSubViewListener = listener;
    }

    public OnSubViewListener<SVH, IVH, T> getOnSubViewListener() {
        return this.onSubViewListener;
    }

    //step5 设置布局类型
    public void setVLayoutType(VLayoutType vLayoutType) {
        this.vLayoutType = vLayoutType;
    }

    public VLayoutType getVLayoutType() {
        return this.vLayoutType;
    }

    public int getHsViewId() {
        return this.hsViewId;
    }

    public int getSpanCount() {
        return spanCount;
    }

    /**
     * 设置列数
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int gethScrollViewHeight() {
        return hScrollViewHeight;
    }

    /**
     * 设置横向滚动视图高度
     * 建议子视图用线性布局(可以自动填充高度)
     *
     * @param hScrollViewHeight
     */
    @Deprecated
    public void sethScrollViewHeight(int hScrollViewHeight) {
        this.hScrollViewHeight = hScrollViewHeight;
    }

    public int getOnePlusNCount() {
        return onePlusNCount;
    }

    /**
     * 设置1拖N计数
     *
     * @param onePlusNCount
     */
    public void setOnePlusNCount(int onePlusNCount) {
        this.onePlusNCount = onePlusNCount;
    }

    public int getVGap() {
        return VGap;
    }

    /**
     * 设置瀑布流形式item上下间距
     *
     * @param VGap
     */
    public void setVGap(int VGap) {
        this.VGap = VGap;
    }

    public int getHGap() {
        return HGap;
    }

    /**
     * 设置瀑布流形式item左右间距
     *
     */
    public void setHGap(int HGap) {
        this.HGap = HGap;
    }

    public int getScrollFixAlianType() {
        return scrollFixAlianType;
    }

    /**
     * 滚动定位对齐类型
     */
    public void setScrollFixAlianType(int scrollFixAlianType) {
        this.scrollFixAlianType = scrollFixAlianType;
    }

    public int getScrollFixXSpace() {
        return scrollFixXSpace;
    }

    /**
     * 设置滚动视图x方向外边距
     *
     * @param scrollFixXSpace
     */
    public void setScrollFixXSpace(int scrollFixXSpace) {
        this.scrollFixXSpace = scrollFixXSpace;
    }

    public int getScrollFixYSpace() {
        return scrollFixYSpace;
    }

    /**
     * 设置滚动视图y方向外边距
     *
     * @param scrollFixYSpace
     */
    public void setScrollFixYSpace(int scrollFixYSpace) {
        this.scrollFixYSpace = scrollFixYSpace;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isCreateHSView() {
        return isCreateHSView;
    }

    public void setCreateHSView(boolean createHSView) {
        isCreateHSView = createHSView;
    }

    private T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public SVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (onSubViewListener != null) {
            SVH holder = onSubViewListener.onCreateHolder(getSubKey());
            if (holder != null && holder.itemView != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSubViewListener != null) {
                            Object tag = v.getTag(ITEM_VIEW_DATA_TAG);
                            if (tag == null) {
                                return;
                            }
                            T entity = (T) tag;
                            if (entity == null) {
                                return;
                            }
                            onSubViewListener.onItemClick(getSubKey(), v, entity);
                        }
                    }
                });
            }
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SVH holder, int position) {
        if (onSubViewListener != null) {
            if (holder.getVH() != null) {
                T entity = getItem(position);
                if (holder.getVH().getContentView() != null) {
                    holder.getVH().getContentView().setTag(ITEM_VIEW_DATA_TAG, entity);
                }
                onSubViewListener.onBindHolder(getSubKey(), holder.getVH(), entity);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }
}
