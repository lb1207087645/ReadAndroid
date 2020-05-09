package com.graduation.android.readme.base.widget.state.loadsir.callback;


import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.widget.state.CommonBaseCallback;

/**
 * 空视图callback
 * @author hongrongyao
 * @function
 * @date 2017/12/1 0001
 */
public class EmptyDefaultCallBack extends CommonBaseCallback {
    @Override
    protected int onCreateView() {
        return R.layout.common_view_empty_default;
    }
}
