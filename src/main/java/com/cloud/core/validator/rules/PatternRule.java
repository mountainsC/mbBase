package com.cloud.core.validator.rules;

import com.cloud.core.utils.ValidUtils;
import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.Pattern;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class PatternRule extends AnnotationRule<Pattern, Object> {

    public PatternRule(final Pattern pattern) {
        super(pattern);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        String s = String.valueOf(value);
        return ValidUtils.valid(mRuleAnnotation.regexp(), s);
    }
}
