package com.cloud.core.validator;

import java.lang.reflect.Field;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
abstract class QuickRule<F extends Field> extends Rule<F> {
    /**
     * Default constructor.
     */
    protected QuickRule() {
        super(-1);
    }

    /**
     * Constructor.
     *
     * @param sequence A non-negative integer value.
     */
    protected QuickRule(final int sequence) {
        super(sequence);
    }

    /**
     * Checks if the rule is valid.
     *
     * @param field The  on which the rule has to be applied.
     * @return true if valid, false otherwise.
     */
    public abstract boolean isValid(F field);
}
