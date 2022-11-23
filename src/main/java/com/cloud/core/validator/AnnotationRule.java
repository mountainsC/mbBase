package com.cloud.core.validator;

import android.content.Context;

import java.lang.annotation.Annotation;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:注解规则
 * Modifier:
 * ModifyContent:
 */
public abstract class AnnotationRule<RULE_ANNOTATION extends Annotation, DATA_TYPE> extends Rule<DATA_TYPE> {
    protected final RULE_ANNOTATION mRuleAnnotation;

    /**
     * Constructor. It is mandatory that all subclasses MUST have a constructor with the same
     * signature.
     *
     * @param ruleAnnotation The rule {@link Annotation} instance to which
     *                       this rule is paired.
     */
    protected AnnotationRule(final RULE_ANNOTATION ruleAnnotation) {
        super(ruleAnnotation != null
                ? Reflector.getAttributeValue(ruleAnnotation, "sequence", Integer.TYPE) : -1);
        if (ruleAnnotation == null) {
            throw new IllegalArgumentException("'ruleAnnotation' cannot be null.");
        }
        mRuleAnnotation = ruleAnnotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(final Context context) {
        final int messageResId = Reflector.getAttributeValue(mRuleAnnotation, "messageResId",
                Integer.class);

        return messageResId != -1
                ? context.getString(messageResId)
                : Reflector.getAttributeValue(mRuleAnnotation, "message", String.class);
    }
}
