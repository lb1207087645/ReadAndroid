package com.graduation.android.readme.base.mvp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.graduation.android.readme.base.R;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.base.widget.state.CommonState;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * mvp 版的activity
 */
public abstract class BaseMvpActivity<P extends IPresenter, V extends BaseView> extends AppCompatActivity implements BaseView {


    protected Context mActivity;
    private Unbinder unbinder;//ButterKnife 绑定
    protected P mPresenter;//p 层
    //    protected DialogFactory mDialogFactory;//加载进度条
    protected LayoutInflater mInflater;
    protected Toolbar mToolbar;
    private View leftToolbarView;
    private View centerToolbarView;
    private View rightToolbarView;


    protected CommonState mStateView;//loadSir 用法

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        beforeSetContentView();
        setContentView(R.layout.common_base_view);//基本布局

        mInflater = LayoutInflater.from(this);
        if (isUseToolbar()) {//是否使用默认toolbar
            //初始化使用toolbar
            findViewById(R.id.common_view_toolbar).setVisibility(View.VISIBLE);
            findViewById(R.id.common_view_container).setVisibility(View.VISIBLE);
            mToolbar = findViewById(R.id.common_toolbar);
            if (setCustomToolbarLayout() <= 0) {
                //使用默认的toolbar
                mInflater.inflate(R.layout.common_include_toolbar, mToolbar);
                leftToolbarView = mToolbar.findViewById(R.id.common_toolbar_tv_left);
                centerToolbarView = mToolbar.findViewById(R.id.common_toolbar_tv_title);
                rightToolbarView = mToolbar.findViewById(R.id.common_toolbar_tv_right);
            } else {
                //自定义toolbar
                mInflater.inflate(setCustomToolbarLayout(), mToolbar);
            }
        }

        // 该处代码用于配合CommonState处理使用，但是顺序必须要有保证，
        // 必须在initWidget与CommonState.registerToTarget之前处理。
        // 这里将继承Activity的布局加入到基类布局的预留容器中。
        ViewGroup container;
        if (isUseToolbar()) {
            container = findViewById(R.id.common_container);
        } else {
            //未使用toolbar时候减少一个层级的布局
            container = findViewById(R.id.common_root);
        }

        // 使用rootview保证布局的根属性能够正常渲染，同时不能加入到rootview中，需要手动添加。
        View contentView = mInflater.inflate(getLayoutId(), container, false);
        // 手动将子类布局加入到基类容器中。
        container.addView(contentView);

        unbinder = ButterKnife.bind(this);
        mPresenter = initPresenter();

        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);//p 层与生命周期做关联
            mPresenter.attachView(this);//presenter与view连接
        }
        if (mToolbar != null && setCustomToolbarLayout() <= 0) {//默认toolbar
            initToolbar(savedInstanceState);
        }
        initProgressDialog(savedInstanceState);
        initWidget(savedInstanceState);
        bindEventListener();

        if (!isUseFragment()) {
            // 该处代码用于向Activity注册一个全局通用的状态UI，必须在initWidget之后，在loadData之前调用。
            if (setLoadingLayoutId() > 0) {
                View loadingView = findViewById(setLoadingLayoutId());
                if (loadingView != null) {
                    mStateView = new CommonState(loadingView);
                } else {
                    mStateView = new CommonState(container);
                }
            } else {
                mStateView = new CommonState(container);
            }
        }

        loadData();
    }

    protected boolean isUseFragment() {
        return true;
    }


    //是否使用toolbar
    protected boolean isUseToolbar() {
        return true;
    }

    protected int setCustomToolbarLayout() {
        return 0;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 子类初始化Presenter，返回给父类进行管理
     *
     * @return Presenter
     */
    protected abstract P initPresenter();

    protected void initProgressDialog(Bundle savedInstanceState) {
//        mDialogFactory = new DialogFactory(getSupportFragmentManager(), savedInstanceState);
//        mDialogFactory.restoreDialogListener(this);
    }

    protected void beforeSetContentView() {
    }

    /**
     * 返回布局layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    protected void bindEventListener() {
    }

    /**
     * 返回需要刷新布局layout id，当fragment需要局部刷新的时候用到,默认全局刷新
     *
     * @return layout id
     */
    protected int setLoadingLayoutId() {
        return 0;
    }

    /**
     * 用于子类对布局的view进行初始化等操作
     *
     * @param savedInstanceState
     */
    protected abstract void initWidget(Bundle savedInstanceState);

    protected void initToolbar(Bundle savedInstanceState) {//对toolbar 做一些设置


    }

//    public BaseDialogFragment.BaseDialogListener getDialogListener() {
//        return mDialogFactory.mListenerHolder.getDialogListener();
//    }

    /**
     * 清空DialogListenerHolder中的dialog listener
     */
    public void clearDialogListener() {
//        mDialogFactory.mListenerHolder.setDialogListener(null);
    }

    @Override
    public void showLoadingDialog(String msg) {
//        mDialogFactory.showProgressDialog(msg, true);

    }

    @Override
    public void hideLoadingDialog() {
//        mDialogFactory.dismissProgressDialog();

    }

    @Override
    public void showErr(ErrorEntity err) {
        Toast.makeText(this, err.errMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载数据
     */
    public abstract void loadData();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mDialogFactory.mListenerHolder.saveDialogListenerKey(outState);
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (unbinder != Unbinder.EMPTY) {//解绑，避免内存泄漏
            unbinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * 显示全局通用无数据view
     */
    @Override
    public void showEmptyView(int imageId) {
//        mStateView.showEmptyView(this, imageId);
    }

    /**
     * 显示全局通用错误view
     */
    @Override
    public void showErrorView() {
//        mStateView.showErrorView(this);
    }

    /**
     * 显示全局通用错误view
     */
    @Override
    public void showErrorView(String errorType) {
//        mStateView.showErrorView(this);
    }

    /**
     * 显示全局通用加载view
     */
    @Override
    public void showLoadingView() {
//        mStateView.showLoadingView();
    }

    /**
     * 显示内容view
     */
    @Override
    public void showContentView() {
//        mStateView.showContentView();
    }

    public View getToolbarLeftView() {
        return leftToolbarView;
    }

    public View getToolbarTitleView() {
        return centerToolbarView;
    }

    public View getToolbarRightView() {
        return rightToolbarView;
    }


}
