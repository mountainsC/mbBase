package com.cloud.core.cviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.configs.BaseRConfig;
import com.cloud.core.configs.ConfigItem;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.ImgRuleType;
import com.cloud.core.enums.ResFolderType;
import com.cloud.core.events.Action1;
import com.cloud.core.glides.GlideProcess;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.ResUtils;
import com.cloud.core.utils.ValidUtils;
import com.cloud.core.utils.ViewUtils;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/27
 * @Description:N组合视图 布局方向由绑定视图时数据的排序方向决定
 * @Modifier:
 * @ModifyContent:
 */
public class NCombinedView extends LinearLayout {

    private OnNCombinedViewItemClickListener onNCombinedViewItemClickListener = null;

    public NCombinedView(Context context) {
        super(context);
    }

    public NCombinedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnNCombinedViewItemClickListener(OnNCombinedViewItemClickListener listener) {
        this.onNCombinedViewItemClickListener = listener;
    }

    /**
     * 绑定N组合视图
     *
     * @param entity 数据对象
     * @param <T>
     */
    public <T extends BaseNCombined> void bindCombinedView(T entity) {
        if (entity == null || entity.getTotalHeight() <= 0 || ObjectJudge.isNullOrEmpty(entity.getConfig())) {
            return;
        }
        if (entity.getTotalWidth() <= 0) {
            entity.setTotalWidth(GlobalUtils.getScreenWidth(getContext()));
        }
        List<NCombinedItem> combinedItems = entity.getConfig();
        for (NCombinedItem combinedItem : combinedItems) {
            int position = combinedItems.indexOf(combinedItem);
            buildView(this, entity.getTotalWidth(), entity.getTotalHeight(), 0, 0, combinedItem, (position + 1) == combinedItems.size());
        }
    }

    private LinearLayout createContainer(int orientation, int width, int height) {
        LayoutParams llparam = new LayoutParams(
                PixelUtils.dip2px(getContext(), width),
                PixelUtils.dip2px(getContext(), height)
        );
        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(llparam);
        container.setOrientation(orientation);
        return container;
    }

    private RelativeLayout createChildContainer(int width, int height) {
        LayoutParams rlparam = new LayoutParams(
                PixelUtils.dip2px(getContext(), width),
                PixelUtils.dip2px(getContext(), height)
        );
        RelativeLayout childrl = new RelativeLayout(getContext());
        childrl.setLayoutParams(rlparam);
        return childrl;
    }

    private ImageView createBgView(RelativeLayout container, int width, int height) {
        RelativeLayout.LayoutParams flparam = new RelativeLayout.LayoutParams(
                PixelUtils.dip2px(getContext(), width),
                PixelUtils.dip2px(getContext(), height)
        );
        FrameLayout layout = new FrameLayout(getContext());
        layout.setLayoutParams(flparam);
        layout.setBackgroundResource(R.color.transparent);
        Drawable drawable = ResUtils.getDrawable(getContext(), R.drawable.def_mask);
        if (drawable != null) {
            layout.setForeground(drawable);
        }
        RelativeLayout.LayoutParams ivparam = new RelativeLayout.LayoutParams(
                PixelUtils.dip2px(getContext(), width),
                PixelUtils.dip2px(getContext(), height)
        );
        ImageView bgiv = new ImageView(getContext());
        bgiv.setLayoutParams(ivparam);

        layout.addView(bgiv);
        container.addView(layout);
        return bgiv;
    }

