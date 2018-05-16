package com.lh.rapid.ui.resetpassword;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/9/28.
 */

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private ResetPasswordContract.View mView;

    @Inject
    public ResetPasswordPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void accountChangePassword(String newPassword, String oldPassword, String newPasswordAgain) {

        if (TextUtils.isEmpty(oldPassword)) {
            mView.showError("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            mView.showError("请输入新密码");
            return;
        }
        if (!newPassword.equals(newPasswordAgain)) {
            mView.showError("两次新密码不一致");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.accountChangePassword(newPassword, oldPassword)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.accountChangePasswordSuccess();
                        ToastUtil.showToast(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.loadError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ResetPasswordContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
