package com.lh.rapid.ui.orderlist.all;

import com.lh.rapid.bean.OrderBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/10/12.
 */
public interface OrderAllContract {

    interface  View extends BaseView {

        void onRefreshCompleted(List<OrderBean> orderBeanList, boolean isClear);

        void enableLoadMore(boolean isLoadAll);

        void loadError(Throwable throwable);

        void onEmpty();

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(int status);

        void loadDate();

        void onLoadMore();


    }

}
