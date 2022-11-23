package com.cloud.core.permissions;


import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentManager;

/**
 * Implementation of {@link PermissionHelper} for Support Library host classes.
 */
public abstract class BaseSupportPermissionsHelper<T> extends PermissionHelper<T> {

    public BaseSupportPermissionsHelper(@NonNull T host) {
        super(host);
    }

    public abstract FragmentManager getSupportFragmentManager();

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               @NonNull String positiveButton,
                                               @NonNull String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode,
                                               @NonNull String... perms) {
        RationaleDialogFragmentCompat
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, perms)
                .showAllowingStateLoss(getSupportFragmentManager(), RationaleDialogFragmentCompat.TAG);
    }
}
