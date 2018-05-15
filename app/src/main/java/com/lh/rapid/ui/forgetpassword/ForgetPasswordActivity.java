package com.lh.rapid.ui.forgetpassword;

import android.widget.Button;
import android.widget.EditText;

import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.main.MainActivity;
import com.lh.rapid.ui.widget.MyActionBar;

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
 * Created by lh on 2017/10/25.
 */
public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View {


    @Inject
    ForgetPasswordPresenter mPresenter;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_validate)
    EditText mEtValidate;
    @BindView(R.id.btn_validate)
    Button mBtnValidate;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_confirm_password)
    EditText mEtConfirmPassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @Override
    public int initContentView() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void initInjector() {
        DaggerForgetPasswordComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("忘记密码");
    }

    @Override
    public void accountPasswordResetSuccess() {
        openActivity(MainActivity.class);
        setResult(Constants.RESULT_REGISTER_CODE);
        finish();
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
                        mBtnValidate.setEnabled(false);
                        mBtnValidate.setClickable(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mBtnValidate.setText("剩余" + aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mBtnValidate.setEnabled(true);
                        mBtnValidate.setClickable(true);
                        mBtnValidate.setText("获取验证码");
                    }
                });
    }

    @OnClick(R.id.btn_validate)
    public void mBtnValidate() {
        String phoneNum = mEtPhone.getText().toString().trim();
        mPresenter.smsCodeSend(phoneNum, Constants.SMSCODE_TYPE_MODIFY_PASSWORD);
    }

    @OnClick(R.id.btn_register)
    public void mBtnRegister() {
        String phone = mEtPhone.getText().toString().trim();
        String validate = mEtValidate.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String passwordConfirm = mEtConfirmPassword.getText().toString().trim();
        mPresenter.accountPasswordReset(phone, validate, password, passwordConfirm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}