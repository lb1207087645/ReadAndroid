package com.graduation.android.readme.news.mvp;

import android.app.Activity;

import com.graduation.android.readme.base.mvp.BasePresenter;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.bean.NewsDetail;
import com.graduation.android.readme.http.BaseObserver;
import com.graduation.android.readme.model.FreshBean;
import com.graduation.android.readme.model.FreshNewsBean;
import com.graduation.android.readme.model.NewsModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 新闻的p层
 */
public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {


    private Activity mActivity;


    private NewsModel model;
    private NewsPresenter mHomePresenterTest;

    public NewsPresenter(Activity activity) {
        mActivity = activity;
        mHomePresenterTest = this;
        model = new NewsModel();
    }


    @Override
    public void getFreshNews(int page) {
        getView().showLoadingView();
        subscribe(model.getFreshNews(page), new BaseObserver<FreshNewsBean>() {
            @Override
            public void onError(ErrorEntity err) {
                getView().showErr(err);

                getView().loadFreshNews(null);
            }

            @Override
            public void onAfter() {

            }

            @Override
            public void onData(FreshNewsBean baseResponse) {
                getView().showContentView();
                getView().loadFreshNews(changeServerData(baseResponse));//转化数据
            }
        });
    }

    /**
     * 新闻数据回调
     */
    @Override
    public void getNewsData(String id, String action, int pullNum) {
        subscribe(model.getNewsDetail(id, action, pullNum), new BaseObserver<List<NewsDetail>>() {
            @Override
            public void onError(ErrorEntity err) {
                getView().showErr(err);
            }

            @Override
            public void onAfter() {

            }

            @Override
            public void onData(List<NewsDetail> newsDetails) {
                getView().loadNewsData(newsDetails);
            }
        });
    }

    private List<FreshBean.PostsBean> changeServerData(FreshNewsBean freshNewsBean) {
        List<FreshBean.PostsBean> studyBeanList = new ArrayList<>();
        for (int i = 0; i < freshNewsBean.getPosts().size(); i++) {
            FreshNewsBean.PostsBean postsBean = freshNewsBean.getPosts().get(i);
            FreshBean.PostsBean postBean = null;
            if (postsBean != null) {
                postBean = new FreshBean.PostsBean();
                postBean.setItemType(NewsDetail.ItemBean.TYPE_DOC_TITLEIMG);
                postBean.setTitle(postsBean.getTitle());
//            studyBean.setUrl(postsBean.getUrl());
                // studyBean.getAuthor().setName(postsBean.getAuthor().getName());
                postBean.setComment_count(postsBean.getComment_count());

                FreshNewsBean.PostsBean.CustomFieldsBean customFieldsBean = postsBean.getCustom_fields();
                if (customFieldsBean != null) {
                    List<String> list = customFieldsBean.getThumb_c();
                    if (list != null && list.size() > 0) {
                        postBean.setImgUrl(list.get(0));
                    }
                }

            }

            studyBeanList.add(postBean);
        }
        return studyBeanList;
    }


}
