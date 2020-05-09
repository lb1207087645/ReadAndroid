package com.graduation.android.readme.base.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * IPresenter
 */
public interface IPresenter<V extends BaseView> extends LifecycleObserver {

    /**
     * presenter 与对应的View 进行绑定，方便p层调用View层
     *
     * @param mvpView
     */
    void attachView(V mvpView);

    /**
     * presenter与View 解绑，处理一些内存泄漏问题
     */
    void detachView();


    /**
     * 与生命周期绑定
     *
     * @param owner
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
//每个生命周期都会调用一次
    void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event);

}
