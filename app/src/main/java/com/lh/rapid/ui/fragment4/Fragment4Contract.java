package com.lh.rapid.ui.fragment4;


import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment4Contract {

    interface  View extends BaseView {

        void onError(Throwable throwable);

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends BasePresenter<View> {

    }

}
