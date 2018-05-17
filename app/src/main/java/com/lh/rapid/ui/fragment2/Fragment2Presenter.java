package com.lh.rapid.ui.fragment2;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.bean.HttpResult;

import java.util.List;
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
public class Fragment2Presenter implements Fragment2Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment2Contract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public Fragment2Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull Fragment2Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void categoryOneLevel(String circleId) {
        mView.showLoading();
        disposables.add(mCommonApi.categoryOneLevel(circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CategoryOneLevelBean>>, ObservableSource<List<CategoryOneLevelBean>>>() {
                    @Override
                    public ObservableSource<List<CategoryOneLevelBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CategoryOneLevelBean>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<List<CategoryOneLevelBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CategoryOneLevelBean> categoryOneLevelBeanList) throws Exception {
                        mView.categoryOneLevelSuccess(categoryOneLevelBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void categoryDetails(int parentId, String circleId) {
        mView.showLoading();
        disposables.add(mCommonApi.categoryDetails(parentId, circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CategoryDetailsBean>>, ObservableSource<List<CategoryDetailsBean>>>() {
                    @Override
                    public ObservableSource<List<CategoryDetailsBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CategoryDetailsBean>> accountInfoBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(accountInfoBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<List<CategoryDetailsBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CategoryDetailsBean> categoryDetailsBeen) throws Exception {
                        mView.categoryDetailsSuccess(categoryDetailsBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

}
