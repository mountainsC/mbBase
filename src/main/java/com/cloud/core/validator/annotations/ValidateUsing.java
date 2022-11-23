package com.cloud.core.validator.annotations;

import com.cloud.core.validator.AnnotationRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ValidateUsing {
    Class<? extends AnnotationRule> value();
}
