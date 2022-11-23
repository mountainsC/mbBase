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
abstract class Rule<VALIDATABLE> {
    protected final int mSequence;

    /**
     * Constructor.
     *
     * @param sequence The sequence number for this {@link Rule}.
     */
    protected Rule(final int sequence) {
        mSequence = sequence;
    }

    /**
     * Checks if the rule is valid.
     *
     * @param validatable Element on which the validation is applied, could be a data type or a
     *                    {@link android.view.View}.
     * @return true if valid, false otherwise.
     */
    public abstract boolean isValid(VALIDATABLE validatable);

    /**
     * Returns a failure message associated with the rule.
     *
     * @param context Any {@link Context} instance, usually an
     *                {@link android.app.Activity}.
     * @return A failure message.
     */
    public abstract String getMessage(Context context);

    /**
     * Returns the sequence of the {@link Rule}.
     *
     * @return The sequence.
     */
    public final int getSequence() {
        return mSequence;
    }
}
