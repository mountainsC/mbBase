package com.cloud.core.validator.rules;


import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.Length;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class LengthRule extends AnnotationRule<Length, Object> {


    public LengthRule(Length length) {
        super(length);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        String str = String.valueOf(value);
        int len = str.trim().length();
        if (len == 0) {
            return false;
        }
        int min = mRuleAnnotation.min();
        int max = mRuleAnnotation.max();
        if (max < min) {
            return false;
        }
        if (min > 0 && len < min) {
            return false;
        }
        if (max > 0 && len > max) {
            return false;
        }
        return true;
    }
}
