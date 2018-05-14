package com.lh.rapid.ui.orderconfirm;

import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.OrderSubmitBean;
import com.lh.rapid.bean.OrderSubmitConfirmBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2018/5/14.
 */

public class OrderConfirmContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void loadError(Throwable throwable);

        void orderSubmitConfirmSuccess(OrderSubmitConfirmBean orderSubmitConfirmBean);

        void orderSubmitSuccess(OrderSubmitBean orderSubmitBean);

        void addressDefaultSuccess(AddressListBean addressListBean);
    }

    interface Presenter extends BasePresenter<View> {

        void orderSubmitConfirm(String addressId, String circleId, String paramsString);

        void orderSubmit(String addressId, String circleId, String paramsString);

        void addressDefault();

    }

}
