package com.graduation.android.readme.base.widget.state.callback;


import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.widget.state.CommonBaseCallback;

/**
 * 错误状态UI
 *
 * @date：2017/10/10
 * @author：chenqq
 * @company: www.babybus.com
 * @email: ym_qqchen@sina.com
 */

public class ErrorCallback extends CommonBaseCallback implements IErrorCallback {

    @Override
    protected int onCreateView() {
        return R.layout.common_error_view;
    }
}
