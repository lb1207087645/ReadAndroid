package com.graduation.android.readme.base.image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.graduation.android.readme.base.image.listener.BitmapLoaderListener;
import com.graduation.android.readme.base.image.listener.ImageLoaderListener;

/**
 * desc: 图片加载器
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
public class ImageLoaderManager implements IImageLoader {

    private IImageLoader mImageLoader;

    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    public void setImageLoader(IImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    // 在application的oncreate中初始化
    public void init() {
        //        mImageLoader = new FrescoImageLoader();
        //        mImageLoader.init(context);
        mImageLoader = new GlideImageLoader();
    }

    @Override
    public void loadGif(ImageView view, String gifUrl) {
        mImageLoader.loadGif(view, gifUrl, null);
    }

    @Override
    public void loadGif(ImageView view, String gifUrl, ImageLoadConfig config) {
        mImageLoader.loadGif(view, gifUrl, config, null);
    }

    @Override
    public void loadGif(ImageView view, String gifUrl, ImageLoadConfig config, ImageLoaderListener listener) {
        mImageLoader.loadGif(view, gifUrl, config, listener);
    }

    @Override
    public void loadImage(ImageView view, String imgUrl) {
        mImageLoader.loadImage(view, imgUrl, null);
    }

    @Override
    public void loadImage(ImageView view, String imgUrl, ImageLoadConfig config) {
        mImageLoader.loadImage(view, imgUrl, config, null);
    }

    @Override
    public void loadImage(ImageView view, int resourceId, ImageLoadConfig config) {
        mImageLoader.loadImage(view, resourceId, config, null);
    }

    @Override
    public void loadImage(ImageView view, String imgUrl, ImageLoadConfig config, ImageLoaderListener listener) {
        mImageLoader.loadImage(view, imgUrl, config, listener);
    }

    @Override
    public void loadImage(ImageView view, int resourceId, ImageLoadConfig config, ImageLoaderListener listener) {
        mImageLoader.loadImage(view, resourceId, config, listener);
    }

    @Override
    public void loadBitmap(Context context, String url, BitmapLoaderListener listener) {
        mImageLoader.loadBitmap(context, url, listener);
    }

    @Override
    public void cancelAllTasks(Context context) {
        mImageLoader.cancelAllTasks(context);
    }

    @Override
    public void resumeAllTasks(Context context) {
        mImageLoader.resumeAllTasks(context);
    }

    @Override
    public void clearDiskCache(Context context) {
        mImageLoader.clearDiskCache(context);
    }

    @Override
    public void clearAllCache(Context context) {
        mImageLoader.clearAllCache(context);
    }

    @Override
    public long getDiskCacheSize(Context context) {
        return mImageLoader.getDiskCacheSize(context);
    }

    @Override
    public void clearTarget(View view) {
        mImageLoader.clearTarget(view);
    }

    @Override
    public void onTrimMemory(Context context, int level) {
        mImageLoader.onTrimMemory(context, level);
    }

    @Override
    public void onLowMemory(Context context) {
        mImageLoader.onLowMemory(context);
    }
}