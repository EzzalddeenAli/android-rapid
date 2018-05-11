package com.lh.rapid.ui.fragment3;

import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.ui.BasePresenter;
import com.lh.rapid.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment3Contract {

    interface  View extends BaseView {

        void loadError(Throwable throwable);

        void onLoadDateCompleted(List<CartGoodsBean> cartGoodsBeans);

        void cartGoodsAddSuccess(String s);

        void cartGoodsDeleteSuccess(String s);

    }

    interface Presenter extends BasePresenter<View> {

        void loadDate();

        void cartGoodsAdd(String goodsId,String quantity,String circleId);

        void cartGoodsDelete(String goodsId,String circleId);

    }

}
