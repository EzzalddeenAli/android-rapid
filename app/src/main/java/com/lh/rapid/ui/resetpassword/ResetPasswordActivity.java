package com.lh.rapid.ui.resetpassword;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.main.MainActivity;

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

public class ResetPasswordActivity extends BaseActivity implements ResetPasswordContract.View {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
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

    @Inject
    ResetPasswordPresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void initInjector() {
        DaggerResetPasswordComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mTvTitle.setText("忘记密码");
        mRlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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