package com.cloud.core.validator.rules;

import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.Min;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class MinRule extends AnnotationRule<Min, Object> {

    public MinRule(final Min min) {
        super(min);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        int minValue = mRuleAnnotation.value();
        if (value instanceof Integer) {
            return ConvertUtils.toInt(value) >= minValue;
        } else if (value instanceof Float) {
            return ConvertUtils.toFloat(value) >= minValue;
        } else if (value instanceof Double) {
            return ConvertUtils.toDouble(value) >= minValue;
        } else {
            return ConvertUtils.toLong(value) >= minValue;
        }
    }
}
