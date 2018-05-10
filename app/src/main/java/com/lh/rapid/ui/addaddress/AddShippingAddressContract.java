package com.lh.rapid.ui.addaddress;

import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/10/16.
 */
public interface AddShippingAddressContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void updateShippingAddressSuccess(String s);

        void loadError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

        void updateShippingAddress(String addressId, String receiveName, String phone, String area,
                                   String detailAddress, String longitude, String latitude);

    }

}
