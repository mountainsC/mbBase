package com.cloud.core.view.flows;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.PixelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/12
 * @Description:流式布局视图
 * @Modifier:
 * @ModifyContent:
 */
public class FlowItemsView extends DynamicViewGroup implements OnMeasureDynamicViewSizeListener {

    private HashMap<Integer, FlowItem> flowItems = new HashMap<Integer, FlowItem>();
    private HashMap<String, Integer> tagNamesIds = new HashMap<String, Integer>();
    //标签项默认背景
    private int defaultItemBackground = 0;
    //标签项选中背景
    private int selectedItemBackground = 0;
    //标签被禁用时背景
    private int disableItemBackground = 0;
    //是否启用选择
    private boolean enableCheck = true;
    //标签横向内边距
    private float horizontalPadding = 0;
    //标签纵向内边距
    private float verticalPadding = 0;
    //默认文本颜色
    private int defaultItemTextColor = 0;
    //选中项文本颜色
    private int selectedItemTextColor = 0;
    //被禁用项文本颜色
    private int disableItemTextColor = 0;
    //是否单项选择
    private boolean singleCheck = false;
    private OnItemChangeListener onItemChangeListener = null;
    //sku规格
    private SkuSepecItem sepecItem = null;
    //单选并禁用取消
    private boolean singleCheckDisCancel = false;

