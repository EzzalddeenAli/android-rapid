package com.lh.rapid.ui.orderdetail;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.OrderDetailBean;

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
public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private OrderDetailContract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public OrderDetailPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void attachView(@NonNull OrderDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void orderDetail(int orderId) {
        mView.showLoading();
        disposables.add(mCommonApi.orderDetail(orderId+"")
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<OrderDetailBean>, ObservableSource<OrderDetailBean>>() {
                    @Override
                    public ObservableSource<OrderDetailBean> apply(@io.reactivex.annotations.NonNull HttpResult<OrderDetailBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderDetailBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OrderDetailBean orderLogisticsBean) throws Exception {
                        if (orderLogisticsBean != null) {
                            mView.hideLoading(0);
                            mView.orderDetailSuccess(orderLogisticsBean);
                        } else {
                            mView.hideLoading(-1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.hideLoading(-1);
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void orderFinish(int orderId) {
        disposables.add(mCommonApi.orderFinish(orderId+"")
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.orderFinishSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void orderCancel(int orderId) {
        disposables.add(mCommonApi.orderCancel(orderId+"")
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.orderCancelSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

}
