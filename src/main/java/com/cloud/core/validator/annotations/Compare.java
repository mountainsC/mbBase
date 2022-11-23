package com.cloud.core.validator.annotations;


import com.cloud.core.validator.enums.CompareSymbol;
import com.cloud.core.validator.rules.CompareRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/15
 * Description:与其它字段比较注解属性
 * Modifier:
 * ModifyContent:
 */
@ValidateUsing(CompareRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Compare {
    int sequence() default -1;

    /**
     * 引用id
     *
     * @return
     */
    String refId();

    /**
     * 操作符号{@link CompareSymbol} value
     *
     * @return
     */
    CompareSymbol symbol() default CompareSymbol.eq;

    /**
     * 验证不符条件时提示信息
     *
     * @return
     */
    String message() default "";
}
