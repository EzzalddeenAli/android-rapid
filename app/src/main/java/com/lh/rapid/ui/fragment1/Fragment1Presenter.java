package com.lh.rapid.ui.fragment1;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.DictionaryBean;
import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.ProductListBean;

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
public class Fragment1Presenter implements Fragment1Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment1Contract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public Fragment1Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull Fragment1Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate(String circleId) {
        mView.showLoading();
        disposables.add(mCommonApi.homePage(circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<HomePageBean>, ObservableSource<HomePageBean>>() {
                    @Override
                    public ObservableSource<HomePageBean> apply(@io.reactivex.annotations.NonNull HttpResult<HomePageBean> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<HomePageBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull HomePageBean homePageBean) throws Exception {
                        mView.onLoadDateCompleted(homePageBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void homeCircle(double longitude, double latitude) {
        mView.showLoading();
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
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<List<HomeCircleBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<HomeCircleBean> homeCircleBeanList) throws Exception {
                        mView.homeCircleSuccess(homeCircleBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void onLoadProductList(String categoryId, String circleId) {
        mView.showLoading();
        disposables.add(mCommonApi.goodsListHome(categoryId, circleId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<HomePageBean.CategoryListsBean.GoodListBean>>, ObservableSource<List<HomePageBean.CategoryListsBean.GoodListBean>>>() {
                    @Override
                    public ObservableSource<List<HomePageBean.CategoryListsBean.GoodListBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<HomePageBean.CategoryListsBean.GoodListBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<List<HomePageBean.CategoryListsBean.GoodListBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<HomePageBean.CategoryListsBean.GoodListBean> goodsDetailBeanList) throws Exception {
                        mView.onLoadProductListSuccess(goodsDetailBeanList);
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

    @Override
    public void search(String type, String goodsName, String sortType, String sortStr, String categoryId, String circleId) {
        disposables.add(mCommonApi.goodsList(type, goodsName, sortType, sortStr, categoryId, circleId, 1, 20)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<ProductListBean>>, ObservableSource<List<ProductListBean>>>() {
                    @Override
                    public ObservableSource<List<ProductListBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<ProductListBean>> homePageBeanHttpResult) throws Exception {
                        return CommonApi.flatResponse(homePageBeanHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ProductListBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<ProductListBean> productListBeanList) throws Exception {
                        mView.searchSuccess(productListBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void commonDictionaryQuery() {
        disposables.add(mCommonApi.commonDictionaryQuery()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<DictionaryBean>>, ObservableSource<List<DictionaryBean>>>() {
                    @Override
                    public ObservableSource<List<DictionaryBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<DictionaryBean>> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DictionaryBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<DictionaryBean> dictionaryBeanList) throws Exception {
                        mView.commonDictionaryQuerySuccess(dictionaryBeanList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

}
