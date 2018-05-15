package com.lh.rapid.ui.fragment4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.bean.AccountUserHomeBean;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.aboutme.AboutMeActivity;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.myshare.MyShareActivity;
import com.lh.rapid.ui.orderlist.OrderListActivity;
import com.lh.rapid.ui.servicecenter.ServiceCenterActivity;
import com.lh.rapid.ui.userinfo.UserInfoActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class Fragment4 extends BaseFragment implements Fragment4Contract.View {

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.ll)
    LinearLayout mLl;
    @BindView(R.id.my_order_arrow)
    ImageView mMyOrderArrow;

    @Inject
    Fragment4Presenter mPresenter;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.tv_user_amount)
    TextView mTvUserAmount;
    @BindView(R.id.tv_user_coin)
    TextView mTvUserCoin;

    public static BaseFragment newInstance() {
        Fragment4 fragment4 = new Fragment4();
        return fragment4;
    }

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_4;
    }

    //  1
    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    //  2
    @Override
    public void getBundle(Bundle bundle) {

    }

    //  3
    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        mPresenter.accountInfo();
        mPresenter.accountUserHome();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.rl_address_manager)
    public void mRlAddressManager() {
        Intent intent = new Intent(getActivity(), AddressManagerActivity.class);
        intent.putExtra("comefrom", 2);
        startActivity(intent);
    }

    @OnClick(R.id.my_share)
    public void mMyShare() {
        openActivity(MyShareActivity.class);
    }

    @OnClick(R.id.service_center)
    public void mServiceCenter() {
        openActivity(ServiceCenterActivity.class);
    }

    @OnClick(R.id.about_me)
    public void mAboutMe() {
        openActivity(AboutMeActivity.class);
    }

    @OnClick(R.id.tv_all_order)
    public void mTvAllOrder() {
        openActivity(OrderListActivity.class);
    }

    @Override
    public void accountInfoSuccess(AccountInfoBean accountInfoBean) {

    }

    @Override
    public void accountUserHomeSuccess(AccountUserHomeBean accountUserHomeBean) {
        mTv1.setText(accountUserHomeBean.getNickName());
        mTv2.setText(accountUserHomeBean.getPhone());
        mTvUserAmount.setText("￥" + accountUserHomeBean.getUserAmount());
        mTvUserCoin.setText(accountUserHomeBean.getUserCoin() + "");
        ImageLoaderUtil.getInstance().loadCircleImage(accountUserHomeBean.getAvatarUrl(), R.mipmap.icon_mine_touxiang, mImageView);
    }


    @OnClick(R.id.ll_wait_pay)
    public void mLlWaitPay() {
        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.ll_preparing)
    public void mLlPreparing() {
        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @OnClick(R.id.ll_distribution)
    public void mLlDistribution() {
        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", 3);
        startActivity(intent);
    }

    @OnClick(R.id.ll_finished)
    public void mLlFinished() {
        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", 4);
        startActivity(intent);
    }

    @OnClick(R.id.ll_canc)
    public void mLlCanc() {
        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", 5);
        startActivity(intent);
    }

    @OnClick(R.id.rl_user_info)
    public void mRlUserInfo() {
        openActivity(UserInfoActivity.class);
    }

}