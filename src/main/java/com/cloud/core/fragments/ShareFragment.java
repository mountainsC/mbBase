package com.cloud.core.fragments;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.cloud.core.R;
import com.cloud.core.beans.ShareContent;
import com.cloud.core.databinding.SharePanelViewBinding;
import com.cloud.core.dialog.BaseDialogPlugFragment;
import com.cloud.core.dialog.LoadingDialog;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.enums.ShareType;
import com.cloud.core.share.OnShareClickListener;
import com.cloud.core.share.OnShareSuccessCall;
import com.cloud.core.utils.BaseShareUtils;
import com.cloud.core.utils.ToastUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/13
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ShareFragment extends BaseDialogPlugFragment<ShareContent, DialogPlus> implements View.OnClickListener, BaseShareUtils.OnUmShareListener {

    private SharePanelViewBinding binding = null;
    private Activity activity = null;
    private OnShareSuccessCall onShareSuccessCall = null;
    private OnShareClickListener onShareClickListener = null;
    private LoadingDialog loading = new LoadingDialog();

    public void setOnShareSuccessCall(OnShareSuccessCall onShareSuccessCall) {
        this.onShareSuccessCall = onShareSuccessCall;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public void build(View contentView, Activity activity, ShareContent shareContent, DialogPlus dialogPlus) {
        super.build(contentView, activity, shareContent, dialogPlus);
        this.activity = activity;
        binding = DataBindingUtil.bind(contentView);
        binding.shareWx.setOnClickListener(this);
        binding.sharePyq.setOnClickListener(this);
        binding.shareQq.setOnClickListener(this);
        binding.shareQzone.setOnClickListener(this);
        //文件分享时隐藏[朋友圈、QQ、QQ空间]
        if (shareContent.getShareType() == ShareType.file) {
            binding.sharePyq.setVisibility(View.GONE);
            binding.shareQq.setVisibility(View.GONE);
            binding.shareQzone.setVisibility(View.GONE);
        }
    }

    private void dismiss() {
        DialogPlus dialogPlug = getDialogPlug();
        if (dialogPlug == null) {
            return;
        }
        dialogPlug.dismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.share_wx) {
            share(SHARE_MEDIA.WEIXIN);
        } else if (id == R.id.share_pyq) {
            share(SHARE_MEDIA.WEIXIN_CIRCLE);
        } else if (id == R.id.share_qq) {
            share(SHARE_MEDIA.QQ);
        } else if (id == R.id.share_qzone) {
            share(SHARE_MEDIA.QZONE);
        }
    }

    private void share(final SHARE_MEDIA platform) {
        final ShareContent data = getData();
        if (data == null) {
            dismiss();
            return;
        }
        loading.showDialog(getContext(), R.string.processing_just, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseShareUtils shareUtils = new BaseShareUtils();
                shareUtils.setOnUmShareListener(ShareFragment.this);
                //分享类型
                if (data.getShareType() == ShareType.link) {
                    shareUtils.shareWeb(activity, data, platform);
                    if (onShareClickListener != null) {
                        onShareClickListener.onShareClick(platform);
                    }
                } else if (data.getShareType() == ShareType.image) {
                    //如果file==null则为网络图片(url)
                    shareUtils.shareImg(activity, data.getUrl(), data.getFile(), platform);
                    if (onShareClickListener != null) {
                        onShareClickListener.onShareClick(platform);
                    }
                } else if (data.getShareType() == ShareType.file) {
                    //文件分享不作事件回调
                    shareUtils.shareFileToWx(activity,data);
                }
                loading.dismiss();
                dismiss();
            }
        }, 10);
    }

    @Override
    public void onResult(SHARE_MEDIA platform) {
        ToastUtils.showShort(getContext(), "分享成功，欢迎回来");
        if (onShareSuccessCall != null) {
            onShareSuccessCall.onShareSuccess(platform);
        }
    }
}
