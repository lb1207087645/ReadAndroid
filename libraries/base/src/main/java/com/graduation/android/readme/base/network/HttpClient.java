package com.graduation.android.readme.base.network;


import com.graduation.android.readme.base.core.CacheClient;
import com.graduation.android.readme.base.CommonApplication;
import com.graduation.android.readme.base.core.NoCacheInterceptor;
import com.graduation.android.readme.base.model.CacheMode;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * desc: 单例httpclient，设置一些超时设置和缓存
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
class HttpClient {

    private static final int DEFAULT_TIME_OUT = 10;//超时时间
    private static final int DEFAULT_READ_TIME_OUT = 10;//读取超时时间
    private static final int DEFAULT_WRITE_TIME_OUT = 10;//写入超时时间
    private static HttpClient instance;
    //全局的http设置
    private OkHttpClient.Builder builder;
    private Cache cache;

    public HttpClient() {
        builder = new OkHttpClient.Builder();
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
//        builder.addInterceptor(new NetworkInterceptor());//添加网络拦截器
       // builder.addInterceptor(RetrofitConfig.sQueryParameterInterceptor);//添加公共参数
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {

        CacheMode cacheMode = CacheClient.getInstance().getCacheMode();
        switch (cacheMode) {
            case NO_CACHE:
                final NoCacheInterceptor noCacheInterceptor = new NoCacheInterceptor();
                addNetworkInterceptor(noCacheInterceptor);
                break;
            case DEFAULT:
                /**
                 * 使用Okhttp的缓存
                 */
                if (this.cache == null) {
                    File cacheDirectory = RetrofitServiceManager.getInstance().getCacheDirectory();
                    if (cacheDirectory == null) {
                        cacheDirectory = new File(CommonApplication.getContext().getCacheDir(), "rxHttpCacheData");
                    }
                    if (cacheDirectory.isDirectory() && !cacheDirectory.exists()) {
                        cacheDirectory.mkdirs();
                    }
                    this.cache = new Cache(cacheDirectory, Math.max(5 * 1024 * 1024, RetrofitServiceManager.getInstance().getCacheMaxSize()));
                }
                final CacheInterceptor cacheInterceptor = new CacheInterceptor(CacheClient.getInstance().getCacheTime());
                addNetworkInterceptor(cacheInterceptor).cache(cache);
                break;
            case FIRSTREMOTE:
            case FIRSTCACHE:
            case ONLYREMOTE:
            case ONLYCACHE:
            case CACHEANDREMOTE:
            case CACHEANDREMOTEDISTINCT:
            case SPECIALCACHE:
                addNetworkInterceptor(new NoCacheInterceptor());//添加获取缓存的拦截器
                break;
            default:
                break;

        }
        return builder.build();
    }


    /**
     * 添加拦截器
     *
     * @param interceptor
     * @return
     */
    public HttpClient addInterceptor(Interceptor interceptor) {
        for (Interceptor i : builder.interceptors()) {
            if (i.getClass().getSimpleName().equals(interceptor.getClass().getSimpleName())) {
                return this;
            }
        }
        builder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 获取头部的拦截器
     *
     * @param interceptor
     * @return
     */
    public HttpClient addNetworkInterceptor(Interceptor interceptor) {
        for (Interceptor i : builder.networkInterceptors()) {
            if (i.getClass().getSimpleName().equals(interceptor.getClass().getSimpleName())) {
                return this;
            }
        }
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public HttpClient cache(Cache cache) {
        builder.cache(cache);
        return this;
    }

    public HttpClient cookie(CookieJar cookieJar) {
        builder.cookieJar(cookieJar);
        return this;
    }

    public HttpClient readTimeout(long timeout, TimeUnit unit) {
        builder.readTimeout(timeout, unit);
        return this;
    }

    public HttpClient writeTimeout(long timeout, TimeUnit unit) {
        builder.writeTimeout(timeout, unit);
        return this;
    }

    public HttpClient connectTimeout(long timeout, TimeUnit unit) {
        builder.connectTimeout(timeout, unit);
        return this;
    }

    public HttpClient sslSocketFactory(SSLSocketFactory sSLSocketFactory, X509TrustManager trustManager) {
        builder.sslSocketFactory(sSLSocketFactory, trustManager);
        return this;
    }

    public HttpClient hostnameVerifier(HostnameVerifier hostnameVerifier) {
        builder.hostnameVerifier(hostnameVerifier);
        return this;
    }
}
