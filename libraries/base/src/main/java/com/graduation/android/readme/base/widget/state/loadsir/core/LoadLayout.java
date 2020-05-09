package com.graduation.android.readme.base.widget.state.loadsir.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.graduation.android.readme.base.widget.state.loadsir.LoadSirUtil;
import com.graduation.android.readme.base.widget.state.loadsir.callback.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:TODO
 * Create Time:2017/9/2 17:02
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class LoadLayout extends FrameLayout {
    private Map<Class<? extends Callback>, Callback> callbacks = new HashMap<>();
    private Context context;
    private Callback.OnReloadListener onReloadListener;
    private Class<? extends Callback> preCallback;

    public LoadLayout(@NonNull Context context) {
        super(context);
    }

    public LoadLayout(@NonNull Context context, Callback.OnReloadListener onReloadListener) {
        this(context);
        this.context = context;
        this.onReloadListener = onReloadListener;
    }

    /**
     * 将LoadSir Builder配置的callback copy一份，然后把注册到target上的listener添加到callback使用的布局的点击事件上；
     * 并且使用.class作为key将copy到的callback记录到LoadLayout上的HashMap中。
     *
     * @param callback
     */
    public void setupCallback(Callback callback) {
        Callback cloneCallback = callback.copy();
        cloneCallback.setCallback(null, context, onReloadListener);
        addCallback(cloneCallback);
    }

    public void addCallback(Callback callback) {
        if (!callbacks.containsKey(callback.getClass())) {
            callbacks.put(callback.getClass(), callback);
        }
    }

    /**
     * 在主线程中显示指定的callback
     *
     * @param callback
     */
    public void showCallback(final Class<? extends Callback> callback) {
        checkCallbackExist(callback);
        if (LoadSirUtil.isMainThread()) {
            showCallbackView(callback);
        } else {
            postToMainThread(callback);
        }
    }

    private void postToMainThread(final Class<? extends Callback> status) {
        post(new Runnable() {
            @Override
            public void run() {
                showCallbackView(status);
            }
        });
    }

    /**
     * 显示指定callback view：先停止上一个callback view，会调用onDetach方法，
     * 在Callback上可以做一些销毁前的处理，然后在移除所有view；
     * 然后找到需要展示的callback view将其添加到LoadLayout上进行展示，
     * 同时调用onAttach方法，在Callback上可以做一些展示出现后的处理。
     *
     * @param status
     */
    private void showCallbackView(Class<? extends Callback> status) {
        if (preCallback != null) {
            callbacks.get(preCallback).onDetach();
        }
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (Class key : callbacks.keySet()) {
            if (key == status) {
                View rootView = callbacks.get(key).getRootView();
                addView(rootView);
                callbacks.get(key).onAttach(context, rootView);
                preCallback = status;
            }
        }
    }

    /**
     * 动态设置callback的view上的响应事件。
     *
     * @param callback
     * @param transport
     */
    public void setCallBack(Class<? extends Callback> callback, Transport transport) {
        if (transport == null) {
            return;
        }
        checkCallbackExist(callback);
        transport.order(context, callbacks.get(callback).obtainRootView());
    }

    private void checkCallbackExist(Class<? extends Callback> callback) {
        if (!callbacks.containsKey(callback)) {
            throw new IllegalArgumentException(String.format("The Callback (%s) is nonexistent.", callback
                    .getSimpleName()));
        }
    }
}
