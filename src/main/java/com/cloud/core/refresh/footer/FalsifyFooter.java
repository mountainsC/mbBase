package com.cloud.core.refresh.footer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cloud.core.refresh.api.RefreshFooter;
import com.cloud.core.refresh.api.RefreshKernel;
import com.cloud.core.refresh.api.RefreshLayout;
import com.cloud.core.refresh.constant.RefreshState;
import com.cloud.core.refresh.header.FalsifyHeader;
import com.cloud.core.utils.PixelUtils;

/**
 * 虚假的 Footer
 * 用于 正真的 Footer 在 RefreshLayout 外部时，
 * Created by SCWANG on 2017/6/14.
 */

public class FalsifyFooter extends FalsifyHeader implements RefreshFooter {

    //<editor-fold desc="FalsifyHeader">
    public FalsifyFooter(Context context) {
        super(context);
    }

    public FalsifyFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FalsifyFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public FalsifyFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {//这段代码在运行时不会执行，只会在Studio编辑预览时运行，不用在意性能问题
            int d = PixelUtils.dip2px(getContext(), 5);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0x44ffffff);
            paint.setStrokeWidth(PixelUtils.dip2px(getContext(), 1));
            paint.setPathEffect(new DashPathEffect(new float[]{d, d, d, d}, 1));
            canvas.drawRect(d, d, getWidth() - d, getBottom() - d, paint);

            TextView textView = new TextView(getContext());
            textView.setText(getClass().getSimpleName() + " 虚假区域\n运行时代表上拉Footer的高度【" + PixelUtils.px2dip(getContext(), getHeight()) + "dp】\n而不会显示任何东西");
            textView.setTextColor(0x44ffffff);
            textView.setGravity(Gravity.CENTER);
            textView.measure(makeMeasureSpec(getWidth(), EXACTLY), makeMeasureSpec(getHeight(), EXACTLY));
            textView.layout(0, 0, getWidth(), getHeight());
            textView.draw(canvas);
        }
    }


    //</editor-fold>

    //<editor-fold desc="RefreshFooter">


    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        super.onInitialized(kernel, height, extendHeight);
        kernel.getRefreshLayout().setEnableAutoLoadmore(false);
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {

    }

    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullToUpLoad:
                if (mPureScrollMode != null
                        && mPureScrollMode != refreshLayout.isEnablePureScrollMode()) {
                    refreshLayout.setEnablePureScrollMode(mPureScrollMode);
                }
                break;
            case ReleaseToLoad:
                mPureScrollMode = refreshLayout.isEnablePureScrollMode();
                if (!mPureScrollMode) {
                    refreshLayout.setEnablePureScrollMode(true);
                }
                break;
        }
    }

    //</editor-fold>

}
