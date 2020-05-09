package com.graduation.android.readme.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.bmob.v3.BmobUser
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import com.graduation.android.readme.R
import com.graduation.android.readme.base.eventbus.AppEventType
import com.graduation.android.readme.base.mvp.BaseMvpFragment
import com.graduation.android.readme.base.mvp.BaseView
import com.graduation.android.readme.base.mvp.IPresenter
import com.graduation.android.readme.base.network.ErrorEntity
import com.graduation.android.readme.login.LoginActivity
import com.graduation.android.readme.setting.SettingActivity
import com.graduation.android.share.utils.DialogUtil
import com.graduation.android.share.utils.ShareSdkUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * 我的页面，kotlin版
 */
class MineFragment2 : BaseMvpFragment<IPresenter<BaseView>, BaseView>(),
    PlatformActionListener, View.OnClickListener {
    private var tv_user_name_login: TextView? = null


    private var tv_setting: TextView? = null
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        var tvShare = view?.findViewById<TextView>(R.id.tv_share)

        tv_setting = view?.findViewById<TextView>(R.id.tv_setting)

        tv_user_name_login = view?.findViewById<TextView>(R.id.tv_user_name_login)
        var tvLogin = view?.findViewById<TextView>(R.id.tv_user_name_login)
        tvLogin?.setOnClickListener(this)
        tv_setting?.setOnClickListener(this)
        tvShare?.setOnClickListener {

            try {
                //                    final Bitmap bmp = BitmapFactory.decodeResource(getResources(), cn.gtscn.usercenter.R.mipmap.icon_share_friends);
                //                    final String mFilePath = setNewBitmap(bmp);
                val dialogUtil = DialogUtil.getInstance()
                val dialog = dialogUtil.showShareDialog(activity, true)
                val title = "待收货"
                val content = "服务"
                val finalShareUrl = "http://www.baidu.com"
                dialogUtil.setmDialogClickListener(object : DialogUtil.OnClickListener {
                    override fun onQqClick() {
                        mShareUtils = ShareSdkUtils(context)//实例化
                        mShareUtils!!.shareQQ_WebPage(//!!"加在变量名后，如果对象为null，那么系统一定会报异常！
                            finalShareUrl,
                            title,
                            content,
                            "",
                            this@MineFragment2
                        )
                    }

                    override fun onWeChatClick() {

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

                    override fun onQZoneClick() {
                        //  mShareUtils.shareQZone_WebPage(finalShareUrl, title, content, mFilePath, InviteBuildActivity.this);
                    }

                    override fun onWeChatFriendsClick() {
                        //    mShareUtils.shareWechatFriends_WebPage(finalShareUrl, title, content, mFilePath, InviteBuildActivity.this);
                    }

                    override fun onMessageClick() {
                        //    mShareUtils.shareMessage_Test(title + (type == 1 ? ",邀请地址<" : ",领取地址<") + finalShareUrl + ">", InviteBuildActivity.this);
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        getUser()
    }


    override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
    }

    override fun onCancel(p0: Platform?, p1: Int) {
    }

    override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
    }

    private var mShareUtils: ShareSdkUtils? = null//定义一个变量，可为null类型

    override fun dismissProgress() {
    }

    override fun showLoadingDialog(msg: String?) {
    }

    override fun hideLoadingDialog() {
    }

    override fun showErr(err: ErrorEntity?) {
    }

    override fun getLayoutId(): Int {//重写layout布局的findViewById
        return R.layout.frag_mine
    }

    override fun showErrorView(errorType: String?) {
    }

    override fun showLoadingView() {
    }

    override fun initPresenter() = null

    override fun showTip(message: String?) {
    }

    override fun showErrorView() {
    }

    override fun showEmptyView(imageId: Int) {
    }


    override fun showProgress() {
    }

    override fun loadData() {
    }


    override fun showContentView() {

    }

    override fun bindEventListener() {

        tv_user_name_login?.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_user_name_login -> {
                if (!BmobUser.isLogin()) {//未登录
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            R.id.tv_setting -> {//设置页
                if (BmobUser.isLogin()) {//已登录
                    val intent = Intent(activity, SettingActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AppEventType) {
        when (event.type) {
            AppEventType.LOGIN_SUCCESS, AppEventType.LOGIN_OUT -> getUser()
        }
    }

    fun getUser() {
        if (BmobUser.isLogin()) {//已登录
            val user: BmobUser = BmobUser.getCurrentUser(BmobUser::class.java)//获取当前用户信息
            tv_user_name_login?.text = user.username
        } else {
            tv_user_name_login?.text = "注册/登录"
        }

    }

    override fun isActive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}