package com.cloud.core.annotations;

import com.cloud.core.enums.RequestContentType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/6
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DELETE {
    /**
     * 相对地址
     * <p>
     * return
     */
    String value() default "";

    /**
     * 相对地址库
     * <p>
     * return
     */
    UrlItem[] values() default {};

    /**
     * 若为true,则默认不添加此请求下的所有字段
     * <p>
     * return
     */
    boolean isRemoveEmptyValueField() default false;

    /**
     * 是否完整url
     * <p>
     * return
     */
    boolean isFullUrl() default false;

    /**
     * 数据提交方式
     * <p>
     * return
     */
    RequestContentType contentType() default RequestContentType.None;

    /**
     * 若ApiCheckAnnotation中的IsTokenValid=true时才有效
     * true:接口返回后需要做登录检验;false:忽略检验;
     * <p>
     * return
     */
    boolean isLoginValid() default true;

    /**
     * 是否输出api日志
     * <p>
     * return
     */
    boolean isPrintApiLog() default false;

    /**
     * 是否验证回调结果
     * true:对返回的结果做以下验证:
     * 1.登录;
     * 2.api异常;
     * false:只对登录做过滤其它不处理;
     *
     * @return
     */
    boolean isValidCallResult() default true;
}
