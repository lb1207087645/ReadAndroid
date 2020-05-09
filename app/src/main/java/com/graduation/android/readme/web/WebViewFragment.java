package com.graduation.android.readme.web;


import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.graduation.android.readme.R;
import com.graduation.android.readme.base.mvp.BaseMvpFragment;
import com.graduation.android.readme.base.mvp.IPresenter;
import com.graduation.android.readme.base.network.ErrorEntity;


/**
 * 商城web页，参考呼吸卫士的web页
 */
public class WebViewFragment extends BaseMvpFragment {

    String mUrl = "http://wx.huxiweishi.cn/wechatbreath/shop/mallIndex.jsp?patientId=161025103116010003";//呼吸卫士测试数据
    /**
     * WebView object
     */
    private WebView mWebView;

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_view_util;
    }

    @Override
    public void loadData() {

        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setUseWideViewPort(true); //将图片调整到适合webView的大小
        webSettings.setJavaScriptEnabled(true); //支持js
        mWebView.addJavascriptInterface(new JSClient(), "appPayRequest");////添加js回调原生
        mWebView.setDrawingCacheEnabled(false);// 允许进行可视区域的截图
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    dismissDialog();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }
        });

        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          view.addJavascriptInterface(new JSClient(), "appPayRequest");
                                          view.loadUrl(url);
                                          return super.shouldOverrideUrlLoading(view, url);
                                      }

                                      @Override
                                      public void onReceivedError(WebView view, int errorCode, String description,
                                                                  String failingUrl) {
                                          super.onReceivedError(view, errorCode, description, failingUrl);
                                          if (errorCode != 200) {

                                          }
                                      }

                                  }
        );
        mWebView.loadUrl(mUrl);  //加载页面
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mWebView = (WebView) view.findViewById(R.id.web_view);
    }


    /**
     * get Class Object
     *
     * @param url     url
     * @param title   title
     * @param isTitle require title?true:false
     * @param isBack  require back imageView?true:false
     * @return Class Object
     */
    public static WebViewFragment newInstance(String url, String title, boolean isTitle, boolean isBack) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        args.putBoolean("isTitle", isTitle);
        args.putBoolean("isBack", isBack);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void showErr(ErrorEntity err) {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showErrorView(String errorType) {

    }

    @Override
    public void showLoadingView() {

    }



    @Override
    public void showEmptyView(int imageId) {

    }

    @Override
    public void showContentView() {

    }


    /**
     * js 回调原生
     */
    class JSClient {

        /**
         * 跳转到患者个人中心
         */
        @JavascriptInterface
        public void loadPatientInfo() {
//            Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
//            startActivity(intent);
        }

        /**
         * 回到功能页面
         */
        @JavascriptInterface
        public void actResultCloseWin() {
//            handler.sendEmptyMessage(0);
//            Log.e("backToResult", "backToResult: -==========----->");
        }


    }
}
