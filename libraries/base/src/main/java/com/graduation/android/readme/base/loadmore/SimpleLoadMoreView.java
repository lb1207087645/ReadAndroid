package com.graduation.android.readme.base.loadmore;


import com.graduation.android.readme.base.R;

/**
 * 加载更多
 *
 * @author
 * @Email lytjackson@gmail.com
 * @date 2017/7/18 0018
 */
public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.common_quick_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
