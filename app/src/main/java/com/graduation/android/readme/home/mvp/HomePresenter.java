package com.graduation.android.readme.home.mvp;

import android.app.Activity;

import com.graduation.android.readme.base.mvp.BasePresenter;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.bean.Translation1;
import com.graduation.android.readme.http.BaseObserver;
import com.graduation.android.readme.http.BaseObserverGet;
import com.graduation.android.readme.http.BaseResponse;
import com.graduation.android.readme.model.HomeModel;
import com.graduation.android.readme.model.Translation3;


/**
 * home的p层
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {


//    private Activity mActivity;


    @Override
    public void getCallPost() {
        subscribe(model.getCallPost("I hate you"), new BaseObserver<Translation1>() {
            @Override
            public void onError(ErrorEntity err) {
                getView().showErr(err);
                getView().loadCallGet(null);
            }

            @Override
            public void onAfter() {

            }

            @Override
            public void onData(Translation1 translation1) {
                getView().loadCallPost(translation1.getTranslateResult().get(0).get(0).getTgt());
            }


        });

    }

    private HomeModel model;
    private HomePresenter mHomePresenter;

    public HomePresenter(Activity activity) {
//        mActivity = activity;
        mHomePresenter = this;

        model = new HomeModel();
    }


    /**
     * 简单文本回调,get 请求示例
     */
    @Override
    public void getCallGet() {
        subscribe(model.getCallGet(), new BaseObserverGet<Translation3.content>() {
            @Override
            public void onError(ErrorEntity err) {
                getView().showErr(err);
                getView().loadCallGet(null);
            }

            @Override
            public void onAfter() {

            }


            @Override
            public void onData(BaseResponse<Translation3.content> baseResponse) {
                getView().loadCallGet(baseResponse.data.out);
            }
        });

    }


}
