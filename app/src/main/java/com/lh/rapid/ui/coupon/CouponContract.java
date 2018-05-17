package com.lh.rapid.ui.coupon;

import com.lh.rapid.bean.UserCouponsBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/28.
 */
public class CouponContract {

    interface View extends BaseView {

        void onRefreshCompleted(List<UserCouponsBean> goodsDetailBeanList);

        void loadError(Throwable throwable);

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate();

    }

}
