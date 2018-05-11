package com.lh.rapid.ui.fragment3;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.bean.HttpResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment3Presenter implements Fragment3Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment3Contract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public Fragment3Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull Fragment3Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate() {
        disposables.add(mCommonApi.cartGoodsList()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CartGoodsBean>>, ObservableSource<List<CartGoodsBean>>>() {
                    @Override
                    public ObservableSource<List<CartGoodsBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CartGoodsBean>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CartGoodsBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CartGoodsBean> cartGoodsBeans) throws Exception {
                        mView.onLoadDateCompleted(cartGoodsBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void cartGoodsAdd(String goodsId, String quantity, String circleId) {
        disposables.add(mCommonApi.cartGoodsAdd(goodsId, quantity, circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> httpResult) throws Exception {
                        return CommonApi.flatResponse(httpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.cartGoodsAddSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void cartGoodsDelete(String goodsId, String circleId) {
        disposables.add(mCommonApi.cartGoodsDelete(goodsId, circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> httpResult) throws Exception {
                        return CommonApi.flatResponse(httpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.cartGoodsDeleteSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}