package com.lh.rapid.ui.fragment3;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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

}