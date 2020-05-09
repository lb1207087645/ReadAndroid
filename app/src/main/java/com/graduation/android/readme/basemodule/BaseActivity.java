package com.graduation.android.readme.basemodule;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduation.android.readme.R;
import com.graduation.android.readme.base.CommonApplication;
import com.graduation.android.readme.base.mvp.BaseMvpActivity;
import com.graduation.android.readme.base.mvp.BaseView;
import com.graduation.android.readme.base.mvp.IPresenter;
import com.graduation.android.readme.base.utils.DensityUtil;
import com.graduation.android.readme.base.utils.StatusBarUtil;
import com.graduation.android.readme.base.utils.ToolbarUtil;


/**
 * Activity基类的实现
 */
public abstract class BaseActivity<P extends IPresenter<V>, V extends BaseView> extends BaseMvpActivity<P, V> {





    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, com.graduation.android.readme.R.color.replaceable_color_navigation_bar_bg));
    }

    /**
     * toolbar样式，可在此修改
     *
     * @param savedInstanceState
     */
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mToolbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.common_title_bar_size)));


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mToolbar.setFitsSystemWindows(true);
        }
        mToolbar.setVisibility(View.VISIBLE);
        ((TextView) getToolbarTitleView()).setTextColor(ContextCompat.getColor(this, R.color.black));
        ((TextView) getToolbarTitleView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


        TextView tv_left = (TextView) getToolbarLeftView();
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.replaceable_drawable_back);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, DensityUtil.dp2px(8), DensityUtil.dp2px(15));//第一个是距离左边距离，第一个是距离上边距离，后面两个是宽高
//        tv_left.setCompoundDrawablePadding(12);
        tv_left.setCompoundDrawables(drawable, null, null, null);
        tv_left.setOnClickListener(v -> finish());//回退按钮消失

        ((TextView) getToolbarRightView()).setTextColor(ContextCompat.getColor(this, R.color.replaceable_color_title));
        ((TextView) getToolbarRightView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        //适配状态栏
        ToolbarUtil.setToolbar(mToolbar);
        setSupportActionBar(mToolbar);

    }
}
