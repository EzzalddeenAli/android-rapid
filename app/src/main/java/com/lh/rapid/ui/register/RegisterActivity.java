package com.lh.rapid.ui.register;

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
 * Created by lh on 2017/9/28.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @Inject
    RegisterPresenter mPresenter;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.et_register_phone_num)
    EditText mEtRegisterPhoneNum;
    @BindView(R.id.et_register_code_num)
    EditText mEtRegisterCodeNum;
    @BindView(R.id.et_register_password)
    EditText mEtRegisterPassword;
    @BindView(R.id.et_register_password_two)
    EditText mEtConfirmPassword;
    @BindView(R.id.bt_register_get_code)
    Button mBtRegisterGetCode;

    @Override
    public int initContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initInjector() {
        DaggerRegisterComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mActionbar.setTitle("用户注册");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
    }

    @Override
    public void registerSuccess() {
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
                        mBtRegisterGetCode.setEnabled(false);
                        mBtRegisterGetCode.setClickable(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mBtRegisterGetCode.setText("剩余" + aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mBtRegisterGetCode.setEnabled(true);
                        mBtRegisterGetCode.setClickable(true);
                        mBtRegisterGetCode.setText("获取验证码");
                    }
                });
    }

    @OnClick(R.id.bt_register_get_code)
    public void mBtRegisterGetCode() {
        String phoneNum = mEtRegisterPhoneNum.getText().toString().trim();
        mPresenter.smsCodeSend(phoneNum, Constants.SMSCODE_TYPE_REGISTER);
    }

    @OnClick(R.id.bt_register_sure)
    public void mBtRegisterSure() {
        String phone = mEtRegisterPhoneNum.getText().toString().trim();
        String validate = mEtRegisterCodeNum.getText().toString().trim();
        String password = mEtRegisterPassword.getText().toString().trim();
        String passwordConfirm = mEtConfirmPassword.getText().toString().trim();
        mPresenter.register(phone, validate, password, passwordConfirm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}