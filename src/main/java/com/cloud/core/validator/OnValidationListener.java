package com.cloud.core.validator;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnValidationListener {
    /**
     * Called when all {@link Rule}s pass.
     */
    void onValidationSucceeded();

    /**
     * @param message error message.
     */
    void onValidationFailed(String message);
}
