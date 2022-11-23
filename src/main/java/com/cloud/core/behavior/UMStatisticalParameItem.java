package com.cloud.core.behavior;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/1/11
 * @Description:友盟统计参数数据项
 * @Modifier:
 * @ModifyContent:
 */
public class UMStatisticalParameItem {
    /**
     * 统计节点名
     */
    private String nodeName = "";
    /**
     * 统计节点值
     */
    private String nodeValue = "";

    /**
     * 获取统计节点名
     */
    public String getNodeName() {
        if (nodeName == null) {
            nodeName = "";
        }
        return nodeName;
    }

    /**
     * 设置统计节点名
     *
     * @param nodeName
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 获取统计节点值
     */
    public String getNodeValue() {
        if (nodeValue == null) {
            nodeValue = "";
        }
        return nodeValue;
    }

    /**
     * 设置统计节点值
     *
     * @param nodeValue
     */
    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }
}
