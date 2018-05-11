package com.lh.rapid.ui.productdetail;


import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface ProductDetailContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void loadError(Throwable throwable);

        void onLoadDateCompleted(GoodsDetailBean goodsDetailBean);

        void cartGoodsAddSuccess(String s);

        void cartGoodsDeleteSuccess(String s);

    }

    interface Presenter extends BasePresenter<ProductDetailContract.View> {

        void loadDate(String circleId, String goodsId);

        void cartGoodsAdd(String goodsId,String quantity,String circleId);

        void cartGoodsDelete(String goodsId,String circleId);

    }

}
