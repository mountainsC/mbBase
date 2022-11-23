package com.cloud.core.validator.annotations;

import com.cloud.core.validator.rules.MinRule;

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
@ValidateUsing(MinRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Min {
    int sequence() default -1;

    //最小值
    int value();

    String message() default "";
}
