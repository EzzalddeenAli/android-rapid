package com.lh.rapid.ui.resetpassword;

import android.widget.Button;
import android.widget.EditText;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2017/10/25.
 */

public class ResetPasswordActivity extends BaseActivity implements ResetPasswordContract.View {


    @Inject
    ResetPasswordPresenter mPresenter;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.et_new_password_again)
    EditText mEtNewPasswordAgain;

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
        mActionbar.setTitle("重置密码");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
    }

    @Override
    public void accountChangePasswordSuccess() {
        finish();
    }


    @OnClick(R.id.btn_register)
    public void mBtnRegister() {
        String passwordOld = mEtOldPassword.getText().toString().trim();
        String passwordNew = mEtNewPassword.getText().toString().trim();
        String passwordNewAgain = mEtNewPasswordAgain.getText().toString().trim();
        mPresenter.accountChangePassword(passwordNew, passwordOld,passwordNewAgain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}