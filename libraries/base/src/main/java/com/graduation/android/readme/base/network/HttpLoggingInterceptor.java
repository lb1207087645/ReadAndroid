package com.graduation.android.readme.base.network;


import com.graduation.android.readme.base.utils.L;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;

/**
 * desc: 日志拦截器
 * Created by lyt
 * email:lytjackson@gmail.com
 */
public class HttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private volatile Level level = Level.NONE;
    private String tag;
    private boolean isLogEnable = false;

    public enum Level {
        /**
         * 不打印log
         */
        NONE,
        /**
         * 只打印 请求首行 和 响应首行
         */
        BASIC,
        /**
         * 打印请求和响应的所有 Header
         */
        HEADERS,
        /**
         * 所有数据全部打印
         */
        BODY
    }

    public HttpLoggingInterceptor(String tag) {
        this.tag = tag;
    }

    public HttpLoggingInterceptor(String tag, boolean isLogEnable) {
        this.tag = tag;
        this.isLogEnable = isLogEnable;
    }

    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        //请求日志拦截
        logForRequest(request, chain.connection());

        //执行请求，计算请求时间
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            L.i(tag, "<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        //Logc.e(tag, "+++++++++++++++++++++++++++end+++++++++++耗时:" + tookMs + "毫秒");

        //响应日志拦截
        return logForResponse(response, tookMs);
    }

    /**
     * 请求的日志拦截
     *
     * @param request
     * @param connection
     * @throws IOException
     */
    private void logForRequest(Request request, Connection connection) throws IOException {
        L.i(tag, "-------------------------------request-------------------------------");
        boolean logBody = (level == Level.BODY);
        boolean logHeaders = (level == Level.BODY || level == Level.HEADERS);
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        try {
            String requestStartMessage = "--> " + request.method() + ' ' + URLDecoder.decode(request.url().url().toString(), UTF8.name()) + ' ' + protocol;
            L.i(tag, requestStartMessage);//请求参数

            if (logHeaders) {
                Headers headers = request.headers();

                for (int i = 0, count = headers.size(); i < count; i++) {
                  //  L.i(tag, "请求头部");
                    L.i(tag, "\t" + headers.name(i) + ": " + headers.value(i));
                }

                if (logBody && hasRequestBody) {
                    if (isPlaintext(requestBody.contentType())) {
                        bodyToString(request);
                    } else {
                        L.i(tag, "\tbody: maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e(e);
        } finally {
            L.i(tag, "--> END " + request.method());
        }
    }

    /**
     * 返回的日志拦截
     * @param response
     * @param tookMs
     * @return
     */
    private Response logForResponse(Response response, long tookMs) {
        L.i(tag, "-------------------------------response-------------------------------");
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        boolean logBody = (level == Level.BODY);
        boolean logHeaders = (level == Level.BODY || level == Level.HEADERS);

        try {
            L.i(tag, "<-- " + clone.code() + ' ' + clone.message() + ' ' + URLDecoder.decode(clone.request().url().url().toString(), UTF8.name()) + " (" + tookMs + "ms）");
            if (logHeaders) {
                L.i(tag, " ");
                Headers headers = clone.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
//                    L.i(tag, "响应头部");
                    L.i(tag, "\t" + headers.name(i) + ": " + headers.value(i));
                }
                L.i(tag, " ");
                if (logBody && HttpHeaders.hasBody(clone)) {
                    if (isPlaintext(responseBody.contentType())) {
                        String body = responseBody.string();
                        L.i(tag, "\tbody:" + body);
                        responseBody = ResponseBody.create(responseBody.contentType(), body);
                        return response.newBuilder().body(responseBody).build();
                    } else {
                        L.i(tag, "\tbody: maybe [file part] , too large too print , ignored!");
                    }
                }
                L.i(tag, " ");
            }
        } catch (Exception e) {
            e(e);
        } finally {
            L.i(tag, "<-- END HTTP");
        }
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.type() != null && "text".equals(mediaType.type())) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")) {
                return true;
            }
        }
        return false;
    }

    private void bodyToString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = copy.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void e(Throwable t) {
        if (isLogEnable) {
            t.printStackTrace();
        }
    }
}