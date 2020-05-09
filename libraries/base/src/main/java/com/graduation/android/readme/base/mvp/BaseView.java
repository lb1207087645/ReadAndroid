/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graduation.android.readme.base.mvp;


import com.graduation.android.readme.base.network.ErrorEntity;

/**
 * View 层
 */
public interface BaseView {


    boolean isActive();

    void showProgress();

    void dismissProgress();

    void showTip(String message);


    /**
     * 显示进度条
     *
     * @param msg 进度条加载内容
     */
    void showLoadingDialog(String msg);

    /**
     * 隐藏进度条
     */
    void hideLoadingDialog();

    /**
     * 显示加载错误
     *
     * @param err 错误体
     */
    void showErr(ErrorEntity err);

    /**
     * 显示全屏错误页面
     */
    void showErrorView();

    /**
     * 显示全屏错误页面
     */
    void showErrorView(String errorType);

    /**
     * 显示正在加载页面
     */
    void showLoadingView();

    /**
     * 显示全屏错误页面
     */
    void showEmptyView(int imageId);

    /**
     * 显示全屏错误页面
     */
    void showContentView();
}
