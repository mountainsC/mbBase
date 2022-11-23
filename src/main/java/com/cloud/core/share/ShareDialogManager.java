package com.cloud.core.share;

import android.app.Activity;
import android.view.View;

import com.cloud.core.R;
import com.cloud.core.beans.ShareContent;
import com.cloud.core.dialog.DialogManager;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.events.Action3;
import com.cloud.core.fragments.ShareFragment;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/13
 * Description:分享工具类
 * Modifier:
 * ModifyContent:
 */
public class ShareDialogManager {

    private ShareContent shareContent = null;
    private DialogManager.DialogManagerBuilder<ShareContent> builder = null;
    private Activity activity = null;
    private OnShareSuccessCall onShareSuccessCall = null;
    private OnShareClickListener onShareClickListener = null;

    private ShareDialogManager() {
        //init
    }

    /**
     * 设置分享信息
     *
     * @param shareContent 分享信息
     * @return
     */
    public ShareDialogManager setShareContent(ShareContent shareContent) {
        this.shareContent = shareContent;
        return this;
    }

    /**
     * 设置分享成功回调
     *
     * @param onShareSuccessCall 分享成功回调
     * @return
     */
    public ShareDialogManager setOnShareSuccessCall(OnShareSuccessCall onShareSuccessCall) {
        this.onShareSuccessCall = onShareSuccessCall;
        return this;
    }

    /**
     * 设置分享事件
     *
     * @param onShareClickListener 分享事件
     * @return
     */
    public ShareDialogManager setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
        return this;
    }

    //获取分享实例
    public static ShareDialogManager getInstance() {
        return new ShareDialogManager();
    }

    public ShareDialogManager build(Activity activity) {
        this.activity = activity;
        builder = DialogManager.getInstance().builder(activity, R.layout.share_panel_view);
        builder.setExtra(shareContent);
        return this;
    }

    public void show() {
        if (builder == null) {
            return;
        }
        builder.show(new Action3<View, DialogPlus, ShareContent>() {
            @Override
            public void call(View view, DialogPlus dialogPlus, ShareContent shareContent) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.setOnShareSuccessCall(onShareSuccessCall);
                shareFragment.setOnShareClickListener(onShareClickListener);
                shareFragment.build(view, activity, shareContent, dialogPlus);
            }
        });
    }
}
