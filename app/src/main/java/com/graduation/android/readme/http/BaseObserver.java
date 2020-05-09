package com.graduation.android.readme.http;

import com.graduation.android.readme.base.constant.ErrorConstant;
import com.graduation.android.readme.base.network.BaseSimpleObserver;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.base.network.IErrorHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.CompositeException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;


/**
 * BaseObserver用法
 *
 * @param <T>
 */
public abstract class BaseObserver<T>
        extends BaseSimpleObserver<T>
        implements IErrorHandler {

    public class ExtendErrorConstant extends ErrorConstant {
        public static final String ERROR_MSG_TIMEOUT_EXCEPTION = "网络不给力，请检查网络设置";
    }

    @Override
    public void onComplete() {
        onAfter();
    }

    /**
     * 回调onCompleted时，2个api等效，将完成请求的逻辑写在任一api中都可
     * 回调onError，会先调用onError，然后onAfter
     */
    public abstract void onAfter();

    /**
     * 网络请求数据成功回调
     * <p>
     * //     * @param baseResponse
     */
//    public abstract void onData(BaseResponse<T> baseResponse);

    public abstract void onData(T t);

    /**
     * 最后调用地方
     * <p>
     * //     * @param baseResponse
     */
//    @Override
//    public void onNext(@NonNull BaseResponse<T> baseResponse) {
//        //__Cache__ 缓存生效模式下发的result code
//        //loadremote 情况下 ResultCode:__Cache__ 代表使用本地缓存
////        if (baseResponse.isSuccess() || "__Cache__".equals(baseResponse.getCode())) {
////            onData(baseResponse);
////        } else {
////            //业务错误
////            // TODO: 2017/8/18
////            onError(new ErrorEntity(baseResponse.getCode(), baseResponse.getMsg()));
////        }
//        onData(baseResponse);
//
//    }

    @Override
    public void onNext(T t) {
        onData(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        ErrorEntity errorEntity;
        if (e instanceof SocketTimeoutException) {
            errorEntity = new ErrorEntity(ErrorConstant.ERROR_TYPE_NET_EXCEPTION, ExtendErrorConstant.ERROR_MSG_TIMEOUT_EXCEPTION);
        } else if (e instanceof NoRouteToHostException || e instanceof ConnectException || e instanceof UnknownHostException) {
            errorEntity = new ErrorEntity(ErrorConstant.ERROR_TYPE_NET_EXCEPTION, ErrorConstant.ERROR_MSG_NET_EXCEPTION);
        }
        // 会有多个异常一起被捕获到，然后需要解析异常列表，正常取第一个异常就好
        else if (e instanceof CompositeException && ((CompositeException) e).getExceptions().size() > 0) {
            e = ((CompositeException) e).getExceptions().get(0);

            onError(e);
            return;
        } else {
            errorEntity = new ErrorEntity(ErrorConstant.ERROR_TYPE_CODE_EXCEPTION, handleError(e));
        }
        onError(errorEntity);
        onAfter();
    }

    /**
     * 错误处理
     *
     * @param e
     * @return
     */
    private String handleError(Throwable e) {
        String error = null;
        try {
            if (e instanceof HttpException) {
                ResponseBody errorBody = ((HttpException) e).response().errorBody();
                error = errorBody != null ? errorBody.string() : null;
            } else {
                error = e.getMessage();
                // 由于在网络拦截器中，在网络有问题的情况下，会有一定几率出现IOException，并且导致App崩溃
                // 所以，在网络拦截器中，将该异常捕获，然后返回null，会导致Rectrofit/Okhttp内部错误
                // 这里将错误信息具体化。
                // interceptor com.sinyee.babybus.basemodule.usercenter.net.NetworkInterceptor@8e2a25d returned null
                if (null != error) {
                    // 添加一些指定的错误内容过滤
                    boolean isFilterError = error.contains("interceptor")
                            || error.contains("returned null")
                            || error.contains("response data is null")
                            || error.contains("connection abort");
                    if (isFilterError) {
                        error = "网络不给力，请检查网络设置";
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return error;
    }
}
