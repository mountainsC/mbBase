package com.cloud.core.view.shuffling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cloud.core.beans.DeviceInfo;
import com.cloud.core.glides.GlideProcess;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.AppInfoUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.ScreenUtils;
import com.youth.banner.loader.ImageLoader;


public class FrescoImageLoader extends ImageLoader {

    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;

    public FrescoImageLoader() {

    }

    public FrescoImageLoader(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    @Override
    public void displayImage(final Context context, Object path, final ImageView imageView) {
        imageView.setScaleType(scaleType);
        String url = String.valueOf(path);
        if (url.contains(".gif")) {
            DeviceInfo deviceInfo = AppInfoUtils.getDeviceInfo(context);
            url=url.substring(0,url.indexOf("?"));
            final GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Device-type", "android").addHeader("cookie_id", deviceInfo.getImei()).build());
//            Glide.with(context).asGif().load(glideUrl).diskCacheStrategy(DiskCacheStrategy.ALL).override(GlobalUtils.getScreenWidth(context)
//                    ,PixelUtils.dip2px(context,120))
//                    .preload();
            Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }else {
            Uri uri = Uri.parse(String.valueOf(path));
            GlideProcess.load(context, uri, new DrawableImageViewTarget(imageView) {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    super.onResourceReady(resource, transition);
                    imageView.invalidate();
                }
            });
        }
    }
}
