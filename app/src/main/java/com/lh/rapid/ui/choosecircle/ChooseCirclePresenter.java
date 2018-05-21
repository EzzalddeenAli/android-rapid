package com.lh.rapid.ui.choosecircle;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.log.Logger;
import com.google.gson.Gson;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.GeoCoderResultEntity;
import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.bean.HttpResult;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseCirclePresenter implements ChooseCircleContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private ChooseCircleContract.View mChooseLocationView;

    private CommonApi mCommonApi;
    private Bus mBus;

    @Inject
    public ChooseCirclePresenter(CommonApi commonApi, Bus bus) {
        mCommonApi = commonApi;
        mBus = bus;
    }

    @Override
    public void geocoderApi(String latLng) {
        disposables.add(mCommonApi.geocoderApi(latLng)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        Logger.i("ResponseBody ?");
                        Logger.i("ResponseBody "+responseBody.toString());
                        String geoCoderResultString = responseBody.string().replace("renderReverse&&renderReverse(", "").replace(")", "");
                        GeoCoderResultEntity geoCoderResultEntity = new Gson().fromJson(geoCoderResultString, GeoCoderResultEntity.class);
                        mChooseLocationView.geocoderResultSuccess(geoCoderResultEntity.getResult().getPois());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mChooseLocationView.onError(throwable);
                    }
                }));
    }

    @Override
    public void homeCircle(double longitude, double latitude) {
        mChooseLocationView.showLoading();
        disposables.add(mCommonApi.homeCircle(longitude, latitude)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<HomeCircleBean>>, ObservableSource<List<HomeCircleBean>>>() {
                    @Override
                    public ObservableSource<List<HomeCircleBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<HomeCircleBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mChooseLocationView.hideLoading();
                    }
                })
                .subscribe(new Consumer<List<HomeCircleBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<HomeCircleBean> homeCircleBeanList) throws Exception {
                        mChooseLocationView.homeCircleSuccess(homeCircleBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mChooseLocationView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ChooseCircleContract.View view) {
        mChooseLocationView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mChooseLocationView = null;
    }

}
