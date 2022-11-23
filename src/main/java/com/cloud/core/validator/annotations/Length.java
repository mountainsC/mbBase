package com.cloud.core.validator.annotations;

import com.cloud.core.validator.rules.LengthRule;

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
@ValidateUsing(LengthRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
    int sequence() default -1;

    /**
     * 最小
     *
     * @return
     */
    int min() default 0;

    /**
     * 最大
     *
     * @return
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 错误消息
     *
     * @return
     */
    String message() default "";
}
