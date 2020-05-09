package com.graduation.android.readme.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graduation.android.readme.adapter.NewsAdapter;
import com.graduation.android.readme.bean.NewsDetail;
import com.graduation.android.readme.R;
import com.graduation.android.readme.base.mvp.BaseFragment;

import com.graduation.android.readme.model.FreshBean;
import com.graduation.android.readme.news.mvp.NewsContract;
import com.graduation.android.readme.news.mvp.NewsPresenter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;


/**
 * 新闻的NewsFragment
 */
public class NewsFragment extends BaseFragment<NewsContract.Presenter, NewsContract.View> implements NewsContract.View {


    ImageView iv_image;//图片显示

    RecyclerView mRecyclerView;

    private static final String TAG = "NewsFragment";


    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected NewsContract.Presenter initPresenter() {
        return new NewsPresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_news;
    }

    @Override
    public void loadData() {
        mPresenter.getFreshNews(0);
     //   mPresenter.getNewsData("SYLB10,SYDT10", "down", 2);
    }


    @Override
    protected void bindEventListener() {
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {//简单下拉刷新
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                loadData();
            }
        });
    }


    @Override
    public void loadFreshNews(List<FreshBean.PostsBean> postsBean) {
        swipyRefreshLayout.setRefreshing(false);//需要加入setRefreshing false ，不然不能用
        NewsAdapter newsAdapter = new NewsAdapter(postsBean);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void loadNewsData(List<NewsDetail> itemBeanList) {


    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        iv_image = view.findViewById(R.id.iv_image);
        swipyRefreshLayout = view.findViewById(R.id.refresh_layout);
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showTip(String message) {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void hideLoadingDialog() {

    }
}
