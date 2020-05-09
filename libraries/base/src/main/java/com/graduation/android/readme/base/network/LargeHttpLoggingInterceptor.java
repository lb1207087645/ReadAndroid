package com.graduation.android.readme.base.network;


import com.graduation.android.readme.base.utils.L;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 日志拦截器
 * 打印大量数据
 */
public class LargeHttpLoggingInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        L.i("LargeHttpLoggingInterceptor", "REQUEST_URL----》" + request.toString());

//        Response response = chain.proceed(request);
//        ResponseBody responseBody = response.body();
//        BufferedSource source = responseBody.source();
//        source.request(Long.MAX_VALUE); // Buffer the entire body.
//        Buffer buffer = source.buffer();
//        Charset UTF8 = Charset.forName("UTF-8");
//
//        L.i("LargeHttpLoggingInterceptor", "REQUEST_JSON----》"+buffer.clone().readString(UTF8));
//                    Object o = buffer.clone().readString(UTF8);
//                    JLog.logi("REQUEST_JSON2", o.toString());

        long t1 = System.nanoTime();//请求发起的时间
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        L.i("LargeHttpLoggingInterceptor",
                String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s",
                        response.request().url(),
                        responseBody.string(),
                        (t2 - t1) / 1e6d,
                        response.headers()
                ));


        return response;
    }
}
