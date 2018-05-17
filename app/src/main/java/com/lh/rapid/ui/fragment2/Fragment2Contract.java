package com.lh.rapid.ui.fragment2;

import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment2Contract {

    interface  View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void categoryOneLevelSuccess(List<CategoryOneLevelBean> categoryOneLevelBeanList);

        void categoryDetailsSuccess(List<CategoryDetailsBean> categoryDetailsBeanList);

    }

    interface Presenter extends BasePresenter<Fragment2Contract.View> {

        void categoryOneLevel(String circleId);

        void categoryDetails(int parentId,String circleId);

    }

}
