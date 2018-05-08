package com.lh.rapid.ui.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.main.MainActivity;
import com.lh.rapid.ui.register.RegisterActivity;
import com.lh.rapid.util.SPUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lh on 2018/4/25.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.et_login_phone_num)
    EditText mEtLoginPhoneNum;
    @BindView(R.id.et_login_code_num)
    EditText mEtLoginCodeNum;
    @BindView(R.id.bt_login_get_code)
    Button mBtLoginGetCode;
    @BindView(R.id.bt_login_sure)
    Button mBtLoginSure;
    @BindView(R.id.tv_other_login)
    TextView mTvOtherLogin;
    @BindView(R.id.view_center)
    View mViewCenter;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initInjector() {
        DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mSPUtil.setIS_LOGIN(0);
        mSPUtil.setTOKNE("");
        mUserStorage.setToken("");
    }

    @Override
    public void loginSuccess() {
        openActivity(MainActivity.class);
    }

    @Override
    public void refreshSmsCodeUi() {
        int time = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return 60 - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mBtLoginGetCode.setEnabled(false);
                        mBtLoginGetCode.setClickable(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mBtLoginGetCode.setText("剩余" + aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mBtLoginGetCode.setEnabled(true);
                        mBtLoginGetCode.setClickable(true);
                        mBtLoginGetCode.setText("获取验证码");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.bt_login_sure)
    public void mBtLoginSure() {
        String mUserName = mEtLoginPhoneNum.getText().toString().trim();
        String mCode = mEtLoginCodeNum.getText().toString().trim();
        mPresenter.loginMobile(mUserName, mCode);
    }

    @OnClick(R.id.tv_login_new_register)
    public void mTvLoginNewRegister() {
        openActivity(RegisterActivity.class);
    }

    @OnClick(R.id.bt_login_get_code)
    public void mBtLoginGetCode(){
        String phoneNum = mEtLoginPhoneNum.getText().toString().trim();
        mPresenter.smsCodeSend(phoneNum, Constants.SMSCODE_TYPE_LOGIN);
    }

    @OnClick(R.id.ll_password_login)
    public void mLlPasswordLogin(){
        openActivity(LoginByPasswordActivity.class);
    }

    @OnClick(R.id.ll_cardnum_login)
    public void mLlCardnumLogin(){
        openActivity(LoginByCardActivity.class);
    }

}