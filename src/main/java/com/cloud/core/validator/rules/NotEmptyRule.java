package com.cloud.core.validator.rules;


import com.cloud.core.validator.AnnotationRule;
import com.cloud.core.validator.annotations.NotEmpty;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class NotEmptyRule extends AnnotationRule<NotEmpty, Object> {

    public NotEmptyRule(NotEmpty notEmpty) {
        super(notEmpty);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.trim().length() == 0) {
                return false;
            }
        }
        return true;
    }
}
