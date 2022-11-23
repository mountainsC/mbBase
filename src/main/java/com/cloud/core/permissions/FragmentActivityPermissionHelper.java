package com.cloud.core.permissions;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.cloud.core.bases.BaseFragmentActivity;

/**
 * Permissions helper for {@link Activity}.
 */
class FragmentActivityPermissionHelper extends BaseFrameworkPermissionsHelper<BaseFragmentActivity> {

    public FragmentActivityPermissionHelper(BaseFragmentActivity host) {
        super(host);
    }

    @Override
    public FragmentManager getFragmentManager() {
        return getHost().getFragmentManager();
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        ActivityCompat.requestPermissions(getHost(), perms, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(), perm);
    }

    @Override
    public Context getContext() {
        return getHost();
    }
}
