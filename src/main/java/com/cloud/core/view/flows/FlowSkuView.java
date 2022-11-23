package com.cloud.core.view.flows;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/14
 * @Description:sku组合视图控件
 * @Modifier:
 * @ModifyContent:
 */
public class FlowSkuView extends LinearLayout {

    /**
     * 所有sku属性
     * 颜色
     * |   宝石蓝
     * |   银钻灰
     * 内存
     * |   6加64G
     * |   6加128G
     */
    private LinkedHashMap<SkuSepecItem, List<SkuItem>> skuList = new LinkedHashMap<SkuSepecItem, List<SkuItem>>();
    private HashMap<Integer, List<String>> effectiveSkuList = new HashMap<Integer, List<String>>();
    private HashMap<String, FlowItemsView> sepecskuattrs = new HashMap<String, FlowItemsView>();
    private int skuItemLayoutId = 0;
    private LinkedHashMap<String, Integer> sepecPosList = new LinkedHashMap<String, Integer>();
    private HashMap<String, SkuItem> sepecCheckSkuItems = new HashMap<String, SkuItem>();
    //组合选择sku分隔符
    private String combinationCheckSkuSplit = "/";
    private OnSkuItemChangeListener onSkuItemChangeListener = null;
    private String chechedSku = "";
    //原有效sku列表
    private List<String> rawEffectiveSkuList = null;
    //有效sku分隔符
    private String effectivSkuSplit = "";
    //最大高度
    private float maxWidth = 0;
    //最大宽度
    private float maxHeight = 0;

    public FlowSkuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowSkuView, 0, 0);
        maxWidth = a.getDimension(R.styleable.FlowSkuView_fsv_maxWidth, 0);
        maxHeight = a.getDimension(R.styleable.FlowSkuView_fsv_maxHeight, 0);
        a.recycle();
    }

    public void setOnSkuItemChangeListener(OnSkuItemChangeListener listener) {
        this.onSkuItemChangeListener = listener;
    }

    /**
     * 组合选择sku分隔符
     *
     * @param combinationCheckSkuSplit 默认为/
     */
    public void setCombinationCheckSkuSplit(String combinationCheckSkuSplit) {
        if (TextUtils.isEmpty(combinationCheckSkuSplit)) {
            return;
        }
        this.combinationCheckSkuSplit = combinationCheckSkuSplit;
    }

    /**
     * 设置所有sku属性
     * 颜色
     * |   宝石蓝
     * |   银钻灰
     * 内存
     * |   6加64G
     * |   6加128G
     *
     * @param skuList
     */
    public void setSkuList(LinkedHashMap<SkuSepecItem, List<SkuItem>> skuList) {
        if (ObjectJudge.isNullOrEmpty(skuList)) {
            return;
        }
        this.skuList = skuList;
    }

    public HashMap<SkuSepecItem, List<SkuItem>> getSkuList() {
        return this.skuList;
    }

    /**
     * @param skuItemLayoutId
     */
    public void setSkuItemLayoutId(int skuItemLayoutId) {
        this.skuItemLayoutId = skuItemLayoutId;
    }

    /**
     * 设置有效的sku
     * 全网通,樱粉金,6加64G,12
     * 全网通,摩卡金,6加128G,12
     * 全网通,樱粉金,6加128G,12
     *
     * @param effectiveSkuList 有效的sku组集合
     * @param split            effectiveSkuList分隔符
     */
    public void setEffectiveSkuList(List<String> effectiveSkuList, String split) {
        if (ObjectJudge.isNullOrEmpty(effectiveSkuList) || TextUtils.isEmpty(split)) {
            return;
        }
        this.rawEffectiveSkuList = effectiveSkuList;
        this.effectivSkuSplit = split;
    }

    private void checkSkuBindValus() {
        int size = skuList.size();
        List<String> lst = new ArrayList<String>();
        for (String skugroup : rawEffectiveSkuList) {
            List<String> list = ConvertUtils.toList(skugroup, effectivSkuSplit);
            if (size == list.size()) {
                lst.add(skugroup);
                for (int i = 0; i < list.size(); i++) {
                    String sku = list.get(i);
                    //过滤有效sku
                    if (effectiveSkuList.containsKey(i)) {
                        List<String> nlst = effectiveSkuList.get(i);
                        if (!nlst.contains(sku)) {
                            nlst.add(sku.trim());
                        }
                    } else {
                        List<String> nlst = new ArrayList<String>();
                        nlst.add(sku.trim());
                        effectiveSkuList.put(i, nlst);
                    }
                }
            }
        }
        rawEffectiveSkuList = lst;
    }

    /**
     * 绑定sku
     *
     * @param effectiveSku 初始化sku,结构与setEffectiveSkuList一致
     * @param split        effectiveSku分隔符
     */
    public void bind(String effectiveSku, String split) {
        if (ObjectJudge.isNullOrEmpty(skuList)) {
            return;
        }
        //如果sku布局id!=0则清空子集;
        //下面以skuItemLayoutId视图添加
        if (skuItemLayoutId != 0) {
            this.removeAllViews();
        }
        checkSkuBindValus();
        List<String> initEffectiveSkus = ConvertUtils.toList(effectiveSku, split);
        //判断初始值sku长度是否符合
        if (initEffectiveSkus.size() > skuList.size()) {
            int delcount = initEffectiveSkus.size() - skuList.size();
            for (int i = 1; i <= delcount; i++) {
                initEffectiveSkus.remove(initEffectiveSkus.size() - i);
            }
        } else if (initEffectiveSkus.size() < skuList.size()) {
            int addcount = skuList.size() - initEffectiveSkus.size();
            for (int i = 0; i < addcount; i++) {
                initEffectiveSkus.add("");
            }
        }
        for (Map.Entry<SkuSepecItem, List<SkuItem>> entry : skuList.entrySet()) {
            SkuSepecItem key = entry.getKey();
            if (TextUtils.isEmpty(key.getSepecTag())) {
                //如果标识为空则结束本次循环
                continue;
            }
            //准备每一组的sku视图
            List<View> views = null;
            if (skuItemLayoutId == 0) {
                views = ViewUtils.getViewsByTag(this, key.getSepecTag());
            } else {
                View skuView = View.inflate(getContext(), skuItemLayoutId, null);
                if (skuView instanceof ViewGroup) {
                    views = ViewUtils.getViewsByTag((ViewGroup) skuView, key.getSepecTag());
                    if (!ObjectJudge.isNullOrEmpty(views)) {
                        this.addView(skuView);
                    }
                }
            }
            if (ObjectJudge.isNullOrEmpty(views)) {
                continue;
            }
            //建立规格-索引关系
            sepecPosList.put(key.getSepecName(), sepecPosList.size());
            //初始化sku
            initSku(key, views, entry.getValue(), initEffectiveSkus);
        }
        skuChange(true, false, null, null);
    }

    private void initSku(SkuSepecItem sepecItem, List<View> views, List<SkuItem> skus, List<String> initEffectiveSkus) {
        for (View view : views) {
            if (view instanceof TextView) {
                //设置规格名称
                TextView textView = (TextView) view;
                textView.setText(sepecItem.getSepecName());
            } else if (view instanceof FlowItemsView) {
                //绑定规格属性
                FlowItemsView flowItemsView = (FlowItemsView) view;
                sepecskuattrs.put(sepecItem.getSepecName(), flowItemsView);
                flowItemsView.setMaxViewHeight(maxHeight);
                flowItemsView.setMaxViewWidth(maxWidth);
                //绑定sku
                bindSkuAttrs(flowItemsView, sepecItem, skus, initEffectiveSkus);
            }
        }
    }

    private void bindSkuAttrs(FlowItemsView flowItemsView, SkuSepecItem sepecItem, List<SkuItem> skus, List<String> initEffectiveSkus) {
        if (flowItemsView == null || ObjectJudge.isNullOrEmpty(skus)) {
            return;
        }
        for (SkuItem skuItem : skus) {
            TagProperties properties = flowItemsView.getDefaultTagProperties();
            properties.setText(skuItem.getSku().trim());
            properties.setAlias(skuItem.getAlias());
            properties.setData(skuItem.getData());
            //取值以sku的值为准
            String skuValue = TextUtils.isEmpty(skuItem.getSku()) ? skuItem.getAlias() : skuItem.getSku();
            //如果sku为有效项则启用否则禁用
            if (isContainerEffectiveSkuList(sepecItem.getSepecName(), skuValue)) {
                properties.setDisable(false);
                flowItemsView.setEnableCheck(true);
                //当前sku包含在初始值中则选中
                if (hasEffectiveSkus(initEffectiveSkus, sepecItem.getSepecName(), skuItem.getSku())) {
                    properties.setCheck(true);
                    sepecCheckSkuItems.put(sepecItem.getSepecName(), skuItem);
                } else {
                    properties.setCheck(false);
                    if (sepecCheckSkuItems.containsKey(sepecItem.getSepecName())) {
                        SkuItem sitem = sepecCheckSkuItems.get(sepecItem.getSepecName());
                        String sitemValue = TextUtils.isEmpty(sitem.getSku()) ? sitem.getAlias() : sitem.getSku();
                        if (TextUtils.equals(sitemValue, skuValue)) {
                            sepecCheckSkuItems.remove(sepecItem.getSepecName());
                        }
                    }
                }
            } else {
                properties.setDisable(true);
            }
            flowItemsView.setSepecItem(sepecItem);
            flowItemsView.setOnItemChangeListener(skuItemChangeListener);
            flowItemsView.addItem(properties);
        }
    }

    private boolean hasEffectiveSkus(List<String> initEffectiveSkus, String sepecName, String sku) {
        boolean flag = false;
        if (sepecPosList.containsKey(sepecName)) {
            int pos = sepecPosList.get(sepecName);
            if (pos < initEffectiveSkus.size()) {
                String esku = initEffectiveSkus.get(pos);
                if (TextUtils.equals(sku, esku)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean isContainerEffectiveSkuList(String sepecName, String skuValue) {
        boolean flag = false;
        if (sepecPosList.containsKey(sepecName)) {
            int pos = sepecPosList.get(sepecName);
            if (effectiveSkuList.containsKey(pos)) {
                List<String> list = effectiveSkuList.get(pos);
                if (list.contains(skuValue)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean isContainerCurrEnalbeSkus(HashMap<Integer, List<String>> currEnalbeSkus, String sepecName, String skuValue) {
        boolean flag = false;
        if (sepecPosList.containsKey(sepecName)) {
            int pos = sepecPosList.get(sepecName);
            if (currEnalbeSkus.containsKey(pos)) {
                List<String> list = currEnalbeSkus.get(pos);
                if (list.contains(skuValue)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private HashMap<Integer, List<String>> getCurrEnalbeSkus(int currSepecPos, SkuItem currCheckSku) {
        HashMap<Integer, List<String>> currEnableSkuList = new HashMap<Integer, List<String>>();
        for (String skugroup : rawEffectiveSkuList) {
            List<String> list = ConvertUtils.toList(skugroup, effectivSkuSplit);
            if (list.size() > currSepecPos) {
                String gsku = list.get(currSepecPos);
                if (TextUtils.equals(gsku, currCheckSku.getSku())) {
                    for (int i = 0; i < list.size(); i++) {
                        String sku = list.get(i);
                        //过滤有效sku
                        if (currEnableSkuList.containsKey(i)) {
                            List<String> nlst = currEnableSkuList.get(i);
                            if (!nlst.contains(sku)) {
                                nlst.add(sku);
                            }
                        } else {
                            List<String> nlst = new ArrayList<String>();
                            nlst.add(sku);
                            currEnableSkuList.put(i, nlst);
                        }
                    }
                }
            }
        }
        return currEnableSkuList;
    }

    private void showEffSkuByCheckItems(String currSepecName, SkuItem currCheckSku) {
        LinkedHashMap<String, SkuItem> checkSkus = getCheckSkus();
        if (!sepecPosList.containsKey(currSepecName)) {
            return;
        }
        int pos = sepecPosList.get(currSepecName);
        HashMap<Integer, List<String>> currEnalbeSkus = getCurrEnalbeSkus(pos, currCheckSku);
        for (Map.Entry<String, FlowItemsView> entry : sepecskuattrs.entrySet()) {
            FlowItemsView flowItemsView = entry.getValue();
            List<TagProperties<Object>> allItems = flowItemsView.getAllItems();
            boolean hasCheckItem = false;//当前规格下是否包含选中sku
            for (TagProperties<Object> allItem : allItems) {
                TagProperties properties = flowItemsView.getDefaultTagProperties();
                properties.setText(allItem.getText());
                properties.setAlias(allItem.getAlias());
                properties.setData(allItem.getData());
                if (isContainerEffectiveSkuList(entry.getKey(), allItem.getText())) {
                    if (TextUtils.equals(entry.getKey(), currSepecName)) {
                        //如果操作的是当前规格则当前项选中其它置为未可选状态
                        properties.setDisable(false);
                        //清除上一次选中sku
                        removeSkuCheckItem(entry.getKey());
                        //选择sku
                        if (TextUtils.equals(allItem.getText(), currCheckSku.getSku())) {
                            if (!isCheckSku(checkSkus, entry.getKey(), allItem.getText())) {
                                properties.setCheck(true);
                                hasCheckItem = true;
                            } else {
                                properties.setCheck(false);
                            }
                        } else {
                            properties.setCheck(false);
                        }
                    } else {
                        //如果操作非当前规格则禁用掉其它不可用sku
                        if (isContainerCurrEnalbeSkus(currEnalbeSkus, entry.getKey(), allItem.getText())) {
                            properties.setDisable(false);
                            //选择sku
                            if (isCheckSku(checkSkus, entry.getKey(), allItem.getText())) {
                                SkuItem item = flowItemsView.getSkuItemByTag(properties);
                                sepecCheckSkuItems.put(entry.getKey(), item);
                                properties.setCheck(true);
                                hasCheckItem = true;
                            } else {
                                properties.setCheck(false);
                            }
                        } else {
                            properties.setDisable(true);
                        }
                    }
                } else {
                    properties.setDisable(true);
                }
                flowItemsView.resetTag(properties);
            }
            //非当前操作规格
            if (!TextUtils.equals(entry.getKey(), currSepecName)) {
                removeCurrSepecWithoutSkuCheckItem(entry.getKey(), hasCheckItem);
            }
        }
    }

    private boolean isCheckSku(LinkedHashMap<String, SkuItem> checkSkus, String sepecName, String sku) {
        boolean flag = false;
        if (checkSkus.containsKey(sepecName)) {
            SkuItem skuItem = checkSkus.get(sepecName);
            if (skuItem != null) {
                if (TextUtils.equals(skuItem.getSku(), sku)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private OnItemChangeListener skuItemChangeListener = new OnItemChangeListener() {
        @Override
        public void onItemChange(View v, boolean isCheck, SkuSepecItem sepecItem, SkuItem skuItem) {
            showEffSkuByCheckItems(sepecItem.getSepecName(), skuItem);
            if (isCheck) {
                sepecCheckSkuItems.put(sepecItem.getSepecName(), skuItem);
            } else if (sepecCheckSkuItems.containsKey(sepecItem.getSepecName())) {
                sepecCheckSkuItems.remove(sepecItem.getSepecName());
            }
            skuChange(false, isCheck, sepecItem, skuItem);
        }
    };

    private void skuChange(boolean isInitialize, boolean isCheck, SkuSepecItem sepecItem, SkuItem skuItem) {
        StringBuffer checkSku = new StringBuffer();
        LinkedHashMap<String, SkuItem> checkSkus = getCheckSkus();
        int count = 0;
        for (Map.Entry<String, SkuItem> entry : checkSkus.entrySet()) {
            SkuItem value = entry.getValue();
            if (value == null) {
                continue;
            }
            //以别名为准
            String skuValue = TextUtils.isEmpty(value.getAlias()) ? value.getSku() : value.getAlias();
            checkSku.append(String.format("/%s", skuValue));
            count++;
        }
        if (checkSku.length() > 0) {
            checkSku.deleteCharAt(0);
        }
        chechedSku = checkSku.toString();
        if (onSkuItemChangeListener != null) {
            onSkuItemChangeListener.onSkuItemChange(chechedSku, count, isInitialize, isCheck, sepecItem, skuItem);
        }
    }

    public void bind() {
        this.bind("", "");
    }

    /**
     * 获取已选择sku(以分隔符分隔)
     *
     * @return
     */
    public String getChechedSku() {
        return chechedSku;
    }

    /**
     * 获取已选择sku列表
     *
     * @return
     */
    public List<String> getCheckedSkuList() {
        List<String> lst = new ArrayList<String>();
        LinkedHashMap<String, SkuItem> checkSkus = getCheckSkus();
        for (Map.Entry<String, SkuItem> entry : checkSkus.entrySet()) {
            SkuItem value = entry.getValue();
            String skuValue = TextUtils.isEmpty(value.getAlias()) ? value.getSku() : value.getAlias();
            lst.add(skuValue);
        }
        return lst;
    }

    /**
     * 是否选择所有规格sku
     *
     * @return true所有规格的sku已选择;false部分规格的sku未选择;
     */
    public boolean isCheckAllSepecSku() {
        return skuList.size() == sepecCheckSkuItems.size();
    }

    /**
     * 获取选中sku
     *
     * @return key-规格;value-SkuItem
     */
    public LinkedHashMap<String, SkuItem> getCheckSkus() {
        LinkedHashMap<String, SkuItem> lst = new LinkedHashMap<String, SkuItem>();
        for (Map.Entry<String, Integer> entry : sepecPosList.entrySet()) {
            if (sepecCheckSkuItems.containsKey(entry.getKey())) {
                SkuItem skuItem = sepecCheckSkuItems.get(entry.getKey());
                lst.put(entry.getKey(), skuItem);
            }
        }
        return lst;
    }

    /**
     * 获取选中规格对应的sku
     *
     * @return
     */
    public LinkedHashMap<SkuSepecItem, SkuItem> getCheckSepecSkus() {
        LinkedHashMap<SkuSepecItem, SkuItem> lst = new LinkedHashMap<SkuSepecItem, SkuItem>();
        for (Map.Entry<SkuSepecItem, List<SkuItem>> entry : skuList.entrySet()) {
            SkuSepecItem sepecItem = entry.getKey();
            if (sepecCheckSkuItems.containsKey(sepecItem.getSepecName())) {
                SkuItem skuItem = sepecCheckSkuItems.get(sepecItem.getSepecName());
                lst.put(entry.getKey(), skuItem);
            }
        }
        return lst;
    }

    /**
     * 获取选中规格tag-sku value
     *
     * @return
     */
    public LinkedHashMap<String, String> getCheckSepecTagsSkuValues() {
        LinkedHashMap<String, String> lst = new LinkedHashMap<String, String>();
        for (Map.Entry<SkuSepecItem, List<SkuItem>> entry : skuList.entrySet()) {
            SkuSepecItem sepecItem = entry.getKey();
            if (sepecCheckSkuItems.containsKey(sepecItem.getSepecName()) && !lst.containsKey(sepecItem.getTag())) {
                SkuItem skuItem = sepecCheckSkuItems.get(sepecItem.getSepecName());
                lst.put(sepecItem.getTag(), skuItem.getSku());
            }
        }
        return lst;
    }

    /**
     * 移除对应规格sku选中项
     *
     * @param sepecName 规格
     */
    private void removeSkuCheckItem(String sepecName) {
        if (sepecCheckSkuItems.containsKey(sepecName)) {
            sepecCheckSkuItems.remove(sepecName);
        }
    }

    /**
     * 除当前规格之外，如果没有一个选中sku项则清除选中列表中对应值
     *
     * @param sepecName     要清除的sku对应规格
     * @param hasCheckItems 当前规格下是否有选中sku
     */
    private void removeCurrSepecWithoutSkuCheckItem(String sepecName, boolean hasCheckItems) {
        if (!hasCheckItems) {
            removeSkuCheckItem(sepecName);
        }
    }

    /**
     * 根据规格获取sku列表
     *
     * @param tag 标识
     * @return
     */
    public List<SkuItem> getSkuListBySepec(String tag) {
        List<SkuItem> skuItems = new ArrayList<SkuItem>();
        for (Map.Entry<SkuSepecItem, List<SkuItem>> entry : skuList.entrySet()) {
            SkuSepecItem sepecItem = entry.getKey();
            if (TextUtils.equals(sepecItem.getTag(), tag)) {
                List<SkuItem> items = entry.getValue();
                skuItems.addAll(items);
                break;
            }
        }
        return skuItems;
    }

    /**
     * 根据连接符对规格-sku进行组合
     * (形如:颜色:灰色丨套餐:套餐一丨租赁天数:90天)
     *
     * @param sepecSkuJoin
     * @param splitJoin
     * @return
     */
    public String toJoinBySepecSku(String sepecSkuJoin, String splitJoin) {
        if (sepecSkuJoin == null) {
            sepecSkuJoin = "";
        }
        if (splitJoin == null) {
            splitJoin = "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sepecPosList.entrySet()) {
            if (sepecCheckSkuItems.containsKey(entry.getKey())) {
                SkuItem skuItem = sepecCheckSkuItems.get(entry.getKey());
                String skuValue = TextUtils.isEmpty(skuItem.getAlias()) ? skuItem.getSku() : skuItem.getAlias();
                sb.append(String.format("%s%s%s%s", entry.getKey(), sepecSkuJoin, skuValue, splitJoin));
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - splitJoin.length(), sb.length());
        }
        return sb.toString();
    }

    /**
     * 根据规格标识获取对应sku
     *
     * @param checkSepecSkus 规格-sku列表
     * @param sepecTag       规格标签名
     * @return sku
     */
    public SkuItem getSepecValueByTag(LinkedHashMap<SkuSepecItem, SkuItem> checkSepecSkus, String sepecTag) {
        SkuItem skuItem = new SkuItem();
        for (Map.Entry<SkuSepecItem, SkuItem> entry : checkSepecSkus.entrySet()) {
            SkuSepecItem key = entry.getKey();
            if (TextUtils.equals(key.getTag(), sepecTag)) {
                skuItem = entry.getValue();
                break;
            }
        }
        return skuItem;
    }
}
