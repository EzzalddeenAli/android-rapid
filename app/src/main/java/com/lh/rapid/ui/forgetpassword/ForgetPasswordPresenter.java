package com.lh.rapid.ui.forgetpassword;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.Constants;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ForgetPasswordPresenter implements ForgetPasswordContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private UserStorage mUserStorage;

    private ForgetPasswordContract.View mView;

    @Inject
    public ForgetPasswordPresenter(CommonApi commonApi, UserStorage userStorage) {
        mCommonApi = commonApi;
        mUserStorage = userStorage;
    }

    @Override
    public void accountPasswordReset(String mobile, String authCode, String newPassword, String passwordConfirm) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showError("请输入用户名或手机号");
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            mView.showError("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            mView.showError("请输入新密码");
            return;
        }
        if(!isPassword(newPassword)){
            mView.showError("密码必须是6-16位数字和字母的组合");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.accountPasswordReset(mobile,authCode,newPassword)
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
                        mView.accountPasswordResetSuccess();
                        ToastUtil.showToast(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void smsCodeSend(String mobile, String type) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showError("请输入用户名或手机号");
            return;
        }
        mView.refreshSmsCodeUi();
        disposables.add(mCommonApi.smsCodeSend(mobile,type)
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
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ForgetPasswordContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    /**
     * 密码验证
     */
    public static boolean isPassword(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(Constants.PARAMETERS_CHECK_PASSWORD);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
