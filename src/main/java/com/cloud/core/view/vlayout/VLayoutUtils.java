package com.cloud.core.view.vlayout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.events.Action1;
import com.cloud.core.view.sview.HSView;
import com.cloud.core.view.sview.OnItemClickListener;
import com.cloud.core.view.sview.OnViewHolderListener;
import com.cloud.core.view.sview.SpaceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/5
 * @Description:vlayout工具类
 * @Modifier:
 * @ModifyContent:
 */
public class VLayoutUtils {

    VLayoutBuilder vLayoutBuilder = new VLayoutBuilder();
    private int OnePlusNMaxNumber = 5;
    private boolean isChangedAdapter = false;
    private List<String> hasKeys = new ArrayList<String>();

    //step 1 设置缓存池
    //相同布局出现多次时需要设置(布局数小时无需设置)
    public void setViewPool(RecyclerView recyclerView, int maxPool) {
        if (recyclerView == null || maxPool <= 0) {
            return;
        }
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, maxPool);
    }

    //step 2 构建
    public void refresh(RecyclerView recyclerView) {
        if (recyclerView == null || !isChangedAdapter) {
            return;
        }
        this.isChangedAdapter = false;
        //取出所有适配器
        VirtualLayoutManager virtualLayoutManager = vLayoutBuilder.getVirtualLayoutManager();
        DelegateAdapter delegateAdapter = vLayoutBuilder.getDelegateAdapter();
        List<DelegateAdapter.Adapter> adapterList = vLayoutBuilder.getSequenceAdapterList();
        delegateAdapter.setAdapters(adapterList);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);
    }

    /**
     * 是否包含adapter
     *
     * @param subKey
     * @return
     */
    public boolean hasAdapter(String subKey) {
        if (TextUtils.isEmpty(subKey)) {
            return true;
        }
        if (vLayoutBuilder.isBuild()) {
            return vLayoutBuilder.containKey(subKey);
        } else {
            return true;
        }
    }

    /**
     * 移除适配器
     *
     * @param subKey
     */
    public void removeAdapter(String subKey) {
        if (TextUtils.isEmpty(subKey)) {
            return;
        }
        if (vLayoutBuilder.isBuild()) {
            if (vLayoutBuilder.containKey(subKey)) {
                vLayoutBuilder.remove(subKey);
            }
        }
    }

    //step 3 添加适配器
    public <T> void addSubAdapter(Context context, SubAdapter subAdapter) {
        if (context != null && subAdapter != null) {
            //初始化变量
            if (!vLayoutBuilder.isBuild()) {
                hasKeys.clear();
                vLayoutBuilder.build(context);
            }
            //如何包含适配器则返回
            if (vLayoutBuilder.containKey(subAdapter.getSubKey())) {
                return;
            }
            //如果列数小于1则设置默认值1
            if (subAdapter.getSpanCount() < 1) {
                subAdapter.setSpanCount(1);
            }
            //添加适配器到集合中
            if (subAdapter.getVLayoutType() == VLayoutType.LinearHorizontal) {
                subAdapter.setSpanCount(1);
                subAdapter.setLayoutHelper(new SingleLayoutHelper());
                SubAdapter singleAdapter = createSingleAdapter(context, subAdapter);
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), singleAdapter);
                isChangedAdapter = true;
            } else if (subAdapter.getVLayoutType() == VLayoutType.OnePlusN) {
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), createOnePlusNAdapter(context, subAdapter));
                isChangedAdapter = true;
            } else if (subAdapter.getVLayoutType() == VLayoutType.LinearVertical) {
                if (subAdapter.getSpanCount() > 1) {
                    GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(subAdapter.getSpanCount());
                    gridLayoutHelper.setAutoExpand(false);
                    gridLayoutHelper.setVGap(subAdapter.getVGap());
                    gridLayoutHelper.setHGap(subAdapter.getHGap());
                    subAdapter.setLayoutHelper(gridLayoutHelper);
                } else {
                    subAdapter.setLayoutHelper(new LinearLayoutHelper());
                }
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), subAdapter);
                isChangedAdapter = true;
            } else if (subAdapter.getVLayoutType() == VLayoutType.LinearVerticalStaggered) {
                StaggeredGridLayoutHelper staggeredGridLayoutHelper = new StaggeredGridLayoutHelper();
                staggeredGridLayoutHelper.setLane(subAdapter.getSpanCount());
                staggeredGridLayoutHelper.setVGap(subAdapter.getVGap());
                staggeredGridLayoutHelper.setHGap(subAdapter.getHGap());
                subAdapter.setLayoutHelper(staggeredGridLayoutHelper);
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), subAdapter);
                isChangedAdapter = true;
            } else if (subAdapter.getVLayoutType() == VLayoutType.SingleObject) {
                subAdapter.setLayoutHelper(new SingleLayoutHelper());
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), subAdapter);
                isChangedAdapter = true;
            } else if (subAdapter.getVLayoutType() == VLayoutType.ScrollFix) {
                ScrollFixLayoutHelper scrollFixLayoutHelper = new ScrollFixLayoutHelper(
                        subAdapter.getScrollFixAlianType(),
                        subAdapter.getScrollFixXSpace(),
                        subAdapter.getScrollFixYSpace()
                );
                scrollFixLayoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_ENTER);
                subAdapter.setLayoutHelper(scrollFixLayoutHelper);
                vLayoutBuilder.addAdapterInGroup(subAdapter.getGroupPosition(), subAdapter);
                isChangedAdapter = true;
            }
        }
    }

    private <SVH extends BaseViewHolder<IVH>, IVH extends BaseItemViewHolder, T> SubAdapter createOnePlusNAdapter(final Context context, final SubAdapter subAdapter) {
        //目前最大支持5个item
        int disNumber = subAdapter.getOnePlusNCount();
        if (disNumber > OnePlusNMaxNumber) {
            disNumber = OnePlusNMaxNumber;
        }
        List<T> lst = new ArrayList<T>();
        List<T> list = subAdapter.getDataList();
        if (disNumber > list.size()) {
            disNumber = list.size();
        }
        for (int i = 0; i < list.size(); i++) {
            if (i < disNumber) {
                lst.add(list.get(i));
            } else {
                break;
            }
        }
        subAdapter.setDataList(lst);
        subAdapter.setLayoutHelper(new OnePlusNLayoutHelper());
        return subAdapter;
    }

    private <SVH extends BaseViewHolder<IVH>, IVH extends BaseItemViewHolder, T> SubAdapter createSingleAdapter(final Context context, final SubAdapter subAdapter) {
        if (vLayoutBuilder.containKey(subAdapter.getSubKey())) {
            SubAdapter adapter = vLayoutBuilder.getAdapterItem(subAdapter.getSubKey());
            return adapter;
        } else {
            SubAdapter<BaseViewHolder<HSItemViewHolder>, HSItemViewHolder, SingleSubItem<T>> singleSubAdapter = new SubAdapter<>();
            //helper
            singleSubAdapter.setLayoutHelper(subAdapter.getLayoutHelper());
            //data
            List<SingleSubItem<T>> subItems = new ArrayList<SingleSubItem<T>>();
            SingleSubItem<T> subItem = new SingleSubItem<T>();
            subItem.setSubItems(subAdapter.getDataList());
            subItem.setSpanCount(subAdapter.getSpanCount());
            subItems.add(subItem);
            singleSubAdapter.setRefresh(true);
            singleSubAdapter.setDataList(subItems);
            //spancount
            singleSubAdapter.setSpanCount(subAdapter.getSpanCount());
            //sub key
            singleSubAdapter.setSubKey(subAdapter.getSubKey());
            //click
            singleSubAdapter.setOnSubViewListener(new OnSubViewListener<BaseViewHolder<HSItemViewHolder>, HSItemViewHolder, SingleSubItem<T>>() {
                @Override
                public BaseViewHolder<HSItemViewHolder> onCreateHolder(String subKey) {
                    HSItemViewHolder itemViewHolder = new HSItemViewHolder(context);
                    if (subAdapter.gethScrollViewHeight() > 0) {
                        itemViewHolder.vlsubScrollViewHsv.getLayoutParams().height = subAdapter.gethScrollViewHeight();
                    }
                    BaseViewHolder<HSItemViewHolder> holder = new BaseViewHolder<HSItemViewHolder>(itemViewHolder.getContentView());
                    holder.setVH(itemViewHolder);
                    return holder;
                }

                @Override
                public void onBindHolder(final String subKey, HSItemViewHolder holder, SingleSubItem<T> entity) {
                    holder.vlsubScrollViewHsv.instance(entity.getSpanCount(),
                            new OnItemClickListener<T>() {
                                @Override
                                public void onItemClick(View view, T item) {
                                    OnSubViewListener onSubViewListener = subAdapter.getOnSubViewListener();
                                    if (onSubViewListener != null) {
                                        onSubViewListener.onItemClick(subKey, view, item);
                                    }
                                }
                            },
                            new OnViewHolderListener<SVH, T>() {
                                @Override
                                public SVH onCreateView(ViewGroup parent, int viewType) {
                                    OnSubViewListener onSubViewListener = subAdapter.getOnSubViewListener();
                                    if (onSubViewListener != null) {
                                        SVH baseViewHolder = (SVH) onSubViewListener.onCreateHolder(subKey);
                                        return baseViewHolder;
                                    }
                                    return null;
                                }

                                @Override
                                public void onBindView(SVH holder, T item) {
                                    OnSubViewListener onSubViewListener = subAdapter.getOnSubViewListener();
                                    if (onSubViewListener != null) {
                                        onSubViewListener.onBindHolder(subKey, holder.getVH(), item);
                                    }
                                }
                            }, new Action1<SpaceItem>() {
                                @Override
                                public void call(SpaceItem spaceItem) {
                                    OnSubViewListener onSubViewListener = subAdapter.getOnSubViewListener();
                                    if (onSubViewListener != null) {
                                        onSubViewListener.onHSViewSpace(subKey, spaceItem);
                                    }
                                }
                            });
                    holder.vlsubScrollViewHsv.notifyDataSetChanged(entity.getSubItems(), false);
                }
            });
            //orientation
            singleSubAdapter.setVLayoutType(subAdapter.getVLayoutType());
            singleSubAdapter.setAdapterPosition(subAdapter.getAdapterPosition());
            singleSubAdapter.setGroupPosition(subAdapter.getGroupPosition());
            return singleSubAdapter;
        }
    }

    private class HSItemViewHolder extends BaseItemViewHolder {

        public HSView vlsubScrollViewHsv = null;

        public HSItemViewHolder(Context context) {
            View view = View.inflate(context, R.layout.hs_layout_view, null);
            vlsubScrollViewHsv = (HSView) view.findViewById(R.id.vlsub_scroll_view_hsv);
            setContentView(view);
        }
    }

    public <T> void notifyChanged(List<T> dataList, String subKey, boolean isRefresh) {
        if (ObjectJudge.isNullOrEmpty(dataList)) {
            isChangedAdapter = true;
            removeAdapter(subKey);
            return;
        }
        SubAdapter subAdapter = vLayoutBuilder.getAdapterItem(subKey);
        if (subAdapter == null) {
            isChangedAdapter = true;
            return;
        }
        subAdapter.setRefresh(isRefresh);
        if (subAdapter.getVLayoutType() == VLayoutType.LinearHorizontal) {
            List<SingleSubItem<T>> subItems = new ArrayList<SingleSubItem<T>>();
            SingleSubItem<T> subItem = new SingleSubItem<T>();
            subItem.setSubItems(dataList);
            subItem.setSpanCount(subAdapter.getSpanCount());
            subItems.add(subItem);
            subAdapter.setRefresh(true);
            subAdapter.setDataList(subItems);
        } else if (subAdapter.getVLayoutType() == VLayoutType.OnePlusN) {
            int disNumber = subAdapter.getOnePlusNCount();
            if (disNumber > OnePlusNMaxNumber) {
                disNumber = OnePlusNMaxNumber;
            }
            List<T> lst = new ArrayList<T>();
            List<T> list = subAdapter.getDataList();
            if (disNumber > list.size()) {
                disNumber = list.size();
            }
            for (int i = 0; i < list.size(); i++) {
                if (i < OnePlusNMaxNumber) {
                    lst.add(list.get(i));
                } else {
                    break;
                }
            }
            subAdapter.setDataList(lst);
        } else {
            subAdapter.setDataList(dataList);
        }
        subAdapter.notifyDataSetChanged();
    }

    public <T> void notifyChanged(List<T> dataList, String subKey) {
        notifyChanged(dataList, subKey, true);
    }

    public <T> void notifySingleChanged(T dataItem, String subKey) {
        if (dataItem == null || ((dataItem instanceof String) && TextUtils.isEmpty((String) dataItem))) {
            isChangedAdapter = true;
            removeAdapter(subKey);
            return;
        }
        List<T> list = new ArrayList<T>();
        list.add(dataItem);
        notifyChanged(list, subKey, true);
    }

    public void clear(String subKey) {
        SubAdapter subAdapter = vLayoutBuilder.getAdapterItem(subKey);
        if (subAdapter == null) {
            return;
        }
        subAdapter.clearDataList();
        subAdapter.notifyDataSetChanged();
    }

    public <SA> SA getShadowAdapter(String subKey) {
        SubAdapter subAdapter = vLayoutBuilder.getAdapterItem(subKey);
        if (subAdapter == null) {
            return null;
        }
        return (SA) subAdapter.getShadowAdapter();
    }
}
