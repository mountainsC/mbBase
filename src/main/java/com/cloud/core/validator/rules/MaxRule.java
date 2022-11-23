package com.cloud.core.validator.rules;

import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.Max;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class MaxRule extends AnnotationRule<Max, Object> {

    public MaxRule(Max max) {
        super(max);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        int maxValue = mRuleAnnotation.value();
        if (value instanceof Integer) {
            return ConvertUtils.toInt(value) <= maxValue;
        } else if (value instanceof Float) {
            return ConvertUtils.toFloat(value) <= maxValue;
        } else if (value instanceof Double) {
            return ConvertUtils.toDouble(value) <= maxValue;
        } else {
            return ConvertUtils.toLong(value) <= maxValue;
        }
    }
}
