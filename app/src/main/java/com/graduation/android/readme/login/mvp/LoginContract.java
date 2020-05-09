package com.graduation.android.readme.login.mvp;

import com.graduation.android.readme.base.mvp.BaseView;
import com.graduation.android.readme.base.mvp.IPresenter;

public class LoginContract {
    public interface View extends BaseView {
        void passwordLoginSuccess();
        void passwordLoginFailure(String  errMsg);
        void registerSuccess();
        void registerFailure(String  errMsg);

    }

    public interface Presenter extends IPresenter<View> {
        void passwordLogin(String account, String password);

        void register(String username, String password);


    }

}