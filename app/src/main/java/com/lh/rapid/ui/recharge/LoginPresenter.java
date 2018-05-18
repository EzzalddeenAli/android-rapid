package com.lh.rapid.ui.recharge;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.PhoneUtil;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
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
 * Created by we-win on 2017/7/21.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private LoginContract.View mLoginView;

    @Inject
    public LoginPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void login(String userName, String password, int type) {
        if (TextUtils.isEmpty(userName) && type == 1) {
            mLoginView.showError("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(userName) && type == 2) {
            mLoginView.showError("请输入卡号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mLoginView.showError("请输入密码");
            return;
        }
        mLoginView.showLoading();
        disposables.add(mCommonApi.loginNormal(userName, password)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<LoginEntity>, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                })
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        mUserStorage.setToken(loginEntity.getToken());
                        mLoginView.loginSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
                    }
                }));

    }

    @Override
    public void loginMobile(String userName, String smsCode) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showError("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            mLoginView.showError("请输入验证码");
            return;
        }
        mLoginView.showLoading();
        disposables.add(mCommonApi.loginMobile(userName, smsCode)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<LoginEntity>, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                })
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        mUserStorage.setToken(loginEntity.getToken());
                        mLoginView.loginSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
                    }
                }));
    }

    @Override
    public void smsCodeSend(String mobile, String type) {
        if (TextUtils.isEmpty(mobile)) {
            mLoginView.showError("请输入手机号");
            return;
        }
        if (!PhoneUtil.isMobile(mobile)) {
            mLoginView.showError("手机号码格式不正确");
            return;
        }
        mLoginView.refreshSmsCodeUi();
        disposables.add(mCommonApi.smsCodeSend(mobile, type)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        ToastUtil.showToast(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }

}
