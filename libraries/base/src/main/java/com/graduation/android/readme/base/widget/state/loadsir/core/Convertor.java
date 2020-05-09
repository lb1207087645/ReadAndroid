package com.graduation.android.readme.base.widget.state.loadsir.core;


import com.graduation.android.readme.base.widget.state.loadsir.callback.Callback;

/**
 * Description:TODO
 * Create Time:2017/9/4 8:58
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface Convertor<T> {
   Class<?extends Callback> map(T t);
}
