package com.lh.rapid.ui.login;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.widget.ShapeEditText;
import com.lh.rapid.R;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.main.MainActivity;
import com.lh.rapid.ui.register.RegisterActivity;
import com.lh.rapid.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/4/25.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.et_username)
    ShapeEditText mEtUsername;
    @BindView(R.id.et_password)
    ShapeEditText mEtPassword;
    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;

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
        mTvTitle.setText("用户登录");
        mRlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSPUtil.setIS_LOGIN(0);
        mSPUtil.setTOKNE("");
        mUserStorage.setToken("");
    }

    @Override
    public void loginSuccess() {
        openActivity(MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.btn_login)
    public void mBtnLogin() {
        String mUserName = mEtUsername.getText().toString().trim();
        String mPassword = mEtPassword.getText().toString().trim();
        mPresenter.login(mUserName, mPassword);
        openActivity(MainActivity.class);
    }

    @OnClick(R.id.btn_register)
    public void mBtnRegister() {
        openActivity(RegisterActivity.class);
    }

}
