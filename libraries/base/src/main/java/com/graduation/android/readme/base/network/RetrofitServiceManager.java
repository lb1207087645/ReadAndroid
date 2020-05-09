package com.graduation.android.readme.base.network;

import android.content.SharedPreferences;


import com.graduation.android.readme.base.core.CacheClient;
import com.graduation.android.readme.base.core.IDiskConverter;
import com.graduation.android.readme.base.core.SetCookieCache;
import com.graduation.android.readme.base.core.SharedPrefsCookiePersistor;
import com.graduation.android.readme.base.model.CacheMode;
import com.graduation.android.readme.base.utils.SSLUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * desc: 网络操作管理单例
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
@SuppressWarnings("unused")
public class RetrofitServiceManager {

//    private static long CACHE_10M = 1024 * 1024 * 10L;

    private RetrofitServiceManager() {

    }

    private static class SingletonHolder {//单例模式
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return RetrofitServiceManager
     */
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 全局的 retrofit
     *
     * @return Retrofit
     */
    private Retrofit getGlobalRetrofit() {
        return getGlobalRetrofit(null);
    }

    /**
     * 全局的 retrofit
     *
     * @return Retrofit
     */
    private Retrofit getGlobalRetrofit(OkHttpClient client) {
        return RetrofitClient.getInstance().getRetrofit(client);
    }

    /**
     * 设置baseUrl
     *
     * @param baseUrl baseUrl
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setBaseUrl(String baseUrl) {
        RetrofitClient.getInstance().setBaseUrl(baseUrl);
        return this;
    }

    public RetrofitServiceManager addCallAdapterFactory(CallAdapter.Factory factory) {
        RetrofitClient.getInstance().addCallAdapterFactory(factory);
        return this;
    }

    public RetrofitServiceManager addConverterFactory(Converter.Factory factory) {
        RetrofitClient.getInstance().addConverterFactory(factory);
        return this;
    }

    /**
     * 是否开启请求日志
     *
     * @param isShowLog 是否开启log
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setLog(boolean isShowLog) {
        if (isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            LargeHttpLoggingInterceptor loggingInterceptor = new LargeHttpLoggingInterceptor();
            addNetworkInterceptor(loggingInterceptor);
        }
        return this;
    }

    /**
     * 添加统一的请求头
     *
     * @param headerMaps 请求头map
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setHeaders(Map<String, String> headerMaps) {
        // 添加公共参数拦截器
        CommonHeaderInterceptor commonInterceptor = new CommonHeaderInterceptor.Builder().addHeaderMapParams(headerMaps).build();
        addNetworkInterceptor(commonInterceptor);
        return this;
    }

//    public RetrofitServiceManager openGzip(){
//        HttpClient.getInstance().addInterceptor(new GzipInterceptor());
//        return this;
//    }

    /**
     * 持久化保存cookie保存到sp文件中
     *
     * @param saveCookie 是否保存cookie
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setCookie(boolean saveCookie) {
        if (saveCookie) {
//            HttpClient.getInstance().addInterceptor(new AddCookiesInterceptor()).addInterceptor(new ReceivedCookiesInterceptor());
            HttpClient.getInstance().cookie(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor()));
        }
        return this;
    }

    /**
     * 持久化保存cookie保存到sp文件中
     *
     * @param saveCookie 是否保存cookie
     * @param sp         存放的sp
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setCookie(boolean saveCookie, SharedPreferences sp) {
        if (saveCookie) {
            HttpClient.getInstance().cookie(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(sp)));
        }
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * @param second 超时时间
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setReadTimeout(long second) {
        HttpClient.getInstance().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param second 超时时间
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setWriteTimeout(long second) {
        HttpClient.getInstance().writeTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param second 超时时间
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setConnectTimeout(long second) {
        HttpClient.getInstance().connectTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 信任所有证书,不安全有风险
     *
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setSslSocketFactory() {
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory();
        HttpClient.getInstance().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 使用预埋证书，校验服务端证书（自签名证书）
     *
     * @param certificates 证书流
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setSslSocketFactory(InputStream... certificates) {
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory(certificates);
        HttpClient.getInstance().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
     *
     * @param bksFile      keystore data
     * @param password     密码
     * @param certificates 证书流
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager setSslSocketFactory(InputStream bksFile, String password, InputStream... certificates) {
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory(bksFile, password, certificates);
        HttpClient.getInstance().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * 添加域名验证
     *
     * @param hostnameVerifier 域名验证
     * @return RetrofitServiceManager
     */
    public RetrofitServiceManager hostnameVerifier(HostnameVerifier hostnameVerifier) {
        HttpClient.getInstance().hostnameVerifier(hostnameVerifier);
        return this;
    }

