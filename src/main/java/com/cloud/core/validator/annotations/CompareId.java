package com.cloud.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/15
 * Description:比较id（用于区分字段的唯一标识）
 * Modifier:
 * ModifyContent:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CompareId {
    int sequence() default -1;
    /**
     * 标识字段唯一性
     *
     * @return
     */
    String value();
}
