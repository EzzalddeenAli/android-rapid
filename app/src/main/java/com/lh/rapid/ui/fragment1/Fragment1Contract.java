package com.lh.rapid.ui.fragment1;


import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment1Contract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void loadError(Throwable throwable);

        void onLoadDateCompleted(HomePageBean homePageBean);

        void homeCircleSuccess(List<HomeCircleBean> homeCircleBeanList);

        void onLoadProductListSuccess(List<HomePageBean.CategoryListsBean.GoodListBean> goodListBeans);

        void cartGoodsAddSuccess(String s);

        void cartGoodsDeleteSuccess(String s);
    }

    interface Presenter extends BasePresenter<View> {

        void loadDate(String circleId);

        void homeCircle(double longitude, double latitude);

        void onLoadProductList(String categoryId, String circleId);

        void cartGoodsAdd(String goodsId,String quantity,String circleId);

        void cartGoodsDelete(String goodsId,String circleId);
    }

}
