package com.cloud.core.bases;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.cloud.core.behavior.OnBehaviorEventStatistics;
import com.cloud.core.behavior.OnBehaviorStatistics;
import com.cloud.core.exception.CrashHandler;
import com.cloud.core.logger.Logger;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/3
 * @Description:Application基本
 * @Modifier:
 * @ModifyContent:
 */
public class BaseApplication extends Application {

    private static BaseApplication mbapp = null;

    //页面行为统计
    private OnBehaviorStatistics onBehaviorStatistics = null;
    //事件
    private OnBehaviorEventStatistics onBehaviorEventStatistics = null;
    //当前处于前台的activity数量
    private int countActivity = 0;
    //最近创建的activity
    private Activity recentlyCreateActivity = null;

    /**
     * 获取最近创建的activity
     *
     * @return
     */
    public Activity getRecentlyCreateActivity() {
        return recentlyCreateActivity;
    }

    protected void onAppSiwtchToBack() {

    }

    protected void onAppSiwtchToFront() {

    }

    /**
     * 设置行为统计监听
     *
     * @param behaviorStatistics
     */
    public void setOnBehaviorStatistics(OnBehaviorStatistics behaviorStatistics) {
        this.onBehaviorStatistics = behaviorStatistics;
    }

    public OnBehaviorStatistics getOnBehaviorStatistics() {
        return this.onBehaviorStatistics;
    }

    public void setOnBehaviorEventStatistics(OnBehaviorEventStatistics eventStatistics) {
        this.onBehaviorEventStatistics = eventStatistics;
    }

    public OnBehaviorEventStatistics getOnBehaviorEventStatistics() {
        return this.onBehaviorEventStatistics;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mbapp = this;
            CrashHandlerEx handlerEx = new CrashHandlerEx();
            handlerEx.init(this, getPackageName());
            registerActivityLifecycle();

            if (Build.VERSION.SDK_INT >= 24) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private class CrashHandlerEx extends CrashHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            super.uncaughtException(thread, ex);
        }
    }

    public static BaseApplication getInstance() {
        return mbapp == null ? new BaseApplication() : mbapp;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            onAppSiwtchToBack();
        }
    }

    private void registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                recentlyCreateActivity = activity;
                countActivity++;
                if (countActivity > 0) {
                    onAppSiwtchToFront();
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                countActivity--;
                if (countActivity == 0) {
                    onAppSiwtchToBack();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
