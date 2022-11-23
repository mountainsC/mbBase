package com.cloud.core.view.sview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.cloud.core.events.Action1;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Action1<SpaceItem> spaceAction = null;

    public SpaceItemDecoration(Action1<SpaceItem> spaceAction) {
        this.spaceAction = spaceAction;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (spaceAction != null) {
            SpaceItem spaceItem = new SpaceItem();
            spaceItem.setOutRect(outRect);
            spaceItem.setView(view);
            spaceItem.setParent(parent);
            spaceItem.setState(state);
            spaceAction.call(spaceItem);
        }
    }
}
