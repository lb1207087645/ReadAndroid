package com.graduation.android.readme.basemodule;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.graduation.android.readme.base.diff.BaseViewCallback;
import com.graduation.android.readme.base.diff.IModuleBaseDiff;
import com.graduation.android.readme.base.widget.state.CommonState;
import com.graduation.android.readme.base.widget.state.loadsir.callback.EmptyDefaultCallBack;
import com.graduation.android.readme.base.widget.state.loadsir.callback.LoadingCallback;
import com.graduation.android.readme.base.widget.state.loadsir.core.Transport;


/**
 * 单例+策略模式，针对不同的工程，传入不同的IModuleBaseDiff
 *
 * @author hongrongyao
 * @function
 * @date 2018/7/24 0024
 */
public class ModuleBaseDiffImpl implements IModuleBaseDiff {


    /**
     * 加载错误页面
     *
     * @param mStateView
     * @param errorType
     * @param baseViewCallback
     */
    @Override
    public void showErrorView(CommonState mStateView, final String errorType, final BaseViewCallback baseViewCallback) {

//        if (mStateView == null || baseViewCallback == null) {
//            return;
//        }
//        mStateView.setupCallback(new ErrorCallback(), new Transport() {
//            @Override
//            public void order(Context context, View view) {//
//
//                view.findViewById(R.id.common_tv_error_qa).setVisibility(View.INVISIBLE);
//
//                if (ErrorConstant.ERROR_TYPE_DATA_EMPTY_EXCEPTION.equals(errorType)) {
//                    view.findViewById(R.id.common_tv_error_refresh).setVisibility(View.GONE);
//                } else {
//                    view.findViewById(R.id.common_tv_error_refresh).setVisibility(View.VISIBLE);
//
//                    view.findViewById(R.id.common_tv_error_refresh).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            baseViewCallback.viewLoadData();
//                        }
//                    });
//                }
//
//                String text = getErrorString(errorType);
//                if (!TextUtils.isEmpty(text)) {
//                    ((TextView) view.findViewById(R.id.common_tv_no_net)).setText(text);
//                }
//
//                view.findViewById(R.id.common_ll_net_error).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        baseViewCallback.viewLoadData();
//                    }
//                });
//            }
//        });
//        mStateView.showCallback(ErrorCallback.class);
    }


    /**
     * 空视图
     *
     * @param mStateView
     * @param text
     * @param isShowTopCurve
     */
    @Override
    public void showEmptyViewDefault(CommonState mStateView, final String text, final boolean isShowTopCurve) {
        if (mStateView == null) {
            return;
        }
        mStateView.setupCallback(new EmptyDefaultCallBack(), new Transport() {
            @Override
            public void order(Context context, View view) {
                if (isShowTopCurve) {
                    view.findViewById(com.graduation.android.readme.base.R.id.common_iv_empty_default_top_curve).setVisibility(View.VISIBLE);
                }
                if (text != null) {
                    ((TextView) view.findViewById(com.graduation.android.readme.base.R.id.common_tv_empty_describe)).setText(text);
                }
            }
        });
        mStateView.showCallback(EmptyDefaultCallBack.class);

    }


    /**
     * 正在加载页面
     *
     * @param mStateView
     */
    @Override
    public void showLoadingView(CommonState mStateView) {
        if (mStateView == null) {
            return;
        }
        mStateView.setupCallback(new LoadingCallback(), new Transport() {
            @Override
            public void order(Context context, View view) {
                view.findViewById(com.graduation.android.readme.base.R.id.common_tv_tips).setVisibility(View.GONE);
            }
        });
        mStateView.showCallback(LoadingCallback.class);
    }

//    @Override
//    public AppProject getAppProject() {
//        return AppProject.ER_GE;
//    }
//
//    @Override
//    public AppDefaultHeader getAppDefaultHeader() {
//        return AppDefaultHeader.XXTEA;
//    }


    /**
     * 请求后不同网络异常的处理
     * @param errorType
     * @return
     */
//    private String getErrorString(String errorType) {
//
//        if (TextUtils.isEmpty(errorType)) return "";
//
//        switch (errorType) {
//
//            case ErrorConstant.ERROR_TYPE_NET_EXCEPTION:
//            case ErrorConstant.ERROR_TYPE_CODE_EXCEPTION:
//                if (NetworkUtils.isConnected(BaseApplication.getContext())) {
//                    return CommonApplication.getContext().getString(R.string.common_week_net_exception);
//                } else {
//                    return CommonApplication.getContext().getString(R.string.common_net_exception);
//                }
//
//            case ErrorConstant.ERROR_TYPE_SERVER_EXCEPTION:
//                return CommonApplication.getContext().getString(R.string.common_server_exception);
//
//            case ErrorConstant.ERROR_TYPE_DATA_EMPTY_EXCEPTION:
//                return CommonApplication.getContext().getString(R.string.common_data_empty_exception);
//
//            case ErrorConstant.ERROR_TYPE_CONNECT_EXCEPTION:
//                if (NetworkUtils.isConnected(BaseApplication.getContext())) {
//                    return CommonApplication.getContext().getString(R.string.common_server_exception);
//                } else {
//                    return CommonApplication.getContext().getString(R.string.common_net_exception);
//                }
//
//            default:
//                break;
//        }
//
//        return "";
//    }
}
