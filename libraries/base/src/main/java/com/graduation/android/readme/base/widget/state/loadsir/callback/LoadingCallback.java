package com.graduation.android.readme.base.widget.state.loadsir.callback;


import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.widget.state.CommonBaseCallback;

/**
 * 正在加载callback
 */
public class LoadingCallback extends CommonBaseCallback {

    @Override
    protected int onCreateView() {
        return R.layout.common_view_loading;
    }
}
