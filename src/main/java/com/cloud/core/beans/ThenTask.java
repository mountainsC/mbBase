package com.cloud.core.beans;

import android.os.Handler;
import android.text.TextUtils;

import com.cloud.core.events.Action1;
import com.cloud.core.events.Func0;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/9
 * @Description:若传入的对象或值==null,那等待一定时间后再执行;否则即刻执行;
 * @Modifier:
 * @ModifyContent:
 */
public class ThenTask<T> {

    private Handler handler = new Handler();
    private Func0<T> paramFunc = null;
    private Action1<T> complete = null;
    private int what = 627694562;
    private long delayMillis = 200;
    //最多检测次数
    private int checkTimes = 5;
    private int currTimes = 0;

    public void task(Func0<T> paramFunc, Action1<T> complete) {
        this.paramFunc = paramFunc;
        this.complete = complete;
        if (paramFunc == null || complete == null) {
            return;
        }
        T value = paramFunc.call();
        if (isEmpty()) {
            currTimes++;
            handler.postDelayed(new TaskRunable(), delayMillis);
        } else {
            complete.call(value);
        }
    }

    private boolean isEmpty() {
        T value = paramFunc.call();
        if (value == null) {
            return true;
        } else if (value instanceof Integer) {
            if ((Integer) value == 0) {
                return true;
            }
        } else if (value instanceof Float) {
            if ((Float) value == 0) {
                return true;
            }
        } else if (value instanceof Double) {
            if ((Double) value == 0) {
                return true;
            }
        } else if (value instanceof Long) {
            if ((Long) value == 0) {
                return true;
            }
        } else if (value instanceof Short) {
            if ((Short) value == 0) {
                return true;
            }
        } else if (value instanceof String) {
            if (TextUtils.isEmpty((String) value)) {
                return true;
            }
        }
        return false;
    }

    private class TaskRunable implements Runnable {
        @Override
        public void run() {
            if (currTimes >= checkTimes) {
                return;
            }
            if (isEmpty()) {
                task(paramFunc, complete);
            } else {
                complete.call(paramFunc.call());
            }
        }
    }


}
