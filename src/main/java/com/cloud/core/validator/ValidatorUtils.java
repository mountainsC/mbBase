package com.cloud.core.validator;

import android.content.Context;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ValidatorUtils {

    private Context context = null;
    private Validator validator = null;

    private ValidatorUtils() throws Throwable {
        throw new Throwable("不能直接实例化");
    }

    private ValidatorUtils(Context context) {
        this.context = context;
        validator = new Validator();
    }

    /**
     * 获取验证类实例
     *
     * @param context 当前上下文
     * @return 验证工具类
     */
    public static ValidatorUtils getInstance(Context context) {
        ValidatorUtils validatorUtils = new ValidatorUtils(context);
        return validatorUtils;
    }

    /**
     * 验证
     *
     * @param model 注解实体对象
     */
    public void valid(Object model) {
        if (validator == null) {
            return;
        }
        validator.validate(model);
    }

    /**
     * 设置验证监听
     *
     * @param listener 验证监听
     */
    public void setOnValidationListener(OnValidationListener listener) {
        if (validator != null) {
            validator.setOnValidationListener(listener);
        }
    }
}
