package com.graduation.android.readme.base.mvp;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.CallSuper;

import com.graduation.android.readme.base.IDisposableRelease;
import com.graduation.android.readme.base.model.CacheMode;
import com.graduation.android.readme.base.network.BaseSimpleObserver;
import com.graduation.android.readme.base.network.Transformer;
import com.graduation.android.readme.base.utils.L;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import io.reactivex.Observable;


/**
 * 基类Presenter
 *
 * @param <V>
 */
public class BasePresenter<V extends BaseView> implements IPresenter<V> {

    private WeakReference<V> mView;//view改成弱引用
    private final List<IDisposableRelease> mObserver = new ArrayList<>();

    @Override
    public void attachView(V mvpView) {
        this.mView = new WeakReference<>(mvpView);

    }

    @Override
    public void detachView() {//防止内存泄漏
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        releaseObserver();

    }


    /**
     * 退出界面前，释放所有观察者资源
     */
    protected void releaseObserver() {
        Iterator<IDisposableRelease> it = mObserver.iterator();
        while (it.hasNext()) {
            IDisposableRelease observer = it.next();
            observer.release();
            it.remove();
        }
    }


    public boolean isAttachView() {
        return mView != null && mView.get() != null;
    }


    /**
     * 获取目标View
     *
     * @return
     */
    public V getView() {
        return mView == null ? null : mView.get();
    }

    /**
     * 检查View 和presenter是否连接
     */
    public void checkViewAttach() {
        if (!isAttachView()) {
            throw new MvpViewNotAttachedException();
        }

    }


    /**
     * 订阅，并默认进行线程转换，工作线程为io
     *
     * @param observable
     * @param observer
     */
    protected void subscribe(Observable observable, BaseSimpleObserver observer) {
        mObserver.add(observer);
        observable.compose(Transformer.switchSchedulers()).subscribe(observer);
    }

    /**
     * 接口自定义缓存失效时间
     *
     * @param observable
     * @param observer
     * @param cacheMode
     * @param key
     * @param time
     * @param type
     */
    protected void subscribe(Observable observable, BaseSimpleObserver observer, CacheMode cacheMode, String key, long time, Type type) {
        mObserver.add(observer);
        observable.compose(Transformer.switchSchedulers())
                .compose(Transformer.switchCache(cacheMode, key, time, type))
                .subscribe(observer);
    }

    /**
     * 使用全局缓存失效时间
     *
     * @param observable
     * @param observer
     * @param cacheMode
     * @param key
     * @param type
     */
    protected void subscribe(Observable observable, BaseSimpleObserver observer, CacheMode cacheMode, String key, Type type) {
        mObserver.add(observer);
        observable.compose(Transformer.switchSchedulers())
                .compose(Transformer.switchCache(cacheMode, key, type))
                .subscribe(observer);
    }

    /**
     * 自定义异常
     */
    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("请求数据前请先调用 attachView(MvpView) 方法与View建立连接");
        }
    }

    @Override
    public void onCreate(LifecycleOwner owner) {
        L.e("BasePresenter", "BasePresenter: onCreate");
    }

    @Override
    public void onStart() {
        L.e("BasePresenter", "BasePresenter: onStart");
    }

    @Override
    public void onResume() {
        L.e("BasePresenter", "BasePresenter: onResume");
    }

    @Override
    public void onPause() {
        L.e("BasePresenter", "BasePresenter: onPause");
    }

    @Override
    public void onStop() {
        L.e("BasePresenter", "BasePresenter: onStop");
    }

    @Override
    @CallSuper
    public void onDestroy(LifecycleOwner owner) {
        L.e("BasePresenter", "BasePresenter: onDestroy");
        releaseObserver();
    }

    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {
        L.e("BasePresenter", "BasePresenter: onLifecycleChanged event=" + event.name());
    }

}
