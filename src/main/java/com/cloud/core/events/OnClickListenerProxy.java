package com.cloud.core.events;

import android.view.View;

import com.cloud.core.logger.Logger;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/30
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
class OnClickListenerProxy implements View.OnClickListener {

    private View.OnClickListener object;

    public OnClickListenerProxy(View.OnClickListener object) {
        this.object = object;
    }

    @Override
    public void onClick(View v) {
        try {
            if (ClickEvent.isFastDoubleClick(v.getId())) {
                return;
            }
            onPreClickProxy(v);
            if (object != null) {
                object.onClick(v);
            }
            onAfterClickProxy(v);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    protected void onPreClickProxy(View v) {

    }

    protected void onAfterClickProxy(View v) {

    }
}
