package com.graduation.android.readme.base.image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.graduation.android.readme.base.image.listener.BitmapLoaderListener;
import com.graduation.android.readme.base.image.listener.ImageLoaderListener;

public interface IImageLoader {

    void loadGif(ImageView view, String gifUrl);

    void loadGif(ImageView view, String gifUrl, ImageLoadConfig config);

    void loadGif(ImageView view, String gifUrl, ImageLoadConfig config, ImageLoaderListener listener);

    void loadImage(ImageView view, String imgUrl);

    void loadImage(ImageView view, String imgUrl, ImageLoadConfig config);

    void loadImage(ImageView view, int resourceId, ImageLoadConfig config);

    /**
     * 加载String类型的资源
     * SD卡资源："file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg"<p/>
     * assets资源："file:///android_asset/f003.gif"<p/>
     * raw资源："Android.resource://com.frank.glide/raw/raw_1"或"android.resource://com.frank.glide/raw/"+R.raw.raw_1<p/>
     * drawable资源："android.resource://com.frank.glide/drawable/news"或load"android.resource://com.frank.glide/drawable/"+R.drawable.news<p/>
     * ContentProvider资源："content://media/external/images/media/139469"<p/>
     * http资源："http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg"<p/>
     * https资源："https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp"<p/>
     *
     * @param view
     * @param imgUrl
     * @param config
     * @param listener
     */
    void loadImage(ImageView view, String imgUrl, ImageLoadConfig config, ImageLoaderListener listener);

    void loadImage(ImageView view, int resourceId, ImageLoadConfig config, ImageLoaderListener listener);

    /**
     * 加载bitmap
     *
     * @param context
     * @param url
     * @param listener
     */
    void loadBitmap(Context context, String url, final BitmapLoaderListener listener);

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    void cancelAllTasks(Context context);

    /**
     * 恢复所有任务
     */
    void resumeAllTasks(Context context);

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    void clearDiskCache(Context context);

    /**
     * 清除所有缓存
     *
     * @param context
     */
    void clearAllCache(Context context);

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    long getDiskCacheSize(Context context);

    void clearTarget(View view);

    void onTrimMemory(Context context, int level);

    void onLowMemory(Context context);

}