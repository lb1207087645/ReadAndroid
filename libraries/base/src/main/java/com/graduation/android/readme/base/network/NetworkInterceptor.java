package com.graduation.android.readme.base.network;

import android.text.TextUtils;
import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器,用于获取公用头部，先放着
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("NetworkInterceptor", "intercept");
        Request originalRequest = chain.request();
        IDynamicParam dh = Header.getDynamicParam(originalRequest);
        Request headerRequest;
        if (dh != null) {
            headerRequest = dh.getHeaderStr(originalRequest);
        } else {
            headerRequest = originalRequest;
        }

        Request.Builder builder = headerRequest.newBuilder();

        String header = headerRequest.header("Content-Encoding");
        if (headerRequest.body() != null && (!TextUtils.isEmpty(header) && "gzip".equalsIgnoreCase(header))) {
            builder.header("Content-Encoding", "gzip");
        }
//        Request compressedRequest = headerRequest.newBuilder()
//                .header("Content-Encoding", "gzip")
//                .header("Content-Type", "application/octet-stream")
//                .build();
//        return chain.proceed(compressedRequest);
        builder.header("Content-Type", "application/octet-stream");
        headerRequest = builder.build();
        return chain.proceed(headerRequest);
    }

}
