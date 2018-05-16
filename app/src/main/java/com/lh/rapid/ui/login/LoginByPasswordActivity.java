package com.lh.rapid.ui.login;

import android.view.View;
import android.widget.EditText;

import com.lh.rapid.R;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.forgetpassword.ForgetPasswordActivity;
import com.lh.rapid.ui.main.MainActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/4/25.
 */

public class LoginByPasswordActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.et_login_phone_num)
    EditText mEtLoginPhoneNum;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;


    @Override
    public int initContentView() {
        return R.layout.activity_password_login;
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

        mActionbar.setLeftVisible(View.GONE);
        mActionbar.setTitle("用户登录");
        mEtLoginPhoneNum.setText("15300936554");
        mEtLoginPassword.setText("1");
    }

    @Override
    public void loginSuccess() {
        openActivity(MainActivity.class);
    }

    @Override
    public void refreshSmsCodeUi() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.bt_login_sure)
    public void mBtLoginSure() {
        String mUserName = mEtLoginPhoneNum.getText().toString().trim();
        String mPassword = mEtLoginPassword.getText().toString().trim();
        mPresenter.login(mUserName, mPassword,1);
    }

    @OnClick(R.id.tv_login_forget)
    public void mTvLoginForget(){
        openActivity(ForgetPasswordActivity.class);
    }

}