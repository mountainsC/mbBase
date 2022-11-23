package com.cloud.core.glides;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cloud.core.configs.BaseCConfig;
import com.cloud.core.configs.BaseRConfig;
import com.cloud.core.configs.ConfigItem;
import com.cloud.core.configs.OnConfigItemUrlListener;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.ImgRuleType;
import com.cloud.core.enums.ResFolderType;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.events.Action1;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.PathsUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.ResUtils;
import com.cloud.core.utils.RxCachePool;
import com.cloud.core.utils.ValidUtils;

import java.io.File;
import java.security.MessageDigest;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class GlideProcess {

    private static int defImgBg = 0;

    static class GlideParams {
        public Context context = null;
        public ImgRuleType ruleType = null;
        public String url = null;
        public int preLoadWidth = 0;
        public int preLoadHeight = 0;
        public int imgWidth = 0;
        public int imgHeight = 0;
        public int imgCorners = 0;
        public int defImg = 0;
        public int crossFade = 0;
        public File file = null;
        public Uri uri = null;
        public boolean isRadius = false;
    }

    static class MessageTransform {
        public GlideParams glideParams = null;
        public ImageView imageView = null;
        public Target target = null;
    }

    static class GlideCircleTransform extends BitmapTransformation {

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.reset();
        }
    }

    private static RequestBuilder<Drawable> loadConfig(GlideParams glideParams, Action1<RequestOptions> optionAction) {
        String allurl = "";
        RequestBuilder<Drawable> request = null;
        RequestManager manager = null;
        RequestOptions options = new RequestOptions()
                .autoClone()
                .dontAnimate()
                .placeholder(glideParams.defImg)
                .error(glideParams.defImg)
                .fitCenter()
                .skipMemoryCache(true)
                .timeout(1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        if (optionAction != null) {
            optionAction.call(options);
        }
        manager = Glide.with(glideParams.context);
        manager = manager.applyDefaultRequestOptions(options);
        if (glideParams.file != null) {
            request = manager.load(glideParams.file);
        } else if (glideParams.uri != null) {
            request = manager.load(glideParams.uri);
        } else {
            if (!ValidUtils.valid(RuleParams.Url.getValue(), glideParams.url)) {
                RxCoreConfigItems configItems = BaseCConfig.getInstance().getConfigItems(glideParams.context);
                ConfigItem imgBasicUrl = configItems.getBasicUrls().getImg();
                Object urlListener = RxCachePool.getInstance().getObject(imgBasicUrl.getUrlType());
                if (urlListener != null && urlListener instanceof OnConfigItemUrlListener) {
                    OnConfigItemUrlListener listener = (OnConfigItemUrlListener) urlListener;
                    String basicUrl = listener.getUrl(imgBasicUrl.getUrlType());
                    glideParams.url = PathsUtils.combine(basicUrl, glideParams.url);
                }
            }
            allurl = FormatDataModel.getUrl(glideParams.context,
                    glideParams.url,
                    glideParams.ruleType,
                    glideParams.imgWidth,
                    glideParams.imgHeight,
                    glideParams.imgCorners);
            request = manager.load(allurl);
        }
        request.thumbnail(0.1f);
        if (glideParams.preLoadWidth > 0 && glideParams.preLoadHeight > 0) {
            request.preload(glideParams.preLoadWidth, glideParams.preLoadHeight);
        }
        return request;
    }

    private static int getDefBackgroundResId(Context context) {
        if (defImgBg == 0) {
            RxCoreConfigItems configItems = BaseRConfig.getInstance().getConfigItems(context);
            ConfigItem defaultBackgroundImage = configItems.getDefaultBackgroundImage();
            ResFolderType folderType = ResFolderType.getResFolderType(defaultBackgroundImage.getType());
            return ResUtils.getResource(context, defaultBackgroundImage.getName(), folderType);
        } else {
            return defImgBg;
        }
    }

    public static void load(Context context, ImgRuleType ruleType, String url, int defImg, int imgWidth, int imgHeight, int imgCorners, ImageView imageView) {
        GlideParams glideParams = new GlideParams();
        glideParams.context = context;
        glideParams.url = url;
        glideParams.defImg = defImg;
        glideParams.ruleType = ruleType;
        glideParams.imgWidth = imgWidth;
        glideParams.imgHeight = imgHeight;
        glideParams.imgCorners = imgCorners;
        MessageTransform transform = new MessageTransform();
        transform.glideParams = glideParams;
        transform.imageView = imageView;
        glidehandler.obtainMessage(100023, transform).sendToTarget();
    }

    public static void load(Context context, ImgRuleType ruleType, String url, int imgWidth, int imgHeight, int imgCorners, ImageView imageView) {
        load(context, ruleType, url, getDefBackgroundResId(context), imgWidth, imgHeight, imgCorners, imageView);
    }

    public static void load(Context context, File file, int defImg, ImageView imageView) {
        GlideParams glideParams = new GlideParams();
        glideParams.context = context;
        glideParams.file = file;
        glideParams.defImg = defImg;
        glideParams.preLoadWidth = PixelUtils.dip2px(context, 46);
        glideParams.preLoadHeight = PixelUtils.dip2px(context, 46);
        glideParams.crossFade = 200;
        MessageTransform transform = new MessageTransform();
        transform.glideParams = glideParams;
        transform.imageView = imageView;
        glidehandler.obtainMessage(100023, transform).sendToTarget();
    }

    public static void load(Context context, File file, ImageView imageView) {
        load(context, file, getDefBackgroundResId(context), imageView);
    }

    public static void load(Context context, Uri uri, Target target) {
        GlideParams glideParams = new GlideParams();
        glideParams.context = context;
        glideParams.uri = uri;
        glideParams.defImg = getDefBackgroundResId(context);
        glideParams.crossFade = 200;
        MessageTransform transform = new MessageTransform();
        transform.glideParams = glideParams;
        transform.target = target;
        glidehandler.obtainMessage(100023, transform).sendToTarget();
    }

    public static void loadRadius(final Context context, ImgRuleType ruleType, String url, int radius, int defImage, ImageView imageView) {
        GlideParams glideParams = new GlideParams();
        glideParams.context = context;
        glideParams.url = url;
        glideParams.imgWidth = 2 * radius;
        glideParams.imgHeight = 2 * radius;
        glideParams.defImg = defImage;
        glideParams.crossFade = 200;
        glideParams.isRadius = true;
        MessageTransform transform = new MessageTransform();
        transform.glideParams = glideParams;
        transform.imageView = imageView;
        glidehandler.obtainMessage(100023, transform).sendToTarget();
    }

    private static Handler glidehandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what != 100023 || msg.obj == null || !(msg.obj instanceof MessageTransform)) {
                    return;
                }
                final MessageTransform transform = (MessageTransform) msg.obj;
                if (transform == null) {
                    return;
                }
                if (transform.glideParams == null) {
                    return;
                }
                RequestBuilder<Drawable> builder = null;
                if (transform.glideParams.isRadius) {
                    builder = loadConfig(transform.glideParams, new Action1<RequestOptions>() {
                        @Override
                        public void call(RequestOptions options) {
                            options.transform(new GlideCircleTransform());
                        }
                    });
                } else {
                    builder = loadConfig(transform.glideParams, null);
                }
                if (transform.imageView != null) {
                    builder.into(transform.imageView);
                } else if (transform.target != null) {
                    builder.into(transform.target);
                }
            } catch (Exception e) {
                Logger.L.error(e);
            }
        }
    };
}
