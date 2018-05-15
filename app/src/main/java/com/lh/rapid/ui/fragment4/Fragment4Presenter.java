package com.lh.rapid.ui.fragment4;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.bean.AccountUserHomeBean;
import com.lh.rapid.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment4Presenter implements Fragment4Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment4Contract.View mView;
    private CommonApi mCommonApi;


    @Inject
    public Fragment4Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull Fragment4Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void accountInfo() {
        mView.showLoading();
        disposables.add(mCommonApi.accountInfo()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<AccountInfoBean>, ObservableSource<AccountInfoBean>>() {
                    @Override
                    public ObservableSource<AccountInfoBean> apply(@io.reactivex.annotations.NonNull HttpResult<AccountInfoBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<AccountInfoBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AccountInfoBean accountInfoBean) throws Exception {
                        mView.accountInfoSuccess(accountInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void accountUserHome() {
        mView.showLoading();
        disposables.add(mCommonApi.accountUserHome()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<AccountUserHomeBean>, ObservableSource<AccountUserHomeBean>>() {
                    @Override
                    public ObservableSource<AccountUserHomeBean> apply(@io.reactivex.annotations.NonNull HttpResult<AccountUserHomeBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<AccountUserHomeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AccountUserHomeBean accountInfoBean) throws Exception {
                        mView.accountUserHomeSuccess(accountInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}
