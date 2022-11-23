package com.cloud.core.view.shuffling;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.cloud.core.ObjectJudge;
import com.cloud.core.ObjectManager;
import com.cloud.core.beans.BaseImageItem;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.JsonUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/25
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ShufflingBannerView extends Banner {

    private OnShufflingBanner onShufflingBanner = null;
    private static String imageUrlFormat = "";
    private static OnShufflingBannerConfig onShufflingBannerConfig = null;
    private int sbwidth = 0;
    private int sbheight = 0;
    private List<String> slbtitles = null;
    private List<String> images = null;

    public ShufflingBannerView(Context context) {
        super(context);
    }

    public ShufflingBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnShufflingBanner(OnShufflingBanner listener) {
        this.onShufflingBanner = listener;
    }

    public static void setOnShufflingBannerConfig(OnShufflingBannerConfig config) {
        onShufflingBannerConfig = config;
    }

    /**
     * 初始化轮播图
     *
     * @param bannerWidth banner宽度
     * @param proportion  图片宽高比
     */
    public void instance(int bannerWidth, double proportion) {
        try {
            int mheight = (int) (bannerWidth / proportion);
            getLayoutParams().height = mheight;
            sbwidth = bannerWidth;
            sbheight = mheight;
            super.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (onShufflingBanner != null && !ObjectJudge.isNullOrEmpty(images) && images.size() > position) {
                        //确保position在有效的范围内
                        onShufflingBanner.onShufflingClick(images, position);
                    }
                }
            });
            this.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 初始化轮播图
     *
     * @param proportion 图片宽高比
     */
    public void instance(double proportion) {
        DisplayMetrics dm = ObjectManager.getDisplayMetrics(getContext());
        instance(dm.widthPixels, proportion);
    }

    public int getBannerWidth() {
        return this.sbwidth;
    }

    public int getBannerHeight() {
        return this.sbheight;
    }

    public void setSlbtitles(List<String> titles) {
        this.slbtitles = titles;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void bind(List<String> imglst, ImageLoaderInterface loaderInterface) {
        try {
            if (ObjectJudge.isNullOrEmpty(imglst)) {
                return;
            }
            this.images = imglst;
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            this.setImageLoader(loaderInterface);
            this.setImages(imglst);
            this.setBannerAnimation(Transformer.Default);
            this.isAutoPlay(true);
            this.setDelayTime(1500);
            if (onShufflingBannerConfig != null) {
                if (onShufflingBannerConfig.getIndicatorGravity() != 0) {
                    this.setIndicatorGravity(onShufflingBannerConfig.getIndicatorGravity());
                } else {
                    this.setIndicatorGravity(BannerConfig.CENTER);
                }
            } else {
                this.setIndicatorGravity(BannerConfig.CENTER);
            }
            if (ObjectJudge.isNullOrEmpty(slbtitles)) {
                List<String> titles = new ArrayList<>();
                for (int i = 0; i < imglst.size(); i++) {
                    titles.add("");
                }
                this.setBannerTitles(titles);
            } else {
                this.setBannerTitles(slbtitles);
            }
            this.start();
        } catch (Exception e) {
            Logger.L.error("bind shuffling error:", e);
        }
    }

    public void bind(List<String> imglst, ImageView.ScaleType scaleType) {
        bind(imglst, new FrescoImageLoader(scaleType));
    }

    public void bind(List<String> imglst) {
        bind(imglst, ImageView.ScaleType.FIT_XY);
    }

    /**
     * 绑定轮播图
     *
     * @param imgListJson    图片数组json {@link BaseImageItem}
     * @param shufflingWidth 轮播图宽度
     */
    public void bind(String imgListJson, int shufflingWidth) {
        try {
            if (onShufflingBannerConfig == null) {
                return;
            }
            List<BaseImageItem> baseImageItems = JsonUtils.parseArray(imgListJson, BaseImageItem.class);
            List<String> imglst = new ArrayList<String>();
            for (BaseImageItem baseImageItem : baseImageItems) {
                String url = onShufflingBannerConfig.getShufflingBannerImageFormat();
                url = String.format(url, baseImageItem.getUrl());
                url = MessageFormat.format(url, String.valueOf(shufflingWidth));
                imglst.add(url);
            }
            bind(imglst);
        } catch (Exception e) {
            Logger.L.error("bind shuffling error:", e);
        }
    }

    /**
     * 禁用滚动
     */
    public void disableRolling() {
        isAutoPlay(false);
        stopAutoPlay();
    }
}
