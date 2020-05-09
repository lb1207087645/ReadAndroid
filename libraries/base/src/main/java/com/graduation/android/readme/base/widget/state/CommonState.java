package com.graduation.android.readme.base.widget.state;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.mvp.IFetchData;
import com.graduation.android.readme.base.widget.state.callback.EmptyCallback;
import com.graduation.android.readme.base.widget.state.callback.ErrorCallback;
import com.graduation.android.readme.base.widget.state.callback.IEmptyCallback;
import com.graduation.android.readme.base.widget.state.callback.IErrorCallback;
import com.graduation.android.readme.base.widget.state.callback.ILoadingCallback;
import com.graduation.android.readme.base.widget.state.callback.LoadingCallback;
import com.graduation.android.readme.base.widget.state.loadsir.callback.Callback;
import com.graduation.android.readme.base.widget.state.loadsir.callback.SuccessCallback;
import com.graduation.android.readme.base.widget.state.loadsir.core.LoadService;
import com.graduation.android.readme.base.widget.state.loadsir.core.LoadSir;
import com.graduation.android.readme.base.widget.state.loadsir.core.Transport;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>页面展示各种状态的工具类，基于LoadSir进行二次封装。</p>
 * <p>目前提供4种状态UI展示：<strong>Loading, Error, Empty, Success</strong>。</p>
 * <p>每种状态都默认了处理事件，若需要自定义处理事件，请使用带<strong>Transport</strong>参数方法，
 * 使用其回调的View进行findViewById找出相应的UI进行设置。</p>
 *
 * <p>默认状态布局中，包含两种View并且使用固定的id。</p>
 * <p>ImageView其id=common_iv_state。</p>
 * <p>TextView其id=common_tv_tips。</p>
 *
 * <p>使用该工具时，需要先在全局位置进行UI布局的初始化，初始化方法{@link CommonState.Config}静态类下，
 * 请在Application#create方法中进行调用。然后在需要的地方使用new CommonStateView(targetView)创建实例，
 * 然后使用该实例进行状态切换。</p>
 * <p>该工具方法中，自动处理了状态图片自带动画的start与stop处理。</p>
 * <p>
 * <p>在core模块中已经内置调用，无需特殊处理。</p>
 *
 * @date：2017/10/10
 * @author：chenqq
 * @company: www.babybus.com
 * @email: ym_qqchen@sina.com
 */

public final class CommonState {

    private static Class EMPTY_CALLBACK = EmptyCallback.class;//空视图
    private static Class ERROR_CALLBACK = ErrorCallback.class;//错误视图
    private static Class LOADING_CALLBACK = LoadingCallback.class;//加载视图


    private LoadService mService;


    public CommonState(Object targetView) {
        mService = LoadSir.getDefault().register(targetView, null);
    }

    public static class Config {
        /**
         * <p>初始化一套状态UI布局，默认已经在BaseApplication中注册。</p>
         * <p>
         * <Strong>若未继承BaseApplication，请在具体的Application中进行初始化。</Strong>
         */
        public static void initGlobalConfig() {
            List<Callback> callbacks = new ArrayList<>();
            callbacks.add(new LoadingCallback());
            callbacks.add(new ErrorCallback());
            callbacks.add(new EmptyCallback());
            initGlobalConfig(callbacks, SuccessCallback.class);
        }

        public static void initGlobalConfig(List<Callback> callbacks, Class<? extends Callback>
                defaultCallback) {
            if (callbacks == null) {
                initGlobalConfig();
                throw new IllegalArgumentException("callbacks为空，定制失败，因此使用了默认的配置！");
            }
            LoadSir.Builder builder = getBuilder();

            for (Callback callback : callbacks) {
                if (callback instanceof ILoadingCallback) {
                    LOADING_CALLBACK = callback.getClass();
                }

                if (callback instanceof IErrorCallback) {
                    ERROR_CALLBACK = callback.getClass();
                }

                if (callback instanceof IEmptyCallback) {
                    EMPTY_CALLBACK = callback.getClass();
                }

                builder.addCallback(callback);
            }

            if (LOADING_CALLBACK == LoadingCallback.class) {
                builder.addCallback(new LoadingCallback());
            }

            if (ERROR_CALLBACK == ErrorCallback.class) {
                builder.addCallback(new ErrorCallback());
            }

            if (EMPTY_CALLBACK == EmptyCallback.class) {
                builder.addCallback(new EmptyCallback());
            }

            if (defaultCallback == null) {
                defaultCallback = SuccessCallback.class;
            }

            builder.setDefaultCallback(defaultCallback).commit();
        }

        public static void setDefaultLoadingCallback(ILoadingCallback callback) {
            LOADING_CALLBACK = callback.getClass();
        }

        public static LoadSir.Builder getBuilder() {
            return LoadSir.beginBuilder();
        }
    }


    /**
     * <p>展示错误状态UI，用于特殊UI控件的事件定制实现。</p>
     *
     * @param transport 定制UI控件的响应事件
     */
    public void showErrorView(Transport transport) {
        mService.setCallBack(ERROR_CALLBACK, transport);
        mService.showCallback(ERROR_CALLBACK);
    }

    /**
     * <p>显示错误状态UI，默认了错误状态UI上的点击事件。</p>
     * <p>再试试则调用loadData方法。</p>
     *
     * @param fetchData 实现了IFetchData接口的实例
     */
    public void showErrorView(final IFetchData fetchData) {
        showErrorView(new Transport() {
            @Override
            public void order(Context context, View view) {
                view.findViewById(R.id.common_tv_tips).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchData.loadData();
                    }
                });
            }
        });
    }

    /**
     * <p>展示无数据状态UI，用于特殊UI控件的事件定制实现。</p>
     *
     * @param transport 定制UI控件的响应事件
     */
    public void showEmptyView(Transport transport) {
        mService.setCallBack(EMPTY_CALLBACK, transport);
        mService.showCallback(EMPTY_CALLBACK);
    }

    /**
     * <p>展示无数据状态UI，默认设置了一个图片展示，同时默认点击图片会再次调用loadData方法。</p>
     *
     * @param fetchData  实现了IFetchData接口的实例
     * @param emptyImgId 具体需要用于展示的图片资源ID。
     */
    public void showEmptyView(final IFetchData fetchData, final int emptyImgId) {
        showEmptyView(new Transport() {
            @Override
            public void order(Context context, View view) {
                ImageView iv = (ImageView) view.findViewById(R.id.common_iv_state);
                if (emptyImgId <= 0) {
                    iv.setImageResource(R.drawable.common_image_default);
                } else {
                    iv.setImageResource(emptyImgId);
                }

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchData.loadData();
                    }
                });
            }
        });
    }

    /**
     * <p>展示正在加载状态UI，用于特殊UI控件的事件定制实现。</p>
     *
     * @param transport 定制UI控件的响应事件
     */
    public void showLoadingView(Transport transport) {
        if (transport != null) {
            mService.setCallBack(LOADING_CALLBACK, transport);
        }
        mService.showCallback(LOADING_CALLBACK);
    }

    /**
     * <p>展示正在加载状态UI，默认展示无处理事件。</p>
     */
    public void showLoadingView() {
        showLoadingView(null);
    }

    public void showContentView() {
        mService.showSuccess();
    }

    public void setupCallback(Callback callback, Transport transport) {
        mService.getLoadLayout().setupCallback(callback);
        mService.setCallBack(callback.getClass(), transport);
    }

    public void showCallback(Class<? extends Callback> callback) {
        mService.showCallback(callback);
    }

    public View getLoadLayout() {
        return mService.getLoadLayout();
    }
}
