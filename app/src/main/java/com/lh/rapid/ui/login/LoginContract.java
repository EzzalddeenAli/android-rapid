package com.lh.rapid.ui.login;


import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface LoginContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(String error);

        void loginSuccess();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();
    }

    interface Presenter extends BasePresenter<View> {

        void login(String userName, String identifyingCode, int type);

        void loginMobile(String userName, String smsCode);

        void smsCodeSend(String mobile, String type);
    }

}
