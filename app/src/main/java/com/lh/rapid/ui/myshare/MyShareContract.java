package com.lh.rapid.ui.myshare;

import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/28.
 */

public class MyShareContract {

    interface View extends BaseView {

        // 获取数据成功
        void onRefreshCompleted(List<String> focusListBeanList, boolean isClear);

        // 获取到的数据有没有下一页        false:没有下一页  true:有下一页
        void onLoadCompleted(boolean isLoadAll);

        // 获取数据失败
        void onError(Throwable throwable);

        // 获取到的数据为空
        void onEmpty();

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate();

        void onRefresh();

        void onLoadMore();

    }

}
