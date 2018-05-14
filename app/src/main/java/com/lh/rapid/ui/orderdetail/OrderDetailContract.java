package com.lh.rapid.ui.orderdetail;


import com.lh.rapid.bean.OrderDetailBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by lh on 2017/10/12.
 */
public interface OrderDetailContract {

    interface  View extends BaseView {

        void showLoading();

        void hideLoading(int type);

        // 获取数据成功
        void orderDetailSuccess(OrderDetailBean orderDetailBean);

        // 获取数据失败
        void onError(Throwable throwable);

        void orderFinishSuccess(String s);

        void orderCancelSuccess(String s);

    }

    interface Presenter extends BasePresenter<View> {

        // 加载数据
        void orderDetail(int orderId);

        void orderFinish(int orderId);

        void orderCancel(int orderId);

    }

}
