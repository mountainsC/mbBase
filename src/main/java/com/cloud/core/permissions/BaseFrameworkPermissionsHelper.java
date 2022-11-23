package com.cloud.core.permissions;

import android.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * Implementation of {@link PermissionHelper} for framework host classes.
 */
public abstract class BaseFrameworkPermissionsHelper<T> extends PermissionHelper<T> {

    public BaseFrameworkPermissionsHelper(@NonNull T host) {
        super(host);
    }

    public abstract FragmentManager getFragmentManager();

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               @NonNull String positiveButton,
                                               @NonNull String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode,
                                               @NonNull String... perms) {
        RationaleDialogFragment
                .newInstance(positiveButton, negativeButton, rationale, theme, requestCode, perms)
                .showAllowingStateLoss(getFragmentManager(), RationaleDialogFragment.TAG);
    }
}
