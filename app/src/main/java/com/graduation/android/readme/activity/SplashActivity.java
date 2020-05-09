package com.graduation.android.readme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.graduation.android.readme.R;

import com.graduation.android.readme.base.mvp.IPresenter;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.basemodule.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;



/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {


    @BindView(R.id.gifImageView)
    TextView gifImageView;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.tv_skip)
    TextView tvSkip;//跳过文字
    @BindView(R.id.fl_ad)
    FrameLayout flAd;


    CompositeDisposable mCompositeDisposable = new CompositeDisposable();//RxJava 用来切断所有订阅事件的


    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {


        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {//倒计时4秒
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                tvSkip.setText("跳过 4");//开始跳过4
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                tvSkip.setText("跳过 " + (integer + 1));//跳过倒计时
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMain();
            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();//切断所有联系，防止内存泄漏
        }
    }

    /**
     * 倒计时完成后跳转到主页
     */
    private void toMain() {
        if (mCompositeDisposable != null) {//切断联系，防止内存泄漏
            mCompositeDisposable.dispose();
        }
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.fl_ad)
    public void onViewClicked() {//点击跳过广告后，跳转到主页
        toMain();
    }

    /**
     * RxJava倒计时操作
     *
     * @param time
     * @return
     */
    public Observable<Integer> countDown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);//设置总共取的次数
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
    public void showErr(ErrorEntity err) {

    }
}
