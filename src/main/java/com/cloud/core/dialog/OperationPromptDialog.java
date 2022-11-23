package com.cloud.core.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.dialog.events.OnOperationPromptKeyListener;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.events.Action3;
import com.cloud.core.icons.IconFontView;
import com.cloud.core.okrx.OkRxManager;
import com.cloud.core.okrx.properties.ReqQueueItem;
import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/6
 * @Description:操作提示dialog
 * @Modifier:
 * @ModifyContent:
 */
public class OperationPromptDialog {

    private int imageResId = 0;
    private String imageUrl = "";
    private CharSequence subject = "";
    private CharSequence describtion = "";
    private String targetViewText = "";
    private OnOperationPromptKeyListener onOperationPromptKeyListener = null;

    /**
     * 设置图片资源
     *
     * @param resId 图片资源id
     */
    public void setImageResouce(int resId) {
        this.imageResId = resId;
    }

    /**
     * 设置图片资源
     *
     * @param imageUrl 图片url
     */
    public void setImageResouce(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 设置主题
     *
     * @param subject 主题内容
     */
    public void setSubject(CharSequence subject) {
        this.subject = subject;
    }

    /**
     * 设置描述性信息
     *
     * @param describtion 描述性信息
     */
    public void setDescribtion(CharSequence describtion) {
        this.describtion = describtion;
    }

    /**
     * 设置目标视图文本
     *
     * @param targetViewText 目标视图文本
     */
    public void setTargetViewText(String targetViewText) {
        this.targetViewText = targetViewText;
    }

    public void setOnOperationPromptKeyListener(OnOperationPromptKeyListener listener) {
        this.onOperationPromptKeyListener = listener;
    }

    public void show(final Context context) {
        DialogManager.DialogManagerBuilder<Object> builder = DialogManager.getInstance().builder(context, R.layout.dialog_operation_prompt_view);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        builder.show(new Action3<View, DialogPlus, Object>() {
            @Override
            public void call(View contentView, DialogPlus dialogPlus, Object object) {
                OperationPromptDialogFragment dialogFragment = new OperationPromptDialogFragment();
                dialogFragment.build(contentView, context, null, dialogPlus);
            }
        });
    }

    private class OperationPromptDialogFragment extends BaseDialogPlugFragment<Object, DialogPlus> {
        @Override
        public void build(View contentView, Context context, Object object, DialogPlus dialogPlus) {
            super.build(contentView, context, object, dialogPlus);
            //设置图片
            ImageView imageView = (ImageView) contentView.findViewById(R.id.dialog_oper_prompt_iv);
            if (imageResId != 0) {
                imageView.setImageResource(imageResId);
            } else if (!TextUtils.isEmpty(imageUrl)) {
                loadImageByUrl(imageUrl);
            }
            //设置主题信息
            if (!TextUtils.isEmpty(subject)) {
                TextView subjectTv = (TextView) contentView.findViewById(R.id.dialog_oper_prompt_topic_tv);
                subjectTv.setText(subject);
            }
            //设置描述性信息
            TextView describtionTv = (TextView) contentView.findViewById(R.id.dialog_oper_prompt_des_tv);
            if (TextUtils.isEmpty(describtion)) {
                describtionTv.setVisibility(View.INVISIBLE);
            } else {
                describtionTv.setVisibility(View.VISIBLE);
                describtionTv.setText(describtion);
            }
            //设置目标事件
            IconFontView fontView = (IconFontView) contentView.findViewById(R.id.dialog_oper_prompt_target_ifv);
            if (TextUtils.isEmpty(targetViewText)) {
                fontView.setVisibility(View.GONE);
            } else {
                fontView.setVisibility(View.VISIBLE);
                fontView.setText(targetViewText);
            }
            fontView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOperationPromptKeyListener == null) {
                        return;
                    }
                    onOperationPromptKeyListener.onTargetViewClick(v);
                }
            });
        }

        private void loadImageByUrl(String url) {
            OkRxManager.getInstance().getBitmap(getContext(),
                    url, null, null,
                    new Action3<Bitmap, String, HashMap<String, ReqQueueItem>>() {
                        @Override
                        public void call(Bitmap bitmap, String s, HashMap<String, ReqQueueItem> stringReqQueueItemHashMap) {
                            View contentView = getContentView();
                            ImageView imageView = (ImageView) contentView.findViewById(R.id.dialog_oper_prompt_iv);
                            imageView.setImageBitmap(bitmap);
                        }
                    },
                    null,
                    "",
                    null);
        }
    }
}
