package com.graduation.android.readme.base.network;


import com.graduation.android.readme.base.utils.L;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: 请求头里添加公共参数拦截器
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
@SuppressWarnings("unused")
public class CommonHeaderInterceptor implements Interceptor {

    private static final String TAG = CommonHeaderInterceptor.class.getSimpleName();
    private Map<String, String> mHeaderParamsMap = new HashMap<>();

    public CommonHeaderInterceptor() {
    }

    public CommonHeaderInterceptor(Map<String, String> headerMaps) {
        this.mHeaderParamsMap = headerMaps;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        L.i(TAG, "===CommonHeaderInterceptor intercept===");
        Request oldRequest = chain.request();
        // 新的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());
        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
                requestBuilder.header(params.getKey(), params.getValue());
            }
        }
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    public static class Builder {
        CommonHeaderInterceptor mHttpCommonInterceptor;

        public Builder() {
            mHttpCommonInterceptor = new CommonHeaderInterceptor();
        }

        public Builder addHeaderParams(String key, String value) {
            mHttpCommonInterceptor.mHeaderParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParams(String key, int value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, float value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, long value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, double value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderMapParams(Map<String, String> headerMaps) {
            if (headerMaps.size() > 0) {
                mHttpCommonInterceptor.mHeaderParamsMap.putAll(headerMaps);
            }
            return this;
        }

        public CommonHeaderInterceptor build() {
            return mHttpCommonInterceptor;
        }
    }
}