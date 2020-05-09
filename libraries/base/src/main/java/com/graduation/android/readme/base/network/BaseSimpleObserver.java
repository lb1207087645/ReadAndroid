package com.graduation.android.readme.base.network;

import com.graduation.android.readme.base.IDisposableRelease;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * desc: 基类BaseObserver
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
public abstract class BaseSimpleObserver<T> implements Observer<T>, IDisposableRelease {

    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onSubscribe (@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void release () {
        compositeDisposable.clear();
    }

}