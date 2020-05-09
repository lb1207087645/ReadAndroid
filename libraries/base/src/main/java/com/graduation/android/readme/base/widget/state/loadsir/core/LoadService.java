package com.graduation.android.readme.base.widget.state.loadsir.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.graduation.android.readme.base.widget.state.loadsir.callback.Callback;
import com.graduation.android.readme.base.widget.state.loadsir.callback.SuccessCallback;

import java.util.List;

/**
 * Description:TODO
 * Create Time:2017/9/6 10:05
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class LoadService<T> {
    private LoadLayout loadLayout;
    private Convertor<T> convertor;

    /**
     * 构造方法：
     * 使用注册的target与listener，生产一个LoadLayout布局，然后替换掉target；
     * 同时默认的添加一个SuccessCallback到LoadLayout中；
     * 然后初始化使用LoadSir Builder配置好的所有callback。
     *
     * @param convertor
     * @param targetContext
     * @param onReloadListener
     * @param builder
     */
    LoadService(Convertor<T> convertor, TargetContext targetContext, Callback
            .OnReloadListener onReloadListener, LoadSir.Builder builder) {
        this.convertor = convertor;
        Context context = targetContext.getContext();
        View oldContent = targetContext.getOldContent();
        loadLayout = new LoadLayout(context, onReloadListener);
        loadLayout.addCallback(new SuccessCallback(oldContent, context, onReloadListener));
        if (targetContext.getParentView() != null) {
            targetContext.getParentView().addView(loadLayout, targetContext.getChildIndex(), oldContent
                    .getLayoutParams());
        }
        initCallback(builder);
    }

    /**
     * 将Builder中的callback配置添加到LoadLayout实例中，同时现实默认的callback。
     *
     * @param builder
     */
    private void initCallback(LoadSir.Builder builder) {
        List<Callback> callbacks = builder.getCallbacks();
        Class<? extends Callback> defalutCallback = builder.getDefaultCallback();
        if (callbacks != null && callbacks.size() > 0) {
            for (Callback callback : callbacks) {
                loadLayout.setupCallback(callback);
            }
        }
        if (defalutCallback != null) {
            loadLayout.showCallback(defalutCallback);
        }
    }

    public void showSuccess() {
        loadLayout.showCallback(SuccessCallback.class);
    }

    public void showCallback(Class<? extends Callback> callback) {
        loadLayout.showCallback(callback);
    }

    public void showWithConvertor(T t) {
        if (convertor == null) {
            throw new IllegalArgumentException("You haven't set the Convertor.");
        }
        loadLayout.showCallback(convertor.map(t));
    }

    public LoadLayout getLoadLayout() {
        return loadLayout;
    }

    /**
     * 将布局中的titleView解耦出来，使其与LoadLayout布局平级，这样就可以不受LoadSir的影响，主要针对target为Activity与Fragment
     * obtain rootView if you want keep the toolbar in Fragment
     *
     * @since 1.2.2
     */
    public LinearLayout getTitleLoadLayout(Context context, ViewGroup rootView, View titleView) {
        LinearLayout newRootView = new LinearLayout(context);
        newRootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        newRootView.setLayoutParams(layoutParams);
        rootView.removeView(titleView);
        newRootView.addView(titleView);
        newRootView.addView(loadLayout, layoutParams);
        return newRootView;
    }

    /**
     * 动态设置callback的view上响应事件，覆盖之前配置好的事件。
     * modify the callback dynamically
     *
     * @param callback  which callback you want modify(layout, event)
     * @param transport a interface include modify logic
     * @since 1.2.2
     */
    public void setCallBack(Class<? extends Callback> callback, Transport transport) {
        loadLayout.setCallBack(callback, transport);
    }
}
