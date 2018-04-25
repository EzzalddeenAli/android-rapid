package com.lh.rapid.ui.fragment1;

import android.support.annotation.NonNull;

import com.lh.rapid.api.common.CommonApi;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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

}
