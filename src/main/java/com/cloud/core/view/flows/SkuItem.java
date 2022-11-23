package com.cloud.core.view.flows;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/18
 * @Description:sku数据项
 * @Modifier:
 * @ModifyContent:
 */
public class SkuItem {
    /**
     * sku
     */
    private String sku = "";
    /**
     * sku别名
     */
    private String alias = "";
    /**
     * 携带数据
     */
    private Object data = null;

    public SkuItem() {

    }

    public SkuItem(String sku) {
        this.sku = sku;
    }

    public SkuItem(String sku, String alias) {
        this.sku = sku;
        this.alias = alias;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
