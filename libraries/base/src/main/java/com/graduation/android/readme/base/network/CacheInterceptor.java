package com.graduation.android.readme.base.network;

import android.text.TextUtils;


import com.graduation.android.readme.base.CommonApplication;
import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.base.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: 缓存拦截器
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
public class CacheInterceptor implements Interceptor {

    private static final String TAG = CacheInterceptor.class.getSimpleName();
    public static final String RETROFIT_HEADER_CACHE_TIME_KEY = "cache-time";
    private long cacheControlValue = 0L;

    public CacheInterceptor(long cacheControlValue) {
        this.cacheControlValue = cacheControlValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isConnected(CommonApplication.getContext())) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isConnected(CommonApplication.getContext())) {
            long maxAge = cacheControlValue;
            //取头部
            String cacheTime = request.header(RETROFIT_HEADER_CACHE_TIME_KEY);
            if (TextUtils.isEmpty(cacheTime)) {
                maxAge = cacheControlValue;
            } else {
                try {
                    maxAge = Integer.valueOf(cacheTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            L.i(TAG, "===CacheInterceptor intercept===" + maxAge);
            return originalResponse.newBuilder().removeHeader("Pragma").header("Cache-Control", "public ,max-age=" + maxAge).build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            return originalResponse.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
        }
    }

}