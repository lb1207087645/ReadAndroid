package com.graduation.android.readme.base.network;

import com.graduation.android.readme.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * desc: 实例化Retrofit 单例
 * Created by lyt
 * www.babybus.com
 * email:lytjackson@gmail.com
 */
class RetrofitClient {

    private static RetrofitClient instance;
    private Retrofit.Builder mRetrofitBuilder;
    private List<? extends Converter.Factory> mFactorys;//未知

    private RetrofitClient() {
        mRetrofitBuilder = new Retrofit.Builder();
        mFactorys = new ArrayList<>();
        mRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());// 支持RxJava平台
    }

    protected static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * Retrofit 基地址设置
     *
     * @param baseUrl
     * @return
     */
    RetrofitClient setBaseUrl(String baseUrl) {
        mRetrofitBuilder.baseUrl(baseUrl);
        return this;
    }


    /**
     * 支持平台
     *
     * @param factory
     * @return
     */
    RetrofitClient addCallAdapterFactory(CallAdapter.Factory factory) {
        Utils.checkNotNull(factory, "call adapter factory == null");
        mRetrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    /**
     * 支持平台
     *
     * @param factory
     * @return
     */
    RetrofitClient addConverterFactory(Converter.Factory factory) {
        Utils.checkNotNull(factory, "convert factory == null");
        for (Converter.Factory i : mFactorys) {
            if (i.getClass().getSimpleName().equals(factory.getClass().getSimpleName())) {
                return this;
            }
        }
        mRetrofitBuilder.addConverterFactory(factory);
        return this;
    }


    /**
     * 获取Retrofit 对象
     *
     * @param okClient
     * @return
     */
    Retrofit getRetrofit(OkHttpClient okClient) {
        if (okClient == null) {
            okClient = HttpClient.getInstance().getOkHttpClient();
        }
        return mRetrofitBuilder.client(okClient).build();
    }
}
