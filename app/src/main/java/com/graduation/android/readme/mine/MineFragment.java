package com.graduation.android.readme.mine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.graduation.android.readme.R;
import com.graduation.android.readme.base.mvp.BaseMvpFragment;
import com.graduation.android.readme.base.network.ErrorEntity;
import com.graduation.android.readme.base.utils.L;
import com.graduation.android.readme.home.mvp.HomeContract;
import com.graduation.android.share.utils.DialogUtil;
import com.graduation.android.share.utils.ShareSdkUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * 我的页面Fragment,Java版
 */
public class MineFragment extends BaseMvpFragment implements PlatformActionListener {


    private static final String TAG = "MineFragment";

    private ShareSdkUtils mShareUtils;

    @Override
    protected HomeContract.Presenter initPresenter() {
        return null;
        // return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_mine;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.tv_share).setOnClickListener(onClickListner);
//
//        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.pullToLoadList();
//            }
//        });
//
//        rv = (RecyclerView) view.findViewById(R.id.rv);
//        rv.addItemDecoration(new GridSpacingDecorator(DensityUtils.dp2px(mActivity, 8)));
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
//        rv.setLayoutManager(gridLayoutManager);
//
//        adapter = new LoadMoreAdapter(rv,
//                new DesignResAdapter(mActivity, datas),
//                new LoadMoreAdapter.OnLoadMoreListener() {//加载更多
//                    @Override
//                    public void onLoadMore() {
//                        mPresenter.loadList(curPage + 1);
//                    }
//                });
//        rv.setAdapter(adapter);


    }

    private View.OnClickListener onClickListner = new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
//                case R.id.tv_personal_file://跳转到个人资料页面
//                    break;
//                case R.id.tv_about://跳转到关于页面
//                    EventBus.getDefault().post(new StartBrotherEvent(AboutFragment.newInstance()));
//                    break;
//                case R.id.iv_right://跳转到设置页面
//                    EventBus.getDefault().post(new StartBrotherEvent(SettingFragment.newInstance()));
//                    break;
                case R.id.tv_share://界面


                    try {
//                    final Bitmap bmp = BitmapFactory.decodeResource(getResources(), cn.gtscn.usercenter.R.mipmap.icon_share_friends);
//                    final String mFilePath = setNewBitmap(bmp);
                        DialogUtil dialogUtil = DialogUtil.getInstance();
                        final Dialog dialog = dialogUtil.showShareDialog(getActivity(), true);
                        final String title = "待收货";
                        final String content = "服务";
                        final String finalShareUrl = "http://www.baidu.com";
                        dialogUtil.setmDialogClickListener(new DialogUtil.OnClickListener() {
                            @Override
                            public void onQqClick() {
                                mShareUtils = new ShareSdkUtils(getContext());
                                mShareUtils.shareQQ_WebPage(finalShareUrl, title, content, "", MineFragment.this);
                            }

                            @Override
                            public void onWeChatClick() {

//
//                    Wechat.ShareParams sp = new Wechat.ShareParams();
//                    //微信分享网页的参数严格对照列表中微信分享网页的参数要求
//                    sp.setTitle("标题");
//                    sp.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
//                    sp.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    sp.setUrl("http://sharesdk.cn");
//                    sp.setShareType(Platform.SHARE_WEBPAGE);
//                    LogUtils.d("ShareSDK", sp.toMap().toString());
//                    Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
//// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
//                    weChat.setPlatformActionListener(new PlatformActionListener() {
//                        @Override
//                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                            LogUtils.d("ShareSDK", "onComplete ---->  分享成功");
//
//                        }
//
//                        @Override
//                        public void onError(Platform platform, int i, Throwable throwable) {
//                            LogUtils.d("ShareSDK", "onError ---->  分享失败" + throwable.getStackTrace().toString());
//                            LogUtils.d("ShareSDK", "onError ---->  分享失败" + throwable.getMessage());
//                            throwable.getMessage();
//                            throwable.printStackTrace();
//                        }
//
//                        @Override
//                        public void onCancel(Platform platform, int i) {
//                            LogUtils.d("ShareSDK", "onCancel ---->  分享取消");
//                        }
//                    });
//// 执行图文分享
//                    weChat.share(sp);


                                //        mShareUtils.shareWechat_WebPage(finalShareUrl, title, content, bmp, InviteBuildActivity.this);
                            }

                            @Override
                            public void onQZoneClick() {
                                //  mShareUtils.shareQZone_WebPage(finalShareUrl, title, content, mFilePath, InviteBuildActivity.this);
                            }

                            @Override
                            public void onWeChatFriendsClick() {
                                //    mShareUtils.shareWechatFriends_WebPage(finalShareUrl, title, content, mFilePath, InviteBuildActivity.this);
                            }

                            @Override
                            public void onMessageClick() {
                                //    mShareUtils.shareMessage_Test(title + (type == 1 ? ",邀请地址<" : ",领取地址<") + finalShareUrl + ">", InviteBuildActivity.this);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showProgress() {
//        srl.post(new Runnable() {
//            @Override
//            public void run() {
//                srl.setRefreshing(true);
//            }
//        });
    }

    @Override
    public void dismissProgress() {
//        srl.setRefreshing(false);
    }

    @Override
    public void showTip(String message) {
        showToast(message);
    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void hideLoadingDialog() {

    }


    @Override
    public void showErr(ErrorEntity err) {
        L.d(TAG, err.errMsg);


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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
