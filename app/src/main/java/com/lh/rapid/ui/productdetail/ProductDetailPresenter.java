package com.lh.rapid.ui.productdetail;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/21.
 */

public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private ProductDetailContract.View mView;

    @Inject
    public ProductDetailPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull ProductDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }


    @Override
    public void loadDate(String goodsId) {
        disposables.add(mCommonApi.goodsDetail(goodsId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<GoodsDetailBean>, ObservableSource<GoodsDetailBean>>() {
                    @Override
                    public ObservableSource<GoodsDetailBean> apply(@io.reactivex.annotations.NonNull HttpResult<GoodsDetailBean> httpResult) throws Exception {
                        return CommonApi.flatResponse(httpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GoodsDetailBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull GoodsDetailBean goodsDetailBean) throws Exception {
                        mView.onLoadDateCompleted(goodsDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}