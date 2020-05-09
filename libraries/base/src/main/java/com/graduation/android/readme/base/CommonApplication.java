package com.graduation.android.readme.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * desc: application基类
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
@SuppressLint("Registered")
public class CommonApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * 得到全局Context。
     *
     * @return context
     */
    public static Context getContext() {
        if (context == null) {
            throw new IllegalStateException("调用此方法获取context，必须使用自定义application继承CommonApplication");
        }
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = this;
    }

}
