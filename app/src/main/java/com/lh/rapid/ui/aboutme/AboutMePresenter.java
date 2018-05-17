package com.lh.rapid.ui.aboutme;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.CommonNewsInfoBean;
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

public class AboutMePresenter implements AboutMeContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private AboutMeContract.View mView;

    @Inject
    public AboutMePresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull AboutMeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void commonNewsInfo() {
        disposables.add(mCommonApi.commonNewsInfo()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CommonNewsInfoBean>>, ObservableSource<List<CommonNewsInfoBean>>>() {
                    @Override
                    public ObservableSource<List<CommonNewsInfoBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CommonNewsInfoBean>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CommonNewsInfoBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CommonNewsInfoBean> commonNewsInfoBeanList) throws Exception {
                        mView.commonNewsInfoSuccess(commonNewsInfoBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}
