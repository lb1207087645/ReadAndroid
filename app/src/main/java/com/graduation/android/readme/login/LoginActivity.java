package com.graduation.android.readme.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.android.readme.R;
import com.graduation.android.readme.activity.MainActivity;
import com.graduation.android.readme.base.BaseApplication;
import com.graduation.android.readme.base.eventbus.AppEventType;
import com.graduation.android.readme.base.utils.ToastUtils;
import com.graduation.android.readme.basemodule.BaseActivity;
import com.graduation.android.readme.login.mvp.LoginContract;
import com.graduation.android.readme.login.mvp.LoginPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter, LoginContract.View> implements LoginContract.View {

    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.iv_weibo)
    ImageView ivWeibo;

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isUseToolbar() {
        return true;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {

        ((TextView) getToolbarTitleView()).setText("登录");
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
        ToastUtils.showToast(BaseApplication.getContext(), "登录成功");
        finish();
        startActivity(new Intent(this, MainActivity.class));
        EventBus.getDefault().post(new AppEventType(AppEventType.LOGIN_SUCCESS));
    }

    @Override
    public void passwordLoginFailure(String errMsg) {
        ToastUtils.showToast(BaseApplication.getContext(), "登录失败" + errMsg);
    }


    @Override
    public void registerSuccess() {

    }

    @Override
    public void registerFailure(String errMsg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                String userName = edtUserName.getText().toString().trim();
                String pwd = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.showToast(LoginActivity.this, "用户名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入密码");
                    return;
                }
                mPresenter.passwordLogin(userName, pwd);

                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                break;
        }
    }


}

