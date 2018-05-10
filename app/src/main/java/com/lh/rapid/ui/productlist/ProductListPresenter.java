package com.lh.rapid.ui.productlist;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.ProductListBean;
import com.lh.rapid.components.storage.UserStorage;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

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

public class ProductListPresenter implements ProductListContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private ProductListContract.View mView;
    private int page = 1;
    private int pageSize = 10;
    private String mType;
    private String mGoodsName;
    private String mSortType;
    private String mSortStr;
    private String mCategoryId;
    private String mCircleId;
    private RefreshLayout mRefreshlayout;

    @Inject
    public ProductListPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull ProductListContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void loadDate() {
        disposables.add(mCommonApi.goodsList(mType, mGoodsName, mSortType, mSortStr, mCategoryId, mCircleId, page, pageSize)
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
                    public void accept(@io.reactivex.annotations.NonNull List<ProductListBean> goodsDetailBeanList) throws Exception {
                        if (page == 1 && goodsDetailBeanList.size() == 0) {
                            mView.onEmpty();
                        } else {
                            mView.onRefreshCompleted(goodsDetailBeanList, page == 1);
                            mView.enableLoadMore(goodsDetailBeanList.size() == pageSize ? true : false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void onRefresh(String type, String goodsName, String sortType, String sortStr, String categoryId, String circleId) {
        mType = type;
        mGoodsName = goodsName;
        mSortType = sortType;
        mSortStr = sortStr;
        mCategoryId = categoryId;
        mCircleId = circleId;
        page = 1;
        loadDate();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadDate();
    }

}
