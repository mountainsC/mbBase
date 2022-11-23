package com.cloud.core.view.flows;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/19
 * @Description:sku选择项改变监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnSkuItemChangeListener {
    /**
     * @param checkedSkus     已选择的sku
     * @param checkedSkuCount 选中sku数量
     * @param isInitialize    是否初始化
     * @param isCheckSku      当前 sku是否被选中
     * @param sepecItem       当前sku规格
     * @param skuItem         当前sku项
     */
    public void onSkuItemChange(String checkedSkus, int checkedSkuCount, boolean isInitialize, boolean isCheckSku, SkuSepecItem sepecItem, SkuItem skuItem);
}
