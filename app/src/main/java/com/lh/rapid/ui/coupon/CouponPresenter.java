package com.lh.rapid.ui.coupon;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.UserCouponsBean;
import com.lh.rapid.components.storage.UserStorage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/9/28.
 */

public class CouponPresenter implements CouponContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private CouponContract.View mView;

    @Inject
    public CouponPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull CouponContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate() {
        disposables.add(mCommonApi.userCoupons()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<UserCouponsBean>>, ObservableSource<List<UserCouponsBean>>>() {
                    @Override
                    public ObservableSource<List<UserCouponsBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<UserCouponsBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserCouponsBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<UserCouponsBean> userCouponsBeans) throws Exception {
                        mView.onRefreshCompleted(userCouponsBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}
