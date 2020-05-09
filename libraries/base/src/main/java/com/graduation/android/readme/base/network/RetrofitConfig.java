package com.graduation.android.readme.base.network;

import com.graduation.android.readme.base.model.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitConfig {


    /**
     * 公共参数拦截器
     */
    public static final Interceptor sQueryParameterInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request;
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("uid", Constants.uid)//插入键值对参数到 url query 中。
                    .addQueryParameter("devid", Constants.uid)
                    .addQueryParameter("proid", "ifengnews")
                    .addQueryParameter("vt", "5")
                    .addQueryParameter("publishid", "6103")
                    .addQueryParameter("screen", "1080x1920")
                    .addQueryParameter("df", "androidphone")
                    .addQueryParameter("os", "android_22")
                    .addQueryParameter("nw", "wifi")
                    .build();
            request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        }
    };


    /**
     * 增加头部信息,测试用,嗨世界用
     */
    public static final Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request build = chain.request().newBuilder()
                    .addHeader("Accept-Encoding", "identity")
                    .build();
            return chain.proceed(build);
        }
    };

}