    private void buildView(LinearLayout container, int width, int height, int yuwidth, int yuheight, NCombinedItem combinedItem, boolean isLast) {
        //判断当前是否为孩子节点
        if (ObjectJudge.isNullOrEmpty(combinedItem.getChildren())) {
            if (isLast) {
                if (yuwidth >= combinedItem.getWidth()) {
                    combinedItem.setWidth(yuwidth);
                }
                if (yuheight >= combinedItem.getHeight()) {
                    combinedItem.setHeight(yuheight);
                }
            }
            RelativeLayout childContainer = createChildContainer(combinedItem.getWidth(), combinedItem.getHeight());
            NCBlockItem block = combinedItem.getBlock();
            if (TextUtils.isEmpty(block.getImgUrl())) {
                if (ValidUtils.valid("^#[a-zA-Z0-9]{6}$", block.getBg())) {
                    int color = Color.parseColor(block.getBg());
                    childContainer.setBackgroundColor(color);
                }
                ViewUtils.setViewListener(childContainer, new Action1<Object>() {
                    @Override
                    public void call(Object data) {
                        if (onNCombinedViewItemClickListener != null) {
                            onNCombinedViewItemClickListener.onNCombinedViewItemClick(data);
                        }
                    }
                }, combinedItem.getData());
                container.addView(childContainer);
            } else {
                RxCoreConfigItems configItems = BaseRConfig.getInstance().getConfigItems(getContext());
                ConfigItem defaultBackgroundImage = configItems.getDefaultBackgroundImage();
                ResFolderType folderType = ResFolderType.getResFolderType(defaultBackgroundImage.getType());
                int defImg = ResUtils.getResource(getContext(), defaultBackgroundImage.getName(), folderType);
                ImageView bgView = createBgView(childContainer, combinedItem.getWidth(), combinedItem.getHeight());
                GlideProcess.load(
                        getContext(),
                        ImgRuleType.GeometricForWidth,
                        block.getImgUrl(),
                        defImg,
                        combinedItem.getWidth(),
                        combinedItem.getHeight(),
                        0,
                        bgView);
                ViewUtils.setViewListener(childContainer, new Action1<Object>() {
                    @Override
                    public void call(Object data) {
                        if (onNCombinedViewItemClickListener != null) {
                            onNCombinedViewItemClickListener.onNCombinedViewItemClick(data);
                        }
                    }
                }, combinedItem.getData());
                container.addView(childContainer);
            }
        } else {
            NCombinedItem first = combinedItem.getChildren().get(0);
            if (TextUtils.equals(first.getOrientation(), NCOrientation.h.name())) {
                buildChild(container, LinearLayout.HORIZONTAL, width, height, yuwidth, yuheight, combinedItem);
            } else if (TextUtils.equals(first.getOrientation(), NCOrientation.v.name())) {
                buildChild(container, LinearLayout.VERTICAL, width, height, yuwidth, yuheight, combinedItem);
            }
        }
    }

    private void buildChild(LinearLayout container, int orientation, int width, int height, int yuwidth, int yuheight, NCombinedItem combinedItem) {
        LinearLayout hll = createContainer(orientation, combinedItem.getWidth(), combinedItem.getHeight());
        container.addView(hll);
        List<NCombinedItem> children = combinedItem.getChildren();
        int[] countSize = countSize(children);
        yuwidth = width - countSize[0];
        yuheight = height - countSize[1];
        for (NCombinedItem child : children) {
            int position = children.indexOf(child);
            if (TextUtils.equals(child.getOrientation(), NCOrientation.h.name())) {
                buildView(hll, combinedItem.getWidth(), combinedItem.getHeight(), combinedItem.getWidth() - countSize[0], countSize[1], child, (position + 1) == children.size());
            } else if (TextUtils.equals(child.getOrientation(), NCOrientation.v.name())) {
                buildView(hll, combinedItem.getWidth(), combinedItem.getHeight(), countSize[0], combinedItem.getHeight() - countSize[1], child, (position + 1) == children.size());
            }
        }
    }

    private int[] countSize(List<NCombinedItem> children) {
        int bwidth = 0;
        int bheight = 0;
        for (int i = 0; i < children.size(); i++) {
            if ((i + 1) < children.size()) {
                NCombinedItem item = children.get(i);
                if (TextUtils.equals(item.getOrientation(), NCOrientation.h.name())) {
                    bwidth += item.getWidth();
                    bheight = item.getHeight();
                } else if (TextUtils.equals(item.getOrientation(), NCOrientation.v.name())) {
                    bwidth = item.getWidth();
                    bheight += item.getHeight();
                }
            }
        }
        int[] args = {bwidth, bheight};
        return args;
    }
}
