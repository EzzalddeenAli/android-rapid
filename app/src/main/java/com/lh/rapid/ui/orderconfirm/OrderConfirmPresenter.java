package com.lh.rapid.ui.orderconfirm;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.OrderSubmitBean;
import com.lh.rapid.bean.OrderSubmitConfirmBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2018/5/14.
 */

public class OrderConfirmPresenter implements OrderConfirmContract.Presenter {

    private OrderConfirmContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CommonApi mCommonApi;

    @Inject
    public OrderConfirmPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull OrderConfirmContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void orderSubmitConfirm(String addressId, String circleId, String paramsString) {
        mView.showLoading();
        disposables.add(mCommonApi.orderSubmitConfirm(addressId, circleId, paramsString)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<OrderSubmitConfirmBean>, ObservableSource<OrderSubmitConfirmBean>>() {
                    @Override
                    public ObservableSource<OrderSubmitConfirmBean> apply(@io.reactivex.annotations.NonNull HttpResult<OrderSubmitConfirmBean> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<OrderSubmitConfirmBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OrderSubmitConfirmBean orderSubmitConfirmBean) throws Exception {
                        mView.orderSubmitConfirmSuccess(orderSubmitConfirmBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void orderSubmit(String addressId, String circleId, String paramsString) {
        mView.showLoading();
        disposables.add(mCommonApi.orderSubmit(addressId, circleId, paramsString)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<OrderSubmitBean>, ObservableSource<OrderSubmitBean>>() {
                    @Override
                    public ObservableSource<OrderSubmitBean> apply(@io.reactivex.annotations.NonNull HttpResult<OrderSubmitBean> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<OrderSubmitBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OrderSubmitBean orderSubmitBean) throws Exception {
                        mView.orderSubmitSuccess(orderSubmitBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void addressDefault() {
        mView.showLoading();
        disposables.add(mCommonApi.addressDefault()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<AddressListBean>, ObservableSource<AddressListBean>>() {
                    @Override
                    public ObservableSource<AddressListBean> apply(@io.reactivex.annotations.NonNull HttpResult<AddressListBean> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<AddressListBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AddressListBean addressListBean) throws Exception {
                        mView.addressDefaultSuccess(addressListBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}
