package com.cloud.core.refresh.api;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * 默认Header创建器
 * Created by SCWANG on 2017/5/26.
 */

public interface DefaultRefreshHeaderCreater {
    RefreshHeader createRefreshHeader(Context context, RefreshLayout layout);
}