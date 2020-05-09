package com.graduation.android.readme.base.widget.state;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.widget.state.loadsir.callback.Callback;


/**
 * 统一处理 动画
 * 状态UI使用的基础Callback封装，统一处理了图片动画的start与stop。
 *
 * @date：2017/10/11
 * @author：chenqq
 * @company: www.babybus.com
 * @email: ym_qqchen@sina.com
 */

public abstract class CommonBaseCallback extends Callback {

    private AnimationDrawable animationDrawable;

    @Override
    protected abstract int onCreateView();

    @Override
    public void onAttach(Context context, View view) {
        super.onAttach(context, view);

        ImageView iv = (ImageView) view.findViewById(R.id.common_iv_state);
        if (iv != null && iv.getDrawable() instanceof AnimationDrawable) {
            animationDrawable = (AnimationDrawable) iv.getDrawable();
        }

        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
