package com.lh.rapid.ui.addressmanager;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.HttpResult;
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

public class AddressManagerPresenter implements AddressManagerContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private AddressManagerContract.View mView;

    @Inject
    public AddressManagerPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull AddressManagerContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate() {
        disposables.add(mCommonApi.addressList()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<AddressListBean>>, ObservableSource<List<AddressListBean>>>() {
                    @Override
                    public ObservableSource<List<AddressListBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<AddressListBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AddressListBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<AddressListBean> homeCircleBeanList) throws Exception {
                        mView.onLoadDateCompleted(homeCircleBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }
}
