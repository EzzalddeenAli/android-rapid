package com.lh.rapid.ui.main;


import android.support.v4.app.Fragment;

import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;


/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface MainContract {

    interface View extends BaseView {

        void addFragment(Fragment fragment);

        void showFragment(Fragment fragment);

        void hideFragment(Fragment fragment);

        void loadError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        void onTabClick(int position);

        void initFragment();

    }

}
