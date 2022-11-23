package com.cloud.core.validator.annotations;

import com.cloud.core.validator.rules.MaxRule;

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
@ValidateUsing(MaxRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Max {
    int sequence() default -1;

    //最大值
    int value() default Integer.MAX_VALUE;

    String message() default "";
}
