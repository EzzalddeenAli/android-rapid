package com.lh.rapid.ui.aboutme;

import com.lh.rapid.bean.CommonNewsInfoBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/28.
 */

public class AboutMeContract {

    interface View extends BaseView {

        void commonNewsInfoSuccess(List<CommonNewsInfoBean> commonNewsInfoBeanList);

        void loadError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        void commonNewsInfo();

    }

}
