package com.cloud.core.view.vlayout;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.cloud.core.ObjectJudge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/5
 * @Description:布局构建器
 * @Modifier:
 * @ModifyContent:
 */
public class VLayoutBuilder {
    private Map<Integer, List<SubAdapter>> groupMaps = new TreeMap<Integer, List<SubAdapter>>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    private VirtualLayoutManager virtualLayoutManager = null;
    private DelegateAdapter delegateAdapter = null;
    private boolean isBuild = false;

    public DelegateAdapter build(Context context) {
        virtualLayoutManager = new VirtualLayoutManager(context);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        this.isBuild = true;
        return delegateAdapter;
    }

    public boolean isBuild() {
        return this.isBuild;
    }

    public void addAdapterInGroup(int groupPosition, SubAdapter subAdapter) {
        if (subAdapter == null) {
            return;
        }
        List<SubAdapter> child = null;
        if (groupMaps.containsKey(groupPosition)) {
            child = groupMaps.get(groupPosition);
            boolean flag = false;
            for (SubAdapter adapter : child) {
                if (TextUtils.equals(adapter.getSubKey(), subAdapter.getSubKey())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                child.add(subAdapter);
            }
        } else {
            child = new ArrayList<SubAdapter>();
            child.add(subAdapter);
        }
        groupMaps.put(groupPosition, child);
    }

    public VirtualLayoutManager getVirtualLayoutManager() {
        return this.virtualLayoutManager;
    }

    public DelegateAdapter getDelegateAdapter() {
        return delegateAdapter;
    }

    public SubAdapter getAdapterItem(String subKey) {
        SubAdapter subAdapter = null;
        for (Map.Entry<Integer, List<SubAdapter>> entry : groupMaps.entrySet()) {
            List<SubAdapter> list = entry.getValue();
            for (SubAdapter adapter : list) {
                if (TextUtils.equals(adapter.getSubKey(), subKey)) {
                    subAdapter = adapter;
                    break;
                }
            }
        }
        return subAdapter;
    }

    public boolean containKey(String subKey) {
        if (TextUtils.isEmpty(subKey)) {
            return false;
        }
        SubAdapter adapter = getAdapterItem(subKey);
        if (adapter == null) {
            return false;
        } else {
            return true;
        }
    }

    public void remove(String subKey) {
        if (TextUtils.isEmpty(subKey)) {
            return;
        }
        int removePosition = -1;
        int removeGroupPosition = -1;
        for (Map.Entry<Integer, List<SubAdapter>> entry : groupMaps.entrySet()) {
            List<SubAdapter> list = entry.getValue();
            for (SubAdapter subAdapter : list) {
                if (TextUtils.equals(subAdapter.getSubKey(), subKey)) {
                    removePosition = list.indexOf(subAdapter);
                    break;
                }
            }
            if (removePosition >= 0) {
                list.remove(removePosition);
                if (ObjectJudge.isNullOrEmpty(list)) {
                    removeGroupPosition = entry.getKey();
                }
                removePosition = -1;
                break;
            }
        }
        if (removeGroupPosition >= 0) {
            groupMaps.remove(removeGroupPosition);
            removeGroupPosition = -1;
        }
    }

    //将每一组的适配器按序列排列并返回
    public List<DelegateAdapter.Adapter> getSequenceAdapterList() {
        List<DelegateAdapter.Adapter> adapters = new ArrayList<DelegateAdapter.Adapter>();
        for (Map.Entry<Integer, List<SubAdapter>> entry : groupMaps.entrySet()) {
            List<SubAdapter> list = entry.getValue();
            Collections.sort(list, new Comparator<SubAdapter>() {
                @Override
                public int compare(SubAdapter o1, SubAdapter o2) {
                    return o1.getAdapterPosition().compareTo(o2.getAdapterPosition());
                }
            });
            for (SubAdapter subAdapter : list) {
                if (subAdapter == null) {
                    continue;
                }
                adapters.add(subAdapter);
            }
        }
        return adapters;
    }
}
