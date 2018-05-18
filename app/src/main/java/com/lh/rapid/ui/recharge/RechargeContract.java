package com.lh.rapid.ui.recharge;


import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface RechargeContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void showError(String error);

        void rechargeSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void recharge();
    }

}
