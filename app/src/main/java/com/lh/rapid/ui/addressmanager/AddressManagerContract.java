package com.lh.rapid.ui.addressmanager;

import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/28.
 */
public class AddressManagerContract {

    interface View extends BaseView {

        void loadError(Throwable throwable);

        void showLoading();

        void hideLoading();

        void onLoadDateCompleted(List<AddressListBean> addressListBeans);

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate();

    }

}
