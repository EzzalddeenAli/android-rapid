package com.lh.rapid.ui.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.PhoneUtil;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.Constants;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
import com.lh.rapid.components.storage.UserStorage;
import com.squareup.otto.Bus;

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

public class RegisterPresenter implements RegisterContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private RegisterContract.View mView;

    @Inject
    public RegisterPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void register(String phone, String validate, String password, String passwordConfirm) {
        if (!PhoneUtil.isMobile(phone)) {
            mView.showError("手机号码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(validate)) {
            mView.showError("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showError("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            mView.showError("请确认密码");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            mView.showError("两次密码输入不一致");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.register(phone, validate, password)
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
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        mUserStorage.setToken(loginEntity.getToken());
                        if (loginEntity != null) {
                            mView.registerSuccess();
                        } else {
                            ToastUtil.showToast("注册失败，请检查您的网络");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void smsCodeSend(String mobile, int type) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showError("请输入手机号");
            return;
        }
        if (!PhoneUtil.isMobile(mobile)) {
            mView.showError("手机号码格式不正确");
            return;
        }
        mView.refreshSmsCodeUi();
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
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull RegisterContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }


    /**
     * 用户名验证
     */
    public static boolean isUsername(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(Constants.PARAMETERS_CHECK_LOGIN_NAME);
        m = p.matcher(str);
        b = m.matches();
        return b;
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
