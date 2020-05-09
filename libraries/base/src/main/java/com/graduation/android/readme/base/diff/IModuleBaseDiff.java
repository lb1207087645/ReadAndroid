package com.graduation.android.readme.base.diff;

import com.graduation.android.readme.base.widget.state.CommonState;

/**
 * 显示不同的加载状态
 *
 * @author hongrongyao
 * @function
 * @date 2018/7/23 0023
 */
public interface IModuleBaseDiff {

    /**
     * 显示错误时的View
     *
     * @param mStateView
     * @param baseViewCallback
     */
    void showErrorView(CommonState mStateView, String errorType, BaseViewCallback baseViewCallback);

    /**
     * 显示Empty时的View
     *
     * @param mStateView
     * @param text
     * @param isShowTopCurve
     */
    void showEmptyViewDefault(CommonState mStateView, String text, boolean isShowTopCurve);

    /**
     * 显示Loading时的View
     *
     * @param mStateView
     */
    void showLoadingView(CommonState mStateView);

//    /**
//     * 获取AppProject
//     *
//     * @return
//     */
//    AppProject getAppProject();
//
//    /**
//     * 获取AppDefaultHeader
//     *
//     * @return
//     */
//    AppDefaultHeader getAppDefaultHeader();
}
