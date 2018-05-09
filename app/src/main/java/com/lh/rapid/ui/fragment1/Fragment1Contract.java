package com.lh.rapid.ui.fragment1;


import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment1Contract {

    interface  View extends BaseView {

        void showLoading();

        void hideLoading();

        void loadError(Throwable throwable);

        void onLoadDateCompleted(HomePageBean homePageBean);

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate(String circleId);

    }

}
