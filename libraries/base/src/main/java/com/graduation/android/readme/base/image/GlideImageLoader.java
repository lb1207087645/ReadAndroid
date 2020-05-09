package com.graduation.android.readme.base.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.graduation.android.readme.base.image.listener.BitmapLoaderListener;
import com.graduation.android.readme.base.image.listener.ImageLoaderListener;
import com.graduation.android.readme.base.utils.DensityUtils;
import com.graduation.android.readme.base.utils.L;

import java.util.concurrent.Executors;

/**
 * desc: 图片加载器的glide实现
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
public class GlideImageLoader implements IImageLoader {

    @Override
    public void loadGif(ImageView view, String gifUrl) {
        loadGif(view, gifUrl, null);
    }

    @Override
    public void loadGif(ImageView view, String gifUrl, ImageLoadConfig config) {
        loadGif(view, gifUrl, config, null);
    }

    @Override
    public void loadGif(ImageView view, String gifUrl, ImageLoadConfig config, ImageLoaderListener listener) {
        loadImage(view, gifUrl, ImageLoadConfig.parseBuilder(config).setAsGif(true).build(), listener);
    }

    @Override
    public void loadImage(ImageView view, String imgUrl) {
        loadImage(view, imgUrl, null);
    }

    @Override
    public void loadImage(ImageView view, String imgUrl, ImageLoadConfig config) {
        loadImage(view, imgUrl, config, null);
    }

    @Override
    public void loadImage(ImageView view, int resourceId, ImageLoadConfig config) {
        loadImage(view, resourceId, config, null);
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadImage(ImageView view, String imgUrl, ImageLoadConfig config, ImageLoaderListener listener) {
        if (null == imgUrl) {
            L.e("Glide", "imgUrl is null");
            imgUrl = "";
        }
        if (null == config) {
            config = new ImageLoadConfig.Builder().build();
        }

        try {
            RequestOptions options = new RequestOptions();
            RequestBuilder builder = null;
            Context context = view.getContext();
            if (config.isAsGif()) {
                // gif类型
                builder = Glide.with(context).asGif().load(imgUrl);
            } else if (config.isAsBitmap()) {
                // bitmap 类型
                builder = Glide.with(context).asBitmap().load(imgUrl);
                options.dontAnimate();
            } else if (config.isAsDrawable()) {
                // bitmap 类型
                builder = Glide.with(context).asDrawable().load(imgUrl);
                options.dontAnimate();
            } else if (config.isCrossFade()) {
                // 渐入渐出动画
                builder = Glide.with(context).load(imgUrl).transition(DrawableTransitionOptions.withCrossFade());
            }
            if (!config.isRoundedCorners() && config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                options.centerCrop();
            } else {
                options.fitCenter();
            }
//            // 缓存设置
//            DiskCacheStrategy strategy = DiskCacheStrategy.ALL;
//            switch (config.getDiskCache()) {
//                case NONE:
//                    strategy = DiskCacheStrategy.NONE;
//                    break;
//                case SOURCE:
//                    strategy = DiskCacheStrategy.SOURCE;
//                    break;
//                case RESULT:
//                    strategy = DiskCacheStrategy.RESULT;
//                    break;
//                case ALL:
//                    strategy = DiskCacheStrategy.ALL;
//                    break;
//                default:
//                    break;
//            }
            Priority priority = Priority.HIGH;
            switch (config.getPriority()) {
                case IMMEDIATE:
                    priority = Priority.IMMEDIATE;
                    break;
                case HIGH:
                    priority = Priority.HIGH;
                    break;
                case NORMAL:
                    priority = Priority.NORMAL;
                    break;
                case LOW:
                    priority = Priority.LOW;
                    break;
                default:
                    break;
            }
            options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(config.isSkipMemoryCache())
                    .priority(priority);
//            if (null != config.getTag()) {
//                builder.signature(new StringSignature(config.getTag()));
//            } else {
//                builder.signature(new StringSignature(imgUrl));
//            }
            if (config.getThumbnail() > 0.0f) {
                builder.thumbnail(config.getThumbnail());
            }
            if (null != config.getErrorResId()) {
                options.error(config.getErrorResId());
            }
            if (null != config.getPlaceHolderResId()) {
                options.placeholder(config.getPlaceHolderResId());
            }
//            if (config.isCropCircle()) {
//                options.transform(new CropCircleTransformation(context));
//            }
//            if (config.isBlur()) {
//                options.transform(new BlurTransformation(context));
//            }
            if (config.isRoundedCorners()) {
                int roundingRadius = 0 != config.getRoundingRadius() ? config.getRoundingRadius() : 5;
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    RoundedDirection roundedDirection = config.getRoundedDirection();
                    if (null != roundedDirection) {
                        GlideRoundedTransform transform = new GlideRoundedTransform(context, DensityUtils.dp2px(roundingRadius));
                        transform.setNeedCorner(roundedDirection.leftTop, roundedDirection.rightTop, roundedDirection.leftBottom, roundedDirection.rightBottom);
                        options.transforms(new CenterCrop(), transform);
                    } else {
                        options.transforms(new CenterCrop(), new RoundedCorners(DensityUtils.dp2px(roundingRadius)));
                    }
                } else {
                    RoundedDirection roundedDirection = config.getRoundedDirection();
                    if (null != roundedDirection) {
                        GlideRoundedTransform transform = new GlideRoundedTransform(context, DensityUtils.dp2px(roundingRadius));
                        transform.setNeedCorner(roundedDirection.leftTop, roundedDirection.rightTop, roundedDirection.leftBottom, roundedDirection.rightBottom);
                        options.transforms(transform);
                    } else {
                        options.transform(new RoundedCorners(DensityUtils.dp2px(roundingRadius)));
                    }
                }
            }
            if (null != config.getSize()) {
                options.override(config.getSize().getWidth(), config.getSize().getHeight());
            }
            if (null != listener) {
                setListener(builder, listener);
            }
            if (null != config.getThumbnailUrl()) {
                RequestBuilder thumbnailRequest = Glide.with(context).asBitmap().load(config.getThumbnailUrl());
                builder.thumbnail(thumbnailRequest).apply(options).into(view);
            } else {
                builder.apply(options).into(view);
            }
        } catch (Exception e) {
            view.setImageResource(config.getErrorResId());
        }
    }

    @Override
    public void loadImage(ImageView view, int resourceId, ImageLoadConfig config, ImageLoaderListener listener) {
        if (0 == resourceId) {
            L.e("Glide", "imgUrl is null");
        }
        if (null == config) {
            config = new ImageLoadConfig.Builder().build();
        }

        try {
            RequestOptions options = new RequestOptions();
            RequestBuilder builder = null;
            Context context = view.getContext();
            if (config.isAsGif()) {
                // gif类型
                builder = Glide.with(context).asGif().load(resourceId);
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
            } else if (config.isAsBitmap()) {
                // bitmap 类型
                builder = Glide.with(context).asBitmap().load(resourceId);
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
                options.dontAnimate();
            } else if (config.isAsDrawable()) {
                // bitmap 类型
                builder = Glide.with(context).asDrawable().load(resourceId);
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
                options.dontAnimate();
            } else if (config.isCrossFade()) {
                // 渐入渐出动画
                builder = Glide.with(context).load(resourceId).transition(DrawableTransitionOptions.withCrossFade());
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
            }
//            // 缓存设置
//            DiskCacheStrategy strategy = DiskCacheStrategy.ALL;
//            switch (config.getDiskCache()) {
//                case NONE:
//                    strategy = DiskCacheStrategy.NONE;
//                    break;
//                case SOURCE:
//                    strategy = DiskCacheStrategy.SOURCE;
//                    break;
//                case RESULT:
//                    strategy = DiskCacheStrategy.RESULT;
//                    break;
//                case ALL:
//                    strategy = DiskCacheStrategy.ALL;
//                    break;
//                default:
//                    break;
//            }
            Priority priority = Priority.HIGH;
            switch (config.getPriority()) {
                case IMMEDIATE:
                    priority = Priority.IMMEDIATE;
                    break;
                case HIGH:
                    priority = Priority.HIGH;
                    break;
                case NORMAL:
                    priority = Priority.NORMAL;
                    break;
                case LOW:
                    priority = Priority.LOW;
                    break;
                default:
                    break;
            }
            options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(config.isSkipMemoryCache())
                    .priority(priority);
//            if (null != config.getTag()) {
//                builder.signature(new StringSignature(config.getTag()));
//            } else {
//                builder.signature(new StringSignature(imgUrl));
//            }
            if (config.getThumbnail() > 0.0f) {
                builder.thumbnail(config.getThumbnail());
            }
            if (null != config.getErrorResId()) {
                options.error(config.getErrorResId());
            }
            if (null != config.getPlaceHolderResId()) {
                options.placeholder(config.getPlaceHolderResId());
            }
//            if (config.isCropCircle()) {
//                options.transform(new CropCircleTransformation(context));
//            }
//            if (config.isBlur()) {
//                options.transform(new BlurTransformation(context));
//            }
            if (config.isRoundedCorners()) {
                options.transform(new RoundedCorners(DensityUtils.dp2px(5)));
//                options.transform(new RoundedCornersTransformation(context, DensityUtils.dp2px(5), 0));
            }
            if (null != config.getSize()) {
                options.override(config.getSize().getWidth(), config.getSize().getHeight());
            }
            if (null != listener) {
                setListener(builder, listener);
            }
            if (null != config.getThumbnailUrl()) {
                RequestBuilder thumbnailRequest = Glide.with(context).asBitmap().load(config.getThumbnailUrl());
                builder.thumbnail(thumbnailRequest).apply(options).into(view);
            } else {
                builder.apply(options).into(view);
            }
        } catch (Exception e) {
            view.setImageResource(config.getErrorResId());
        }
    }

    @SuppressLint("CheckResult")
    private void setListener(RequestBuilder request, final ImageLoaderListener listener) {
        request.listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if (!"divide by zero".equals(e.getMessage())) {
                    listener.onError();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                listener.onSuccess();
                return false;
            }
        });
    }

    /**
     * 加载bitmap
     *
     * @param context
     * @param url
     * @param listener
     */
    @Override
    public void loadBitmap(Context context, String url, final BitmapLoaderListener listener) {
        if (url == null) {
            if (listener != null) {
                listener.onError();
            }
        } else {
            Glide.with(context).asBitmap().
                    load(url).
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (listener != null) {
                                listener.onSuccess(resource);
                            }
                        }
                    });
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    @Override
    public void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    @Override
    public void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    @Override
    public void clearDiskCache(final Context context) {
        try {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearDiskCache();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存
     *
     * @param context
     */
    @Override
    public void clearAllCache(Context context) {
        clearDiskCache(context);
        Glide.get(context).clearMemory();
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    @Override
    public synchronized long getDiskCacheSize(Context context) {
        /*File cacheDir = PathUtils.getDiskCacheDir(context, CacheConfig.IMG_DIR);

        if (cacheDir != null && cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                File[] arr$ = files;
                int len$ = files.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File imageCache = arr$[i$];
                    if (imageCache.isFile()) {
                        size += imageCache.length();
                    }
                }
            }
        }*/
        return 0L;
    }

    @Override
    public void clearTarget(View view) {
        Glide.with(view).clear(view);
    }

    @Override
    public void onTrimMemory(Context context, int level) {
//        Glide.with(context).onTrimMemory(level);
    }

    @Override
    public void onLowMemory(Context context) {
//        Glide.with(context).onLowMemory();
    }
}