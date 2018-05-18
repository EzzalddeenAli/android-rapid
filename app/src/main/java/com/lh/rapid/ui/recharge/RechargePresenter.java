package com.lh.rapid.ui.recharge;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by we-win on 2017/7/21.
 */

public class RechargePresenter implements RechargeContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private RechargeContract.View mLoginView;

    @Inject
    public RechargePresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull RechargeContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }

    @Override
    public void recharge() {

    }
}
