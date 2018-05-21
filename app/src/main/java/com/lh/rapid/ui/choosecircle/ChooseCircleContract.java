package com.lh.rapid.ui.choosecircle;


import com.lh.rapid.bean.GeoCoderResultEntity;
import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseCircleContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList);

        void onError(Throwable throwable);

        void homeCircleSuccess(List<HomeCircleBean> homeCircleBeanList);
    }

    interface Presenter extends BasePresenter<View> {

        void geocoderApi(String latLng);

        void homeCircle(double longitude, double latitude);

    }

}
