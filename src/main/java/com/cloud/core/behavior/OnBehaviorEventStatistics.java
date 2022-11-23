package com.cloud.core.behavior;

import android.content.Context;
import android.view.View;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/15
 * @Description:行为事件统计
 * @Modifier:
 * @ModifyContent:
 */
public interface OnBehaviorEventStatistics {
    public void onEvent(Context context, View v, String eventId, String classNameUrl, BehaviorType behaviorType, HashMap<String, String> map, int du);
}