    /**
     * 添加自定义Interceptor 拦截器
     *
     * @param interceptor interceptor
     */
    public RetrofitServiceManager addInterceptor(Interceptor interceptor) {
        HttpClient.getInstance().addInterceptor(interceptor);
        return this;
    }

    /**
     * 添加自定义NetworkInterceptor，拦截器
     *
     * @param interceptor interceptor
     */
    public RetrofitServiceManager addNetworkInterceptor(Interceptor interceptor) {
        HttpClient.getInstance().addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>     接口类型
     * @return 接口
     */
    public <T> T create(Class<T> service) {
        return create(service, null);
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>     接口类型
     * @return 接口
     */
    public <T> T create(Class<T> service, OkHttpClient client) {
        validateServiceInterface(service);
        // addInterceptor(new NetworkInterceptor());
        addInterceptor(RetrofitConfig.headerInterceptor);
//        addInterceptor(RetrofitConfig.sQueryParameterInterceptor);
        return getGlobalRetrofit(client).create(service);
    }

    /**
     * 校验接口合法性
     */
    private static <T> void validateServiceInterface(Class<T> service) {
        if (service == null) {
            throw new IllegalArgumentException("API class can not be null.");
        }
        if (!service.isInterface()) {//是否是接口类型
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }

    /**
     * 全局的缓存模式
     */
    public RetrofitServiceManager setCacheMode(CacheMode cacheMode) {
        CacheClient.getInstance().setCacheMode(cacheMode);
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public CacheMode getCacheMode() {
        return CacheClient.getInstance().getCacheMode();
    }

    /**
     * 全局的缓存过期时间
     */
    public RetrofitServiceManager setCacheTime(long cacheTime) {
        CacheClient.getInstance().setCacheTime(cacheTime);
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public long getCacheTime() {
        return CacheClient.getInstance().getCacheTime();
    }

    /**
     * 全局的缓存大小,默认50M
     */
    public RetrofitServiceManager setCacheMaxSize(long maxSize) {
        CacheClient.getInstance().setCacheMaxSize(maxSize);
        return this;
    }

    /**
     * 获取全局的缓存大小
     */
    public long getCacheMaxSize() {
        return CacheClient.getInstance().getCacheMaxSize();
    }

    /**
     * 全局设置缓存的版本，默认为1，缓存的版本号
     */
    public RetrofitServiceManager setCacheVersion(int cacheVersion) {
        CacheClient.getInstance().setCacheVersion(cacheVersion);
        return this;
    }

    /**
     * 全局设置缓存的路径，默认是应用包下面的缓存
     */
    public RetrofitServiceManager setCacheDirectory(File directory) {
        CacheClient.getInstance().setCacheDirectory(directory);
        return this;
    }

    /**
     * 获取缓存的路劲
     */
    public File getCacheDirectory() {
        return CacheClient.getInstance().getCacheDirectory();
    }

    /**
     * 全局设置缓存的转换器
     */
    public RetrofitServiceManager setCacheDiskConverter(IDiskConverter converter) {
        CacheClient.getInstance().setCacheDiskConverter(converter);
        return this;
    }
}