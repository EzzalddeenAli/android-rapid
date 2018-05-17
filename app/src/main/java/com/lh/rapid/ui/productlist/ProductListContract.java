package com.lh.rapid.ui.productlist;

import com.lh.rapid.bean.ProductListBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/28.
 */
public class ProductListContract {

    interface View extends BaseView {

        // 获取数据成功
        void onRefreshCompleted(List<ProductListBean> goodsDetailBeanList, boolean isClear);

        void enableLoadMore(boolean isLoadAll);

        // 获取数据失败
        void loadError(Throwable throwable);

        // 获取到的数据为空
        void onEmpty();

        void showLoading();

        void hideLoading();

        void cartGoodsAddSuccess(String s);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String type, String goodsName, String sortType,
                       String sortStr, String categoryId, String circleId);

        void onLoadMore();

        void loadDate();

        void cartGoodsAdd(String goodsId,String quantity,String circleId);
    }

}
