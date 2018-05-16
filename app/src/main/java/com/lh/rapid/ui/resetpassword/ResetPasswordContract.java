package com.lh.rapid.ui.resetpassword;

import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/9/28.
 */

public class ResetPasswordContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(String error);

        void accountChangePasswordSuccess();

        void loadError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        void accountChangePassword(String newPassword, String oldPassword, String newPasswordAgain);

    }

}
