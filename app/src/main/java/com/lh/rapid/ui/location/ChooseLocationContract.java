package com.lh.rapid.ui.location;


import com.lh.rapid.bean.GeoCoderResultEntity;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseLocationContract {
    interface View extends BaseView {

        void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

        void geocoderApi(String latLng);

    }
}
