package com.cloud.core.view.flows;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/18
 * @Description:sku规格数据项
 * @Modifier:
 * @ModifyContent:
 */
public class SkuSepecItem {
    /**
     * 规格标识
     */
    private String sepecTag = "";
    /**
     * 规格名称
     */
    private String sepecName = "";
    /**
     * 标识
     */
    private String tag = "";

    public String getSepecTag() {
        return sepecTag;
    }

    public void setSepecTag(String sepecTag) {
        this.sepecTag = sepecTag;
    }

    public String getSepecName() {
        return sepecName;
    }

    public void setSepecName(String sepecName) {
        this.sepecName = sepecName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
