package com.graduation.android.readme.base;

import android.app.Activity;
import android.os.Bundle;

import com.graduation.android.readme.base.image.ImageLoaderManager;
import com.graduation.android.readme.base.utils.L;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BaseApplication extends CommonApplication {

    /**
     * 维护Activity 的list
     */
    private static final List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityListener();
        ImageLoaderManager.getInstance().init();//初始化imageLoader相关

//        CommonState.Config.initGlobalConfig();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoaderManager.getInstance().onTrimMemory(this, level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoaderManager.getInstance().onLowMemory(this);
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    private void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    private void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }


    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        return mActivitys.get(mActivitys.size() - 1);
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Iterator iterator = mActivitys.iterator();
        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();

                activity.finish();
                activity = null;
            }
        }
//        for (Activity activity : mActivitys) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * 获取上一个入栈的activity
     *
     * @return
     */
    public static Activity getLastActivity() {
        Activity activity;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 2;
            if (size < 0) {
                return null;
            }
            activity = mActivitys.get(size);
        }
        return activity;
    }

    /**
     * @return 获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;
    }

    /**
     * @return 获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = getTopActivity();
        if (mBaseActivity == null) {
            return "";
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity,退出应用程序
     */
    public static void finishAllActivity(Activity act) {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (act == activity) {
                continue;
            } else {
                activity.finish();
            }
        }
        mActivitys.clear();
        mActivitys.add(act);
    }

    /**
     * 结束所有Activity,退出应用程序
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                /**
                 *  监听到 Activity创建事件 将该 Activity 加入list
                 */
                L.i(activity.getClass().getSimpleName() + " onCreated bundle=" + savedInstanceState);
                pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                L.i(activity.getClass().getSimpleName() + " onStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                L.i(activity.getClass().getSimpleName() + " onResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                L.i(activity.getClass().getSimpleName() + " onPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                L.i(activity.getClass().getSimpleName() + " onStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                L.i(activity.getClass().getSimpleName() + " onSaveInstanceState bundle=" + outState);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                L.i(activity.getClass().getSimpleName() + " onDestroyed");
                if (null == mActivitys || mActivitys.isEmpty()) {
                    return;
                }
                if (mActivitys.contains(activity)) {
                    /**
                     *  监听到 Activity销毁事件 将该Activity 从list中移除
                     */
                    popActivity(activity);
                }
            }
        });
    }
}

