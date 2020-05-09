package com.graduation.android.readme.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * function：禁止手势滑动的ViewPager
 *
 * @ author： linjiliang 2016/10/13 14:16
 */
public class NoScrollHorizontalViewPager extends ViewPager {
    private boolean disable = false;

    public NoScrollHorizontalViewPager(Context context) {
        super(context);
    }

    public NoScrollHorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        try {
            return disable && super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        try {
            return disable && super.onTouchEvent(arg0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);// 表示切换的时候，不需要切换时间。
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}