package com.cloud.core.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cloud.core.logger.Logger;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/14
 * @Description:软键盘操作工具类
 * @Modifier:
 * @ModifyContent:
 */
public class SoftUtils {
    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(final Context context, final View view) {
        if (context == null || view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                imm.showSoftInput(view, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 获取软键盘状态
     *
     * @param context
     * @return
     */
    public static boolean isShowSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            //获取状态信息 true 打开
            if (imm == null) {
                return false;
            } else {
                return imm.isActive(view);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return false;
    }
}
