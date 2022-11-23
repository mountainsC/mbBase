package com.cloud.core.validator.annotations;


import com.cloud.core.validator.rules.PatternRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/15
 * Description:
 * Modifier:
 * ModifyContent:
 */
@ValidateUsing(PatternRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pattern {
    int sequence() default -1;

    /**
     * 验证通过的正则
     *
     * @return
     */
    String regexp() default "";

    /**
     * 对regexp验证不通过显示的错误信息
     *
     * @return
     */
    String message() default "";
}
