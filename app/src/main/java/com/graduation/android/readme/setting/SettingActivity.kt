package com.graduation.android.readme.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bmob.v3.BmobUser
import com.graduation.android.readme.R
import com.graduation.android.readme.base.eventbus.AppEventType
import com.graduation.android.readme.base.mvp.BaseView
import com.graduation.android.readme.base.mvp.IPresenter
import com.graduation.android.readme.basemodule.BaseActivity
import com.graduation.android.readme.downloadapk.AboutUsActivity
import org.greenrobot.eventbus.EventBus

class SettingActivity : BaseActivity<IPresenter<BaseView>, BaseView>(), View.OnClickListener {


    private var tvRight: TextView? = null

    private var rl_about_us: RelativeLayout? = null
    private var tv_user_name_login_out: TextView? = null
    private var edtFeedback: EditText? = null
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_logout -> {
                BmobUser.logOut();
                EventBus.getDefault().post(AppEventType(AppEventType.LOGIN_OUT))
                finish()
            }
            R.id.rl_about_us -> {
                startActivity(Intent(mActivity, AboutUsActivity::class.java))
            }
        }
    }


    override fun bindEventListener() {
        tvRight?.setOnClickListener(this)

    }


    override fun initWidget(savedInstanceState: Bundle?) {
        var tvTitle: TextView = toolbarTitleView as TextView
        tvTitle.setText("设置")
        tvRight = toolbarRightView as TextView
        tvRight!!.setText("")
        tv_user_name_login_out = findViewById<TextView>(R.id.btn_logout)
        tv_user_name_login_out?.setOnClickListener(this)

        rl_about_us = findViewById<RelativeLayout>(R.id.rl_about_us)
        rl_about_us?.setOnClickListener(this)
    }

    override fun showProgress() {

    }

    override fun isActive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTip(message: String?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun loadData() {
    }

    override fun dismissProgress() {
    }


    override fun initPresenter() = null

}