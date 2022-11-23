package com.cloud.core.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/25
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ExpireDetailListBean {
    /**
     * detail : 用户应在租赁届满之前，将租赁物及相关配件寄送至商户指定地址或自行送达商家门店，待商户收到商品后检测无问题后，平台返还用户冻结金额，该笔订单结束。
     * id : 5a68434918dbb81330b5f1e1
     * resume : 租赁到期可按商家提供的地址快递寄还;
     * title : 退换
     */

    private String detail;
    private String id;
    private String resume;
    private String title;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
