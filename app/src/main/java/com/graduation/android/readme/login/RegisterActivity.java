package com.graduation.android.readme.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.graduation.android.readme.R;
import com.graduation.android.readme.base.BaseApplication;
import com.graduation.android.readme.base.utils.ToastUtils;
import com.graduation.android.readme.basemodule.BaseActivity;
import com.graduation.android.readme.login.mvp.LoginContract;
import com.graduation.android.readme.login.mvp.LoginPresenter;
import com.graduation.android.readme.news.mvp.NewsPresenter;
import com.mob.wrappers.AnalySDKWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity<LoginContract.Presenter, LoginContract.View> implements LoginContract.View {

    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {

        ((TextView) getToolbarTitleView()).setText("注册");
    }

    @Override
    public void loadData() {
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
    public void passwordLoginSuccess() {

    }

    @Override
    public void passwordLoginFailure(String errMsg) {

    }


    @Override
    public void registerSuccess() {
        ToastUtils.showToast(BaseApplication.getContext(), "注册成功");
        finish();
    }

    @Override
    public void registerFailure(String errMsg) {
        ToastUtils.showToast(BaseApplication.getContext(), "注册失败：" + errMsg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_register)
    public void onViewClicked() {
        String userName = edtUserName.getText().toString();
        String pwd = edtPassword.getText().toString();
        String confirmPwd = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showToast(RegisterActivity.this, "用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(RegisterActivity.this, "请输入密码");
            return;
        }

        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtils.showToast(RegisterActivity.this, "再次输入密码");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            ToastUtils.showToast(RegisterActivity.this, "两次密码输入不一致");
            return;
        }
        mPresenter.register(userName, pwd);
    }
}

