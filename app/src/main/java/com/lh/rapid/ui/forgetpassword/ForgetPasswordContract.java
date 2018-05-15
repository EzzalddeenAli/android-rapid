package com.lh.rapid.ui.forgetpassword;

import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/9/28.
 */

public class ForgetPasswordContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(String error);

        void accountPasswordResetSuccess();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();

    }

    interface Presenter extends BasePresenter<View> {

        void accountPasswordReset(String mobile, String authCode, String newPassword, String passwordConfirm);

        void smsCodeSend(String mobile, String type);

    }

}
