package com.graduation.android.readme.base.network;

import android.app.Dialog;

import com.graduation.android.readme.base.core.CacheClient;
import com.graduation.android.readme.base.model.CacheMode;
import com.graduation.android.readme.base.utils.L;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 控制操作线程的辅助类
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
public class Transformer {

    /**
     * 带参数  显示loading对话框
     *
     * @param <T> 泛型
     * @return 返回Observable
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return switchSchedulers(null);
    }

    public static <T> ObservableTransformer<T, T> switchSchedulers(final Dialog dialog) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (dialog != null) {
                                    dialog.show();
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

//    public static <T> ObservableTransformer<T, T> switchCache(Type type) {
//        return switchCache(CacheClient.getInstance().getCacheMode(), type);
//    }
//
//    public static <T> ObservableTransformer<T, T> switchCache(CacheMode cacheMode, Type type) {
//        return switchCache(cacheMode, "", type);
//    }

    public static <T> ObservableTransformer<T, T> switchCache(CacheMode cacheMode, String key, Type type) {
        return switchCache(cacheMode, key, CacheClient.getInstance().getCacheTime(key), type);
    }

    public static <T> ObservableTransformer<T, T> switchCache(CacheMode cacheMode, String key, long time, Type type) {
        L.e("rxcache", "Transformer switchCache key=" + key);
        CacheClient.getInstance().setCacheTime(key, time);
        CacheClient.getInstance().setCacheKey(key);
        return CacheClient.getInstance().getRxCache().transformer(cacheMode, key, time, type);
    }
}