/**
 * Title: ObjectJudge.java
 * Description:
 * author: lijinghuan
 * data: 2015年5月4日 上午7:16:34
 */
package com.cloud.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cloud.core.events.Action1;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.FilenameUtils;
import com.cloud.core.utils.GlobalUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class ObjectJudge {
    /**
     * 判断列表是否为空
     * <p>
     * param list 需要检测的列表集合
     * return
     */
    public static <T> Boolean isNullOrEmpty(T[] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 需要检测的列表集合
     * return
     */
    public static <K, V> Boolean isNullOrEmpty(TreeMap<K, V> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(HashSet<?> haslist) {
        if (haslist != null && !haslist.isEmpty() && haslist.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(HashMap<?, ?> haslist) {
        if (haslist != null && !haslist.isEmpty() && haslist.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param haslist 需要检测的列表集合
     * return
     */
    public static Boolean isNullOrEmpty(Hashtable<?, ?> haslist) {
        if (haslist != null && !haslist.isEmpty() && haslist.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 整型数据集合
     * return
     */
    public static Boolean isNullOrEmpty(int[] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param longlist 长整型集合
     * return
     */
    public static Boolean isNullOrEmpty(long[] longlist) {
        if (longlist != null && longlist.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 列表集合
     * return
     */
    public static Boolean isNullOrEmpty(List<?> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 数据集合
     * return
     */
    public static Boolean isNullOrEmpty(Collection<?> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断列表是否为空
     * <p>
     * param list 数据列表
     * return
     */
    public static Boolean isNullOrEmpty(String[][] list) {
        if (list != null && list.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断服务是否正在运行
     * <p>
     * param context
     * param className
     * return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(100000);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 是否在后台运行
     * <p>
     * param context
     * return
     */
    public static boolean isBackgroundRunning(Context context) {
        try {
            String processName = context.getPackageName();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager == null)
                return false;
            List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                    .getRunningAppProcesses();
            if (ObjectJudge.isNullOrEmpty(processList)) {
                return false;
            }
            Boolean flag = false;
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                if (process.processName.startsWith(processName)) {
                    boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
                            && process.importance != IMPORTANCE_VISIBLE;
                    flag = isBackground;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 是否中文字符
     * <p>
     * param chineseStr
     * return
     */
    public static boolean isChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
                    || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为真
     * <p>
     * param object true或大于0都为真,否则为假
     * return
     */
    public static boolean isTrue(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return (boolean) object;
        } else {
            String flag = object.toString().trim().toLowerCase();
            if (TextUtils.equals(flag, "true")) {
                return true;
            } else if (ConvertUtils.toInt(flag) > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断是否银行卡卡号
     *
     * @param bankCard 卡号
     * @return
     */
    public static boolean isBankCard(String bankCard) {
        if (bankCard == null) {
            return false;
        }
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0 || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断字符串是否为空
     *
     * @param content
     * @return
     */
    public static boolean isEmptyString(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else {
            content = content.toLowerCase();
            if (TextUtils.equals(content, "null") ||
                    TextUtils.equals(content, "(null)")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断activity是否正在运行
     *
     * @param context          上下文
     * @param className        短类名或长类名
     * @param isShortClassName true-表示className为短类名;false-表示className为长类名;
     * @return
     */
    public static boolean isRunningActivity(Context context, String className, boolean isShortClassName) {
        try {
            if (TextUtils.isEmpty(className)) {
                return false;
            }
            Context applicationContext = context.getApplicationContext();
            String packageName = applicationContext.getPackageName();
            if (!isShortClassName) {
                className = FilenameUtils.getName(className);
            }
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100000);
            boolean isRunning = false;
            for (ActivityManager.RunningTaskInfo info : list) {
                if (TextUtils.equals(info.topActivity.getShortClassName(), className) ||
                        TextUtils.equals(info.baseActivity.getShortClassName(), className)) {
                    if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                        isRunning = true;
                        break;
                    }
                }
            }
            return isRunning;
        } catch (SecurityException e) {
            Logger.L.error(e);
        }
        return false;
    }

    /**
     * 是否包含sdk卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断广播是否已经注册
     *
     * @param context
     * @param action  广播action
     * @return
     */
    public static boolean isRegisterBroadcast(Context context, String action) {
        if (context == null || TextUtils.isEmpty(action)) {
            return false;
        }
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfos = manager.queryBroadcastReceivers(intent, 0);
        return !ObjectJudge.isNullOrEmpty(resolveInfos);
    }

    /**
     * 是否匹配字段和属性值
     *
     * @param t
     * @param field         属性字段
     * @param propertyValue 属性值
     * @param <T>
     * @return
     */
    public static <T> boolean isMatchPropertyValue(T t, String field, Object propertyValue) {
        if (t == null || TextUtils.isEmpty(field) || propertyValue == null) {
            return false;
        }
        Object value = GlobalUtils.getPropertiesValue(t, field);
        if (value == null) {
            return false;
        }
        if (value instanceof String ||
                value instanceof Integer ||
                value instanceof Double ||
                value instanceof Float) {
            String old = String.valueOf(value);
            String now = String.valueOf(propertyValue);
            return TextUtils.equals(String.valueOf(value), String.valueOf(propertyValue));
        } else {
            return value == propertyValue;
        }
    }

    /**
     * 实体对象是否匹配所有的字段和属性值
     *
     * @param t
     * @param fieldValues 字段和属性值
     * @param <T>
     * @return
     */
    public static <T> boolean isMatchPropertyValues(T t, LinkedHashMap<String, String> fieldValues) {
        if (ObjectJudge.isNullOrEmpty(fieldValues)) {
            return false;
        }
        int count = 0;
        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
            boolean isMatch = isMatchPropertyValue(t, entry.getKey(), entry.getValue());
            if (isMatch) {
                count++;
            }
        }
        return count == fieldValues.size();
    }

    /**
     * 监听所有编辑文本是否非空并在回调中返回
     *
     * @param action    检测完成后回调
     * @param editTexts 编辑文本集合
     */
    public static void checkAllEditTextNotEmpty(Action1<Boolean> action, EditText... editTexts) {
        if (action == null || ObjectJudge.isNullOrEmpty(editTexts)) {
            return;
        }
        HashMap<Integer, String> maps = new HashMap<Integer, String>();
        class InputTextWatcher implements TextWatcher {

            private int position = 0;
            private HashMap<Integer, String> maps = null;
            private Action1<Boolean> action = null;

            public InputTextWatcher(int position, HashMap<Integer, String> maps, Action1<Boolean> action) {
                this.position = position;
                this.maps = maps;
                this.action = action;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (maps == null) {
                    return;
                }
                maps.put(position, text == null ? "" : text.toString());
                boolean flag = true;
                for (Map.Entry<Integer, String> entry : maps.entrySet()) {
                    if (TextUtils.isEmpty(entry.getValue())) {
                        flag = false;
                        break;
                    }
                }
                action.call(flag);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
        for (int i = 0; i < editTexts.length; i++) {
            EditText editText = editTexts[i];
            maps.put(i, "");
            editText.addTextChangedListener(new InputTextWatcher(i, maps, action));
        }
    }

    /**
     * 判断当前执行的方法或线程是否属于主线程
     *
     * @return true-主线程;false-非主线程;
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
