package com.cloud.core.validator.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/15
 * Description:比较规则
 * Modifier:
 * ModifyContent:
 */
public enum CompareSymbol {
    /**
     * 用于比较两个值是否相等(适用：数值 字符串 对象[===])
     */
    eq,
    /**
     * 大于(适用数值)
     */
    greater,
    /**
     * 小于(适用数值)
     */
    less;
}
