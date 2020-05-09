package com.graduation.android.readme.model;

import android.support.v4.media.session.PlaybackStateCompat;

import com.graduation.android.readme.base.network.RetrofitServiceManager;
import com.graduation.android.readme.bean.NewsDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class NewsModel {

    public static final String TYPE_FRESH = "get_recent_posts";
    private ModelService modelService;

    public NewsModel() {
        modelService = RetrofitServiceManager.getInstance().create(ModelService.class);
    }


    /**
     * 获取新闻详情
     *
     * @param id
     * @param action
     * @param pullNum
     * @return
     */
    public Observable<List<NewsDetail>> getNewsDetail(String id, @PlaybackStateCompat.Actions String action, int pullNum) {
        return modelService.getNewsDetail(id, action, pullNum);
    }


    public Observable<FreshNewsBean> getFreshNews(int page) {
        return modelService.getFreshNews("http://i.jandan.net/", TYPE_FRESH,
                "url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields",
                page, "thumb_c,views", "1");
    }


    /**
     * 请求接口
     */
    interface ModelService {


        @GET("ClientNews")
        Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
                                                   @Query("action") String action,
                                                   @Query("pullNum") int pullNum);


        /**
         * 煎蛋新闻
         *
         * @param oxwlxojflwblxbsapi
         * @param include
         * @param page
         * @param custom_fields
         * @param dev
         * @return
         */
        @GET
        Observable<FreshNewsBean> getFreshNews(@Url String url, @Query("oxwlxojflwblxbsapi") String oxwlxojflwblxbsapi,
                                               @Query("include") String include,
                                               @Query("page") int page,
                                               @Query("custom_fields") String custom_fields,
                                               @Query("dev") String dev
        );


    }

}
