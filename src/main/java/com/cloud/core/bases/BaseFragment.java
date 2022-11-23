package com.cloud.core.bases;

import android.os.Bundle;
import android.view.View;

import com.cloud.core.ObjectJudge;
import com.cloud.core.utils.BundleMap;
import com.cloud.core.utils.ConvertUtils;

import java.util.ArrayList;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/4
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseFragment extends BaseSupperFragment {


    public Bundle getBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return new Bundle();
        } else {
            return bundle;
        }
    }

    /**
     * 从bundle中获取字符串
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(Bundle bundle, String key, String defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object o = bundle.get(key);
            if (o == null) {
                return defaultValue;
            }
            return String.valueOf(o);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(String key, String defaultValue) {
        Bundle bundle = getBundle();
        return getStringBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @return
     */
    public String getStringBundle(String key) {
        return getStringBundle(key, "");
    }

    /**
     * 从bundle中获取int值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(Bundle bundle, String key, int defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toInt(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(String key, int defaultValue) {
        Bundle bundle = getBundle();
        return getIntBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @return
     */
    public int getIntBundle(String key) {
        return getIntBundle(key, 0);
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(Bundle bundle, String key, boolean defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ObjectJudge.isTrue(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(String key, boolean defaultValue) {
        Bundle bundle = getBundle();
        return getBooleanBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @return
     */
    public boolean getBooleanBundle(String key) {
        return getBooleanBundle(key, false);
    }

    /**
     * 从bundle中获取object值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(Bundle bundle, String key, Object defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.get(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(String key, Object defaultValue) {
        Bundle bundle = getBundle();
        return getObjectBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @return
     */
    public Object getObjectBundle(String key) {
        return getObjectBundle(key, null);
    }

    /**
     * 从bundle中获取float值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(Bundle bundle, String key, float defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toFloat(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(String key, float defaultValue) {
        Bundle bundle = getBundle();
        return getFloatBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @return
     */
    public float getFloatBundle(String key) {
        return getFloatBundle(key, 0);
    }

    /**
     * 从bundle中获取double值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(Bundle bundle, String key, double defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toDouble(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(String key, double defaultValue) {
        Bundle bundle = getBundle();
        return getDoubleBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @return
     */
    public double getDoubleBundle(String key) {
        return getDoubleBundle(key, 0);
    }

    /**
     * 从bundle中获取long值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(Bundle bundle, String key, long defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toLong(value, (int) defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(String key, long defaultValue) {
        Bundle bundle = getBundle();
        return getLongBundle(bundle, key, defaultValue);
    }

    public long getLongBundle(String key) {
        return getLongBundle(key, 0);
    }

    /**
     * Fragment回调方法,在其它页面拿到当前Fragment对象后调用onFragmentCall时执行
     *
     * @param action    区分是哪一次回调
     * @param bundleMap BundleMap参数集合
     */
    public void onFragmentCall(String action, BundleMap bundleMap) {

    }

    /**
     * Fragment需要通过此回调返回具体滚动的视图
     *
     * @return
     */
    @Override
    public View getScrollableView() {
        return null;
    }

    protected static BundleMap getBundleMap() {
        BundleMap bundleMap = new BundleMap();
        return bundleMap;
    }

    /**
     * 设置integer类型数据列表
     *
     * @param key  数据键
     * @param list 需要设置的数据
     */
    public void setIntegerArrayList(String key, ArrayList<Integer> list) {
        Bundle bundle = getBundle();
        bundle.putIntegerArrayList(key, list);
    }

    /**
     * integer类型数据列表
     *
     * @param key 数据键
     * @return
     */
    public ArrayList<Integer> getIntegerArrayList(String key) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            ArrayList<Integer> list = bundle.getIntegerArrayList(key);
            return list == null ? new ArrayList<Integer>() : list;
        } else {
            return new ArrayList<Integer>();
        }
    }

    /**
     * 设置String类型数据列表
     *
     * @param key  数据键
     * @param list 需要设置的数据
     */
    public void setStringArrayList(String key, ArrayList<String> list) {
        Bundle bundle = getBundle();
        bundle.putStringArrayList(key, list);
    }

    /**
     * String类型数据列表
     *
     * @param key 数据键
     * @return
     */
    public ArrayList<String> getStringArrayList(String key) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            ArrayList<String> list = bundle.getStringArrayList(key);
            return list == null ? new ArrayList<String>() : list;
        } else {
            return new ArrayList<String>();
        }
    }
}
