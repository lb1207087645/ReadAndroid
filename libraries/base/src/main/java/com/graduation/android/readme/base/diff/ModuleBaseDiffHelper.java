package com.graduation.android.readme.base.diff;

import com.graduation.android.readme.base.widget.state.CommonState;

/**
 * 不同加载状态的管理类
 * 单例+策略模式
 *
 * @author hongrongyao
 * @function
 * @date 2018/7/23 0023
 */
public class ModuleBaseDiffHelper implements IModuleBaseDiff {

    private IModuleBaseDiff moduleBaseDiff;

    private ModuleBaseDiffHelper() {
    }

    public static ModuleBaseDiffHelper getInstance() {
        return ModuleBaseDiffHelperHolder.INSTANCE;
    }

    private static class ModuleBaseDiffHelperHolder {
        private static final ModuleBaseDiffHelper INSTANCE = new ModuleBaseDiffHelper();
    }

    public void registerModuleBaseDiff(IModuleBaseDiff moduleBaseDiff) {
        this.moduleBaseDiff = moduleBaseDiff;
    }

    @Override
    public void showErrorView(CommonState mStateView, String errorType, BaseViewCallback baseViewCallback) {
        if (!checkIsRegister()) {
            return;
        }

        moduleBaseDiff.showErrorView(mStateView, errorType, baseViewCallback);
    }

    @Override
    public void showEmptyViewDefault(CommonState mStateView, String text, boolean isShowTopCurve) {
        if (!checkIsRegister()) {
            return;
        }

        moduleBaseDiff.showEmptyViewDefault(mStateView, text, isShowTopCurve);
    }

    @Override
    public void showLoadingView(CommonState mStateView) {
        if (!checkIsRegister()) {
            return;
        }

        moduleBaseDiff.showLoadingView(mStateView);
    }

//    @Override
//    public AppProject getAppProject() {
//        if (!checkIsRegister()) {
//            return null;
//        }
//
//        return moduleBaseDiff.getAppProject();
//    }
//
//    @Override
//    public AppDefaultHeader getAppDefaultHeader() {
//        if (!checkIsRegister()) {
//            return null;
//        }
//
//        return moduleBaseDiff.getAppDefaultHeader();
//    }

    public boolean checkIsRegister() {
        return moduleBaseDiff == null ? false : true;
    }
}
