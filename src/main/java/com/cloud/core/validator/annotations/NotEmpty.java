package com.cloud.core.validator.annotations;

import com.cloud.core.validator.rules.NotEmptyRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:检测null或空字符串
 * Modifier:
 * ModifyContent:
 */
@ValidateUsing(NotEmptyRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotEmpty {
    int sequence() default -1;

    /**
     * null或空字符串时提示的信息
     *
     * @return 提示信息
     */
    String message() default "值不能为空";


}
