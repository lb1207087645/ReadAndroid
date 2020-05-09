package com.graduation.android.readme.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.graduation.android.readme.BuildConfig;
import com.graduation.android.readme.base.BaseApplication;

import com.graduation.android.readme.basemodule.ModuleBaseDiffImpl;
import com.graduation.android.readme.base.diff.ModuleBaseDiffHelper;
import com.graduation.android.readme.base.network.RetrofitServiceManager;
import com.graduation.android.readme.base.widget.state.CommonState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.bmob.v3.Bmob;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class MainApplication extends BaseApplication implements Application.ActivityLifecycleCallbacks {

    private static final String AF_DEV_KEY = "rpCt9aGscypJY7ukA9HEwD";

    public static final String KEY = "59a8ff8e04e20532c60008a0";

    private String channel;
    private boolean isMainProcess;
    public static boolean isMainActivityExist = false;

    private static final String TAG = "MainApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "30968f8baac0b731aafedf25c16f8f28");


        // retrofit全局配置
        try {

            boolean isShowLog = BuildConfig.LOG_DEBUG;
            RetrofitServiceManager retrofitServiceManager = RetrofitServiceManager.getInstance()
                    .addConverterFactory(GsonConverterFactory.create());
//                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create());
//            if (BuildConfig.LOG_DEBUG) {
//                retrofitServiceManager.addInterceptor(new HostConfigInterceptor());//添加拦截器
//            }
            retrofitServiceManager
//                    .setSslSocketFactory()//信任所有证书
////                    .hostnameVerifier(new HostnameVerifier() {
////                        @Override
////                        public boolean verify(String hostname, SSLSession session) {
////                            if ("tj.babybus.com".equals(hostname)) {
////                                return true;
////                            }
////                            return false;
////                        }
////                    })
//                    .setCookie(true)
//                    .setCacheMode(CacheMode.NO_CACHE)//默认不使用缓存
//                    .setCacheMaxSize(10 * 1024 * 1024)//缓存配置相关
//                    .setCacheTime(30 * 24 * 60 * 60)// 缓存时间，默认-1表示永久缓存
//                    .setCacheVersion(com.sinyee.babybus.core.util.AppUtil.getVersionCode(this))// 缓存版本，可以自行设置
                    .setBaseUrl(BuildConfig.BASE_URL)//base url 设置
                    .setLog(isShowLog);//设置log,打印请求日志和返回日志
            // 捕获rx不处理的异常
            RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ModuleBaseDiffHelper.getInstance().registerModuleBaseDiff(new ModuleBaseDiffImpl()); // 初始化IModuleBaseDiff，加载loadview

        CommonState.Config.initGlobalConfig();//全局状态配置loadsir加载框架
    }

    private void initAudio() {

    }


    private static void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化bugly相关操作
     */
    private void initBugly() {

    }

    /**
     * 初始化第三方sdk，放在新线程中执行。避免耗时
     */
    private void initThirdSdk() {
//        //创建一个下游  观察者Observer
//        Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer value) {
//                Log.w(TAG, "" + value);
//                Log.d(TAG, "observer thread is : " + Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//            }
//
//            @Override
//            public void onComplete() {
//            }
//        };
//
//        //创建一个上游  被观察者 Observable：
//        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
//                emitter.onNext(1);
//            }
//        });
//
//        //建立连接
//        observable.subscribeOn(Schedulers.newThread())
//                .subscribe(observer);


    }

//    private void initAdjustLib() {
//        String appToken = "m0g7vytmsgsg";
//        String environment = BuildConfig.LOG_DEBUG ? AdjustConfig.ENVIRONMENT_SANDBOX : AdjustConfig.ENVIRONMENT_PRODUCTION;
//        AdjustConfig config = new AdjustConfig(this, appToken, environment);
//        config.setLogLevel(BuildConfig.LOG_DEBUG ? LogLevel.VERBOSE : LogLevel.SUPRESS);
//        Adjust.onCreate(config);
//    }

    /**
     * 新版分析
     */
    private void initSharjah() {

    }

    private void initCastScreen() {

    }

    /**
     * 初始化广告投放效果跟踪sdk
     */
    private void initFlyerLib() {
    }


    /**
     * 分享的各个平台的配置
     */
    private void initShare() {

    }

    /**
     * 初始化自有统计
     */
    private void initAiolos() {

    }

    // 初始化全局配置SDK：包含灰度，更新，用户好评引导
    private void initConfigFactory() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

}
