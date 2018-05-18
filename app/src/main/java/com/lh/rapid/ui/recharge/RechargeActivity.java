package com.lh.rapid.ui.recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lh on 2018/4/25.
 */

public class RechargeActivity extends BaseActivity implements RechargeContract.View {

    @Inject
    RechargePresenter mPresenter;

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.et_recharge_money)
    EditText mEtRechargeMoney;
    @BindView(R.id.cb_alipay)
    CheckBox mCbAlipay;
    @BindView(R.id.cb_wechat)
    CheckBox mCbWechat;


    @OnClick(R.id.rl_alipay)
    public void mRlAlipay() {
        if (mCbAlipay.isChecked()) {
            mCbAlipay.setChecked(false);
        } else {
            mCbAlipay.setChecked(true);
            mCbWechat.setChecked(false);
        }
    }

    @OnClick(R.id.rl_wechat)
    public void mRlWechat() {
        if (mCbWechat.isChecked()) {
            mCbWechat.setChecked(false);
        } else {
            mCbAlipay.setChecked(false);
            mCbWechat.setChecked(true);
        }
    }

    @Override
    public int initContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initInjector() {
        DaggerRechargeComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mActionbar.setLeftVisible(View.GONE);
        mActionbar.setTitle("用户充值");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void rechargeSuccess() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}