package com.lh.rapid.ui.userinfo;

import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/9/28.
 */

public class UserInfoContract {

    interface View extends BaseView {

        void onLoadDateCompleted(AccountInfoBean accountInfoBean);

        void loadError(Throwable throwable);

        void accountInfoCompletedError();

        void showLoading();

        void hideLoading();

        void accountInfoCompletedSuccess(String string);

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate();

        void accountInfoCompleted(String nickName, String gender, String email, String birthday);

    }

}
