package com.cloud.core.bases;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;


import androidx.fragment.app.Fragment;

import com.cloud.core.behavior.BehaviorUtils;
import com.cloud.core.ebus.EBus;
import com.cloud.core.hvlayout.HeaderScrollHelper;
import com.cloud.core.update.UpdateFlow;
import com.cloud.core.utils.WinObjectUtils;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
@SuppressLint("ValidFragment")
class BaseSupperFragment extends Fragment implements HeaderScrollHelper.ScrollableContainer {

    private WinObjectUtils mwoutils = new WinObjectUtils();

    protected boolean isInitStatistics() {
        return false;
    }

    /**
     * 当前分页索引
     */
    protected int currPageIndex = 1;
    public UpdateFlow ubll = new UpdateFlow();
    private BehaviorUtils behaviorUtils = new BehaviorUtils();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isInitStatistics()) {
            behaviorUtils.onCreate(getContext(), this, getClass());
            behaviorUtils.setStatisticsPreper(view);
        }
    }

    protected void reportStatisticsMap(View v, HashMap<String, String> map, int du) {
        behaviorUtils.reportStatisticsMap(v, map, du);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View findViewById(int id) {
        return getActivity().findViewById(id);
    }

    protected int getCurrPageIndex() {
        return currPageIndex = 1;
    }

    /**
     * 实例Fragment对象
     *
     * @param t    要实例的Fragment类对象
     * @param args bundle对象
     * @param <T>
     * @return
     */
    public static <T extends BaseFragment> T newInstance(T t, Bundle args) {
        if (args != null) {
            t.setArguments(args);
        }
        return t;
    }

    /**
     * 实例Fragment对象
     *
     * @param t   要实例的Fragment类对象
     * @param <T>
     * @return
     */
    public static <T extends BaseFragment> T newInstance(T t) {
        return newInstance(t, (Bundle) null);
    }

    @Override
    public void onResume() {
        super.onResume();
        behaviorUtils.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        behaviorUtils.onPause(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        behaviorUtils.onStart(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        behaviorUtils.onDestory(this);
        //注销EBus
        EBus.getInstance().unregister(this);
    }


    @Override
    public View getScrollableView() {
        return null;
    }
}
