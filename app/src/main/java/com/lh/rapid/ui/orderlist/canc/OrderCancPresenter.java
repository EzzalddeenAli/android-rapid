package com.lh.rapid.ui.orderlist.canc;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.OrderBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/10/12.
 */
public class OrderCancPresenter implements OrderCancContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private OrderCancContract.View mView;
    private CommonApi mCommonApi;
    private int page = 1;
    private int pageSize = 10;
    private int mStatus;

    @Inject
    public OrderCancPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull OrderCancContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate() {
        disposables.add(mCommonApi.orderList(mStatus + "", page, pageSize)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<OrderBean>>, ObservableSource<List<OrderBean>>>() {
                    @Override
                    public ObservableSource<List<OrderBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<OrderBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OrderBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<OrderBean> orderBeanList) throws Exception {
                        if (page == 1 && orderBeanList.size() == 0) {
                            mView.onEmpty();
                        } else {
                            mView.onRefreshCompleted(orderBeanList, page == 1);
                            mView.enableLoadMore(orderBeanList.size() == pageSize ? true : false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void onRefresh(int status) {
        mStatus = status;
        page = 1;
        loadDate();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadDate();
    }


}
