package com.graduation.android.readme.base.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.graduation.android.readme.base.utils.ToastUtils;
import com.graduation.android.readme.base.widget.state.CommonState;

import butterknife.Unbinder;


/**
 * mvp版的BaseFragment 基类
 *
 * @param <P>
 * @param <V>
 */
public abstract class BaseMvpFragment<P extends IPresenter<V>, V extends BaseView> extends Fragment implements BaseView, IFetchData {

    protected P mPresenter;//p层


    protected Activity mActivity;//Fragment对应的Activity

    /**
     * 是否对用户可见，懒加载部分
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    protected View rootView;

    protected Unbinder unbinder;

    protected CommonState mStateView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            initialize(inflater, container, savedInstanceState);
        } else {
            // bind 与 unbind 需要配对，在fragment view被回收后unbind，
            // 重建时，需要重新bind。
            // modified by chenqq at 2018-12-13
            //   unbinder = ButterKnife.bind(this, rootView);
        }

        // 由于使用了LoadSir，rootView外部自动添加了LoadLayout进行包装，因此这里处理的rootView应该改成.
        if (setLoadingLayoutId() <= 0) {
            rootView = mStateView.getLoadLayout();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    protected void initialize(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);//p层与生命周期绑定
            mPresenter.attachView((V) this);//presenter与view连接
        }
        initView(rootView, savedInstanceState);
        bindEventListener();
        // 该处代码用于配合CommonState处理使用，但是顺序必须要有保证，
        // 必须在initView方法之后，在loadData之前。

        if (setLoadingLayoutId() > 0) {
            View view = rootView.findViewById(setLoadingLayoutId());
            if (view != null) {//局部刷新的情况
                mStateView = new CommonState(view);
            } else {
                mStateView = new CommonState(rootView);
            }

        } else {//全局刷新
            mStateView = new CommonState(rootView);

        }
//懒加载部分
        if (isLazyLoad()) {
            mIsPrepare = true;
            onLazyLoad();
        } else {
            loadData();
        }
    }

    /**
     * 返回需要刷新布局layout id，当fragment需要局部刷新的时候用到,默认全局刷新
     *
     * @return layout id
     */
    protected int setLoadingLayoutId() {
        return 0;
    }

    public void setViewOnClickListener(View.OnClickListener clickListener, View... views) {
        for (View view : views) {
            view.setOnClickListener(clickListener);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }


    /**
     * 事件监听的处理
     */
    protected void bindEventListener() {

    }


    /**
     * 子类去初始化Presenter
     *
     * @return
     */
    protected abstract P initPresenter();


    /**
     * 获取布局文件的ID
     *
     * @return
     */
    protected abstract int getLayoutId();


    /**
     * 加载数据,重新加载和首次加载用
     */
    @Override
    public abstract void loadData();

    /**
     * 是否懒加载
     * 默认懒加载，可以复写此方法返回true。并在{@link #onVisible()}, {@link #onInvisible()}处理用户是否可见页面时的逻辑
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 懒加载部分
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    private void onLazyLoad() {
        if (mIsVisible & mIsPrepare) {
            mIsPrepare = false;
            loadData();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }


    /**
     * 显示全局通用无数据view
     *
     * @param imageId
     */
    @Override
    public void showEmptyView(int imageId) {
        mStateView.showEmptyView(this, imageId);
    }

    /**
     * 显示错误view
     */
    @Override
    public void showErrorView() {
        mStateView.showErrorView(this);
    }

    /**
     * 显示内容view
     */
    @Override
    public void showContentView() {
        mStateView.showContentView();
    }

    /**
     * 加载中
     */
    @Override
    public void showLoadingView() {
        mStateView.showLoadingView();
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onInvisible() {

    }

    /**
     * 子类的初始化操作
     *
     * @param view
     */
    protected abstract void initView(View view, Bundle savedInstanceState);


    @Override
    public void onDestroyView() {
        if (mPresenter != null) {//防止内存泄漏
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    /**
     * toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastUtils.showToast(mActivity, msg, Toast.LENGTH_SHORT);
    }
}
