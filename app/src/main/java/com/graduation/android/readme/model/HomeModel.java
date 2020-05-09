package com.graduation.android.readme.model;

import com.graduation.android.readme.base.network.RetrofitServiceManager;
import com.graduation.android.readme.bean.Translation1;
import com.graduation.android.readme.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class HomeModel {


    private ModelService modelService;

    public HomeModel() {
        modelService = RetrofitServiceManager.getInstance().create(ModelService.class);
    }


    public Observable<BaseResponse<Translation3.content>> getCallGet() {
        return modelService.getCallGet();
    }

    public Observable<Translation1> getCallPost(String name) {
        return modelService.getCallPost(name);
    }


    /**
     * 请求接口
     */
    interface ModelService {

        //get测试类
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
        Observable<BaseResponse<Translation3.content>> getCallGet();

        //post测试类
        @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
        @FormUrlEncoded
        Observable<Translation1> getCallPost(@Field("i") String targetSentence);

    }

}
