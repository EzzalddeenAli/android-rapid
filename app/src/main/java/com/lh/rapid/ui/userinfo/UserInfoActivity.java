package com.lh.rapid.ui.userinfo;

import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.resetpassword.ResetPasswordActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/15.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoContract.View {

    @Inject
    UserInfoPresenter mPresenter;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_card_num)
    TextView mTvCardNum;
    @BindView(R.id.tv_name_prefix)
    TextView mTvNamePrefix;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rl_name)
    RelativeLayout mRlName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout mRlSex;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.rl_email)
    RelativeLayout mRlEmail;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout mRlBirthday;

    @Override
    public int initContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initInjector() {
        DaggerUserInfoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mPresenter.loadDate();
        mActionbar.setTitle("会员信息");
        mActionbar.setRightText("修改密码");
        mActionbar.setRightTvClickLitener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                Intent intent = new Intent(UserInfoActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onLoadDateCompleted(AccountInfoBean accountInfoBean) {
        mTvName.setText(accountInfoBean.getNickName());
        mTvPhone.setText(accountInfoBean.getPhone());
        mTvCardNum.setText(accountInfoBean.getCardId());
        mTvEmail.setText(accountInfoBean.getEmail());
        mTvBirthday.setText(accountInfoBean.getBirthday());
    }

}