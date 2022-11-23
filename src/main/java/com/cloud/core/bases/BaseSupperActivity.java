package com.cloud.core.bases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.behavior.BehaviorUtils;
import com.cloud.core.ebus.EBus;
import com.cloud.core.update.UpdateFlow;
import com.cloud.core.utils.BundleMap;
import com.cloud.core.utils.BundleUtils;
import com.cloud.core.utils.ConvertUtils;
import com.cloud.core.utils.RxCachePool;
import com.cloud.core.utils.WinObjectUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
class BaseSupperActivity extends Activity {
    /**
     * 当前分页索引,当加载更多时对此索引加1表示加载下一页数据
     */
    protected int currPageIndex = 1;
    protected WinObjectUtils mwoutils = new WinObjectUtils();
//    private BehaviorUtils behaviorUtils = BehaviorUtils.getInstance();

    protected boolean isInitStatistics() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        behaviorUtils.onCreate(this, this, getClass());
//        if (isInitStatistics()) {
//            behaviorUtils.setStatisticsPreper(getWindow().getDecorView().getRootView());
//        }
        initStatusBar();
    }
    protected void initStatusBar(){
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.color_ffffff)
                .statusBarDarkFont(true)
                .init();
    }
    protected void reportStatisticsMap(View v, HashMap<String, String> map, int du) {
//        behaviorUtils.reportStatisticsMap(v, map, du);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 将分页索引currPageIndex进行初始化设置为1
     *
     * @return
     */
    protected int getCurrPageIndex() {
        return currPageIndex = 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UpdateFlow updateFlow = mwoutils.getUpdateFlow();
        if (requestCode == updateFlow.INSTALL_REQUEST_CODE) {
            updateFlow.againInstall();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mwoutils.onResume(this);
//        behaviorUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        behaviorUtils.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        behaviorUtils.onStart(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        behaviorUtils.onDestory(this);
        //移除缓存中的跳转标记
        String simpleName = this.getClass().getSimpleName();
        RxCachePool.getInstance().clearObject(simpleName);
        //注销EBus
        EBus.getInstance().unregister(this);
    }

    /**
     * 获取bundle对象
     *
     * @return
     */
    public Bundle getBundle() {
        Intent intent = getIntent();
        if (intent == null) {
            return new Bundle();
        } else {
            Bundle bundle = intent.getExtras();
            return bundle == null ? new Bundle() : bundle;
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

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @return
     */
    public long getLongBundle(String key) {
        return getLongBundle(key, 0);
    }

    /**
     * 获取bundle map集合
     * 调用方法startActivity时传入的参数
     *
     * @return
     */
    protected static BundleMap getBundleMap() {
        BundleMap bundleMap = new BundleMap();
        return bundleMap;
    }

    /**
     * 设置对象至bundle
     *
     * @param bundle 当前bundle
     * @param key    数据键
     * @param object 需要设置的数据
     */
    public void setObjectBundle(Bundle bundle, String key, Object object) {
        BundleUtils.setBundleValue(bundle, key, object);
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