    public FlowItemsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowItemsView, 0, 0);
        defaultItemBackground = a.getResourceId(R.styleable.FlowItemsView_fiv_defaultItemBackground, 0);
        selectedItemBackground = a.getResourceId(R.styleable.FlowItemsView_fiv_selectedItemBackground, 0);
        disableItemBackground = a.getResourceId(R.styleable.FlowItemsView_fiv_disableItemBackground, 0);
        horizontalPadding = a.getDimension(R.styleable.FlowItemsView_fiv_horizontalPadding, 0);
        verticalPadding = a.getDimension(R.styleable.FlowItemsView_fiv_verticalPadding, 0);
        defaultItemTextColor = a.getColor(R.styleable.FlowItemsView_fiv_defaultItemTextColor, 0);
        selectedItemTextColor = a.getColor(R.styleable.FlowItemsView_fiv_selectedItemTextColor, 0);
        disableItemTextColor = a.getColor(R.styleable.FlowItemsView_fiv_disableItemTextColor, 0);
        singleCheck = a.getBoolean(R.styleable.FlowItemsView_fiv_singleCheck, false);
        singleCheckDisCancel = a.getBoolean(R.styleable.FlowItemsView_fiv_singleCheckDisCancel, false);
        a.recycle();
        int[] androidAttrs = new int[]{
                android.R.attr.paddingLeft,
                android.R.attr.paddingRight,
                android.R.attr.paddingTop,
                android.R.attr.paddingBottom,
                android.R.attr.padding
        };
        TypedArray aArr = context.obtainStyledAttributes(attrs, androidAttrs);
        float padding = aArr.getDimension(4, 1);
        int pleft = (int) aArr.getDimension(0, padding);
        int pright = (int) aArr.getDimension(1, padding);
        int ptop = (int) aArr.getDimension(2, padding);
        int pbottom = (int) aArr.getDimension(3, padding);
        this.setPadding(pleft, ptop, pright, pbottom);
        aArr.recycle();
        this.setOnMeasureDynamicViewSizeListener(this);
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.onItemChangeListener = listener;
    }

    /**
     * 设置是否为单选行为
     * 默认为true;
     *
     * @param singleCheck true:在可用的选项范围内为单选行为;false:为多选行为;
     */
    public void setSingleCheck(boolean singleCheck) {
        this.singleCheck = singleCheck;
    }

    /**
     * 是否启用选择
     *
     * @param enableCheck true所有标签不可选择操作,反之可操作;
     */
    public void setEnableCheck(boolean enableCheck) {
        this.enableCheck = enableCheck;
    }

    /**
     * 设置sku规格
     *
     * @param sepecItem
     */
    public void setSepecItem(SkuSepecItem sepecItem) {
        this.sepecItem = sepecItem;
    }

    /**
     * 获取标签默认属性;相关资源及特性将继承自控件的属性;
     *
     * @param <T>
     * @return
     */
    public <T> TagProperties getDefaultTagProperties() {
        TagProperties<T> properties = new TagProperties<T>();
        properties.setDefaultItemBackground(defaultItemBackground);
        properties.setSelectedItemBackground(selectedItemBackground);
        properties.setDisableItemBackground(disableItemBackground);
        properties.setDefaultItemTextColor(defaultItemTextColor);
        properties.setSelectedItemTextColor(selectedItemTextColor);
        properties.setDisableItemTextColor(disableItemTextColor);
        return properties;
    }

    private TextView buildItem() {
        ViewGroup.LayoutParams tvparam = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                PixelUtils.dip2px(getContext(), 30)
        );
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(tvparam);
        textView.setId(GlobalUtils.getHashCodeByUUID());
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v);
            }
        });
        return textView;
    }

    private void onItemClick(View v) {
        if (v.getTag() == null ||
                !(v.getTag() instanceof FlowItem) ||
                onItemChangeListener == null ||
                !enableCheck) {
            return;
        }
        FlowItem flowItem = (FlowItem) v.getTag();
        if (flowItem.isDisable()) {
            return;
        }
        try {
            boolean state = setViewState(v, flowItem);
            if (state) {
                SkuItem skuItem = getFlowItem(flowItem);
                onItemChangeListener.onItemChange(v, flowItem.isCheck(), sepecItem, skuItem);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    public SkuItem getFlowItem(FlowItem flowItem) {
        SkuItem skuItem = new SkuItem();
        skuItem.setSku(flowItem.getTagName());
        skuItem.setAlias(flowItem.getAlias());
        skuItem.setData(flowItem.getData());
        return skuItem;
    }

    public SkuItem getSkuItemByTag(TagProperties properties) {
        FlowItem flowItem = fromTagProperties(properties);
        return getFlowItem(flowItem);
    }

    private boolean setViewState(View v, FlowItem flowItem) {
        if (flowItem.isCheck()) {
            if (singleCheckDisCancel) {
                return false;
            }
            if (singleCheck) {
                //若为单选行为,则取消项选中状态
                clearCheckItems(v.getId());
            }
            //若原先为选中状态,则单击取消选中
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTextColor(flowItem.getDefaultItemTextColor());
                textView.setBackgroundResource(flowItem.getDefaultItemBackground());
            }
            flowItem.setCheck(false);
            v.setTag(flowItem);
            if (flowItems.containsKey(v.getId())) {
                FlowItem item = flowItems.get(v.getId());
                item.setCheck(false);
            }
        } else {
            if (singleCheck) {
                //若为单选行为,则取消项选中状态
                clearCheckItems(v.getId());
            }
            //若原先为未选中状态,则单击选中
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTextColor(flowItem.getSelectedItemTextColor());
                textView.setBackgroundResource(flowItem.getSelectedItemBackground());
            }
            flowItem.setCheck(true);
            v.setTag(flowItem);
            if (flowItems.containsKey(v.getId())) {
                FlowItem item = flowItems.get(v.getId());
                item.setCheck(true);
            }
        }
        return true;
    }

    private void clearCheckItems(int currViewId) {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = this.getChildAt(i);
            if (childAt.getId() != currViewId) {
                initCheckState(childAt);
            }
        }
    }

    private void initCheckState(View v) {
        if (v.getTag() == null ||
                !(v.getTag() instanceof FlowItem)) {
            return;
        }
        FlowItem flowItem = (FlowItem) v.getTag();
        if (flowItem.isDisable()) {
            return;
        }
        try {
            if (flowItem.isCheck()) {
                //若原先为选中状态,则单击取消选中
                v.setBackgroundResource(flowItem.getDefaultItemBackground());
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(flowItem.getDefaultItemTextColor());
                }
                flowItem.setCheck(false);
                v.setTag(flowItem);
                if (flowItems.containsKey(v.getId())) {
                    FlowItem item = flowItems.get(v.getId());
                    item.setCheck(false);
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private <T> FlowItem initTagProperties(TagProperties<T> tagProperties) {
        if (tagProperties.getDefaultItemBackground() == 0) {
            tagProperties.setDisableItemBackground(defaultItemBackground);
        }
        if (tagProperties.getSelectedItemBackground() == 0) {
            tagProperties.setSelectedItemBackground(selectedItemBackground);
        }
        if (tagProperties.getDisableItemBackground() == 0) {
            tagProperties.setDisableItemBackground(disableItemBackground);
        }
        if (tagProperties.getDefaultItemTextColor() == 0) {
            tagProperties.setDefaultItemTextColor(defaultItemTextColor);
        }
        if (tagProperties.getSelectedItemTextColor() == 0) {
            tagProperties.setSelectedItemTextColor(selectedItemTextColor);
        }
        if (tagProperties.getDisableItemTextColor() == 0) {
            tagProperties.setDisableItemTextColor(disableItemTextColor);
        }
        return fromTagProperties(tagProperties);
    }

    private FlowItem fromTagProperties(TagProperties properties) {
        FlowItem flowItem = new FlowItem();
        flowItem.setCheck(properties.isCheck());
        flowItem.setDisable(properties.isDisable());
        flowItem.setData(properties.getData());
        flowItem.setDefaultItemBackground(properties.getDefaultItemBackground());
        flowItem.setSelectedItemBackground(properties.getSelectedItemBackground());
        flowItem.setDisableItemBackground(properties.getDisableItemBackground());
        flowItem.setTagName(properties.getText());
        flowItem.setAlias(properties.getAlias());
        flowItem.setDefaultItemTextColor(properties.getDefaultItemTextColor());
        flowItem.setSelectedItemTextColor(properties.getSelectedItemTextColor());
        flowItem.setDisableItemTextColor(properties.getDisableItemTextColor());
        return flowItem;
    }

    /**
     * 添加标签项
     *
     * @param tagProperties 可先通过getDefaultTagProperties()获取,然后再修改值;
     * @param <T>
     */
    public <T> void addItem(TagProperties<T> tagProperties) {
        if (tagProperties == null || TextUtils.isEmpty(tagProperties.getText())) {
            return;
        }
        FlowItem flowItem = initTagProperties(tagProperties);
        TextView textView = buildItem();
        if (flowItem.isDisable()) {
            textView.setTextColor(flowItem.getDisableItemTextColor());
            textView.setBackgroundResource(flowItem.getDisableItemBackground());
            textView.setEnabled(false);
        } else {
            textView.setEnabled(true);
            //标签未被禁用判断isCheck来决定背景设值
            if (flowItem.isCheck()) {
                textView.setTextColor(flowItem.getSelectedItemTextColor());
                textView.setBackgroundResource(flowItem.getSelectedItemBackground());
            } else {
                textView.setTextColor(flowItem.getDefaultItemTextColor());
                textView.setBackgroundResource(flowItem.getDefaultItemBackground());
            }
        }
        flowItem.setTagId(textView.getId());
        //显示以别名为准
        textView.setText(TextUtils.isEmpty(flowItem.getAlias()) ? flowItem.getTagName() : flowItem.getAlias());
        textView.setTag(flowItem);
        int minHP = PixelUtils.dip2px(getContext(), 5);
        int minVP = PixelUtils.dip2px(getContext(), 3);
        if (horizontalPadding < minHP) {
            horizontalPadding = minHP;
        }
        if (verticalPadding < minVP) {
            verticalPadding = minVP;
        }
        textView.setPadding((int) horizontalPadding, (int) verticalPadding, (int) horizontalPadding, (int) verticalPadding);
        this.addView(textView);
        flowItems.put(textView.getId(), flowItem);
        tagNamesIds.put((flowItem.getTagName() + "").trim(), textView.getId());
    }

    private <T> TagProperties<T> fromFlowItem(FlowItem flowItem) {
        TagProperties<T> properties = new TagProperties<>();
        properties.setDisable(flowItem.isDisable());
        properties.setDisableItemTextColor(flowItem.getDisableItemTextColor());
        properties.setText(flowItem.getTagName());
        properties.setAlias(flowItem.getAlias());
        properties.setSelectedItemTextColor(flowItem.getSelectedItemTextColor());
        properties.setDefaultItemTextColor(flowItem.getDefaultItemTextColor());
        properties.setData((T) flowItem.getData());
        properties.setDisableItemBackground(flowItem.getDisableItemBackground());
        properties.setSelectedItemBackground(flowItem.getSelectedItemBackground());
        properties.setDefaultItemBackground(flowItem.getDefaultItemBackground());
        properties.setCheck(flowItem.isCheck());
        return properties;
    }

    /**
     * 获取所有标签属性对象
     *
     * @param <T>
     * @return
     */
    public <T> List<TagProperties<T>> getAllItems() {
        List<TagProperties<T>> lst = new ArrayList<TagProperties<T>>();
        for (Map.Entry<Integer, FlowItem> entry : flowItems.entrySet()) {
            FlowItem value = entry.getValue();
            TagProperties<T> properties = fromFlowItem(value);
            lst.add(properties);
        }
        return lst;
    }

    public int getItemCount() {
        return flowItems.size();
    }

    /**
     * 获取选中项属性及数据对象
     *
     * @param <T>
     * @return
     */
    public <T> List<TagProperties<T>> getCheckItems() {
        List<TagProperties<T>> lst = new ArrayList<TagProperties<T>>();
        for (Map.Entry<Integer, FlowItem> entry : flowItems.entrySet()) {
            FlowItem value = entry.getValue();
            if (value.isCheck()) {
                TagProperties<T> properties = fromFlowItem(value);
                lst.add(properties);
            }
        }
        return lst;
    }

    /**
     * 根据标签名称获取属性
     *
     * @param tagName 标签名称
     * @param <T>
     * @return
     */
    public <T> TagProperties<T> getTagProperties(String tagName) {
        TagProperties<T> properties = new TagProperties<>();
        if (TextUtils.isEmpty(tagName)) {
            return properties;
        }
        tagName = tagName.trim();
        if (!tagNamesIds.containsKey(tagName)) {
            return properties;
        }
        int viewId = tagNamesIds.get(tagName);
        if (!flowItems.containsKey(viewId)) {
            return properties;
        }
        FlowItem flowItem = flowItems.get(viewId);
        if (flowItem == null) {
            return properties;
        }
        properties = fromFlowItem(flowItem);
        return properties;
    }

    /**
     * 重置标签
     *
     * @param properties 推荐通过getTagProperties(tagName)获取
     * @param <T>
     */
    public <T> void resetTag(TagProperties<T> properties) {
        if (properties == null || TextUtils.isEmpty(properties.getText())) {
            return;
        }
        String tagName = properties.getText().toString().trim();
        if (!tagNamesIds.containsKey(tagName)) {
            return;
        }
        int viewId = tagNamesIds.get(tagName);
        if (viewId == 0) {
            return;
        }
        View child = this.findViewById(viewId);
        if (child == null) {
            return;
        }
        FlowItem flowItem = fromTagProperties(properties);
        if (properties.isDisable()) {
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextColor(flowItem.getDisableItemTextColor());
                textView.setBackgroundResource(flowItem.getDisableItemBackground());
                textView.setEnabled(false);
            }
            child.setEnabled(false);
        } else {
            child.setEnabled(true);
            if (flowItem.isCheck()) {
                //设为选中状态
                if (child instanceof TextView) {
                    TextView textView = (TextView) child;
                    textView.setBackgroundResource(flowItem.getSelectedItemBackground());
                    textView.setTextColor(flowItem.getSelectedItemTextColor());
                }
                flowItem.setCheck(true);
                child.setTag(flowItem);
                if (flowItems.containsKey(child.getId())) {
                    FlowItem item = flowItems.get(child.getId());
                    item.setCheck(true);
                }
            } else {
                //设为未选中状态
                if (child instanceof TextView) {
                    TextView textView = (TextView) child;
                    textView.setBackgroundResource(flowItem.getDefaultItemBackground());
                    textView.setTextColor(flowItem.getDefaultItemTextColor());
                }
                flowItem.setCheck(false);
                child.setTag(flowItem);
                if (flowItems.containsKey(child.getId())) {
                    FlowItem item = flowItems.get(child.getId());
                    item.setCheck(false);
                }
            }
        }
    }

    /**
     * 删除标签
     *
     * @param tagName 标签名称
     */
    public void deleteTag(String tagName) {
        if (TextUtils.isEmpty(tagName)) {
            return;
        }
        tagName = tagName.trim();
        if (!tagNamesIds.containsKey(tagName)) {
            return;
        }
        int viewId = tagNamesIds.get(tagName);
        if (viewId == 0) {
            return;
        }
        View child = this.findViewById(viewId);
        this.removeView(child);
        tagNamesIds.remove(tagName);
        if (flowItems.containsKey(viewId)) {
            flowItems.remove(viewId);
        }
    }

    /**
     * 清空所有标签
     */
    public void clearAllTags() {
        this.removeAllViews();
        tagNamesIds.clear();
        flowItems.clear();
    }

    @Override
    public void onMeasureDynamicViewSize(int width, int height) {
        ViewGroup.LayoutParams params = this.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = height;
    }
}
