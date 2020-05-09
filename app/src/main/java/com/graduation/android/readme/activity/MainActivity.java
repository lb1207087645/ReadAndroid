package com.graduation.android.readme.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.android.readme.R;

import com.graduation.android.readme.adapter.MainAdapter;
import com.graduation.android.readme.base.mvp.IPresenter;
import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.base.utils.ToastUtils;
import com.graduation.android.readme.basemodule.BaseActivity;
import com.graduation.android.readme.home.HomeFragment;
import com.graduation.android.readme.mine.MineFragment2;
import com.graduation.android.readme.news.NewsFragment;
import com.graduation.android.readme.web.WebViewFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;


/**
 * 首页
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.main_bottom_tab_layout)
    TabLayout mainTabLayout;


    @BindView(R.id.main_viewpager)
    ViewPager mainViewPager;


    private Disposable rxPermissionsDisposable;//权限dispose，防止内存泄漏

    /**
     * tab 页String
     */
    private String[] tabStrArray = {"头条", "新闻", "商城", "我的"};

    private MainAdapter vpAdapter;


    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
      TextView  tv_title = (TextView) getToolbarTitleView();
        tv_title.setText("haiai");
    }

    @Override
    public void loadData() {
        initTab();
        //  requestOne();
        requestMulti(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
    }


    /**
     * 初始化tab页
     */
    private void initTab() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new NewsFragment());
        fragments.add(new WebViewFragment());//商城
        fragments.add(new MineFragment2());//我的
        vpAdapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mainViewPager.setAdapter(vpAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainViewPager.setOffscreenPageLimit(vpAdapter.getCount());
        mainViewPager.setCurrentItem(0);


//初始化每个tab页
        for (int i = 0; i < vpAdapter.getCount(); i++) {

            String tabStr = tabStrArray[i];


            // 获得每一个tab
            TabLayout.Tab tab = mainTabLayout.getTabAt(i);
            if (tab != null) {
                // 给每一个tab设置view
                tab.setCustomView(R.layout.main_item_activity_main_tab);
            }
            if (i == 0) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    // 设置第一个tab的TextView是被选择的样式
                    tabView.findViewById(R.id.main_tab_item).setSelected(true);
                }
            }
            //设置tabLayout点击事件
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(mTabOnClickListener);
            }
            View tabView = tab.getCustomView();
            ImageView imageView;
            TextView textView;
            View view;
            if (tabView != null) {
                imageView = tabView.findViewById(R.id.main_tab_icon);
                textView = tabView.findViewById(R.id.main_tab_title);
                textView.setText(tabStr);

                switch (i) {
                    case 0:
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_recommend_icon_selector));
                        break;
                    case 1:
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_news_icon_selector));
                        break;
                    case 2:
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_weather_icon_selector));
                        break;
                    case 3:
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_mine_icon_selector));
                        break;
                    default:
                        break;
                }
            }

        }
        bindOnTabSelectedListener();
    }


    /**
     * tab页选中事件
     */
    private void bindOnTabSelectedListener() {
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    tabView.findViewById(R.id.main_tab_item).setSelected(true);
                }
                mainViewPager.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    tabView.findViewById(R.id.main_tab_item).setSelected(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
     * TabLayout点击事件处理
     */
    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TabLayout.Tab tab = mainTabLayout.getTabAt(position);
//            if (tab != null) {
//                L.d(TAG, "position==" + position + "tab.isSelected()" + tab.isSelected());
//                //点击好看
//                if (position == 0 && tab.isSelected()) {
//                    EventBus.getDefault().post(new SwitchEvent());
//                }
//            }
        }
    };

    @Override
    public boolean isActive() {
        return false;
    }


    @Override
    protected boolean isUseToolbar() {
        return true;
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


    /**
     * 分别请求多个权限
     */
    @SuppressLint("CheckResult")
    @TargetApi(Build.VERSION_CODES.M)
    private void requestMulti(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(permissions)
                .subscribe(permission -> { // will emit 2 Permission objects

                    //两个都请求完后会调用
                    if (permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                        if (permission.granted) {
                            L.d("granted", "READ_PHONE_STATE允许");
                        } else {
                            L.d("granted", "READ_PHONE_STATE拒绝");
                        }
                    }

                    if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (permission.granted) {
                            L.d("granted", "WRITE_EXTERNAL_STORAGE允许");
                        } else {
                            L.d("granted", "WRITE_EXTERNAL_STORAGE拒绝");
                        }
                    }


                });

    }


    /**
     * 单个权限申请,访问请求摄像机权限
     */
    private void requestOne() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissionsDisposable = rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) { // 用户允许
                ToastUtils.showToast(this, "用户允许");
                // I can control the camera now
            } else {//用户拒绝,如果要强制申请权限，可以在这里执行提示用户去设置的弹窗
                // Oups permission denied
                ToastUtils.showToast(this, "用户拒绝");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxPermissionsDisposable != null && !rxPermissionsDisposable.isDisposed()) {
            rxPermissionsDisposable.dispose();
        }
    }
}
