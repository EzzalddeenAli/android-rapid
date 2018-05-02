package com.lh.rapid.ui.register;


import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/9/28.
 */

public class RegisterContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(String error);

        void registerSuccess();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();

    }

    interface Presenter extends BasePresenter<View> {

        void register(String phone, String validate, String password, String passwordConfirm);

        void smsCodeSend(String mobile, int type);

    }

}
