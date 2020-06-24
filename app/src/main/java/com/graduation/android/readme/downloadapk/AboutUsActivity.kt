package com.graduation.android.readme.downloadapk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.graduation.android.readme.R
import com.graduation.android.readme.base.mvp.BaseView
import com.graduation.android.readme.base.mvp.IPresenter
import com.graduation.android.readme.basemodule.BaseActivity
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : BaseActivity<IPresenter<BaseView>, BaseView>(), View.OnClickListener {

    override fun initPresenter() = null
    override fun isActive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
    }

    override fun showTip(message: String?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        tv_about.setOnClickListener(this)
    }

    override fun loadData() {
    }

    override fun dismissProgress() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_about -> {
                VersionDialog(this@AboutUsActivity, "http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk")
            }
        }
    }


}
