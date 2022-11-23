package com.cloud.core.view.sview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cloud.core.ObjectJudge;
import com.cloud.core.events.Action1;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/7
 * @Description:横向滚动视图
 * @Modifier:
 * @ModifyContent:
 */
public class HSView extends RecyclerView {

    private HSViewAdapter hsViewAdapter = null;

    public HSView(Context context) {
        super(context);
    }

    public HSView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //step 1 初始化
    public void instance(int spanCount,
                         OnItemClickListener onItemClickListener,
                         OnViewHolderListener onViewHolderListener,
                         Action1<SpaceItem> spaceAction) {
        if (hsViewAdapter == null) {
            hsViewAdapter = new HSViewAdapter();
            this.setAdapter(hsViewAdapter);
            this.setItemAnimator(new DefaultItemAnimator());
            this.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                    StaggeredGridLayoutManager.HORIZONTAL));
            RecyclerView.ItemDecoration spacingInPixel = new SpaceItemDecoration(spaceAction);
            this.addItemDecoration(spacingInPixel);
            hsViewAdapter.setOnItemClickListener(onItemClickListener);
            hsViewAdapter.setOnViewHolderListener(onViewHolderListener);
        }
    }

    //step 4 刷新数据
    public <T> void notifyDataSetChanged(List<T> dataList, boolean isAppend) {
        if (ObjectJudge.isNullOrEmpty(dataList)) {
            return;
        }
        if (!isAppend) {
            hsViewAdapter.getDataList().clear();
        }
        hsViewAdapter.getDataList().addAll(dataList);
        hsViewAdapter.notifyDataSetChanged();
    }
}
