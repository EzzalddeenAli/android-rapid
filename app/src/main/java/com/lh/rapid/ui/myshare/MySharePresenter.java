package com.lh.rapid.ui.myshare;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lh on 2017/9/28.
 */

public class MySharePresenter implements MyShareContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private MyShareContract.View mView;

    @Inject
    public MySharePresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }


    @Override
    public void attachView(@NonNull MyShareContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadDate() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

}
