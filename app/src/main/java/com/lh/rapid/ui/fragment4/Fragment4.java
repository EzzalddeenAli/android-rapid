package com.lh.rapid.ui.fragment4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.interf.CallbackChangeFragment;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.bean.AccountUserHomeBean;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.aboutme.AboutMeActivity;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;
import com.lh.rapid.ui.coupon.CouponActivity;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.myshare.MyShareActivity;
import com.lh.rapid.ui.orderlist.OrderListActivity;
import com.lh.rapid.ui.recharge.RechargeActivity;
import com.lh.rapid.ui.setting.SettingActivity;
import com.lh.rapid.ui.userinfo.UserInfoActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

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


    @OnClick(R.id.tv_recharge)
    public void mTvRecharge(){
        openActivity(RechargeActivity.class);
    }

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void initData() {
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!TextUtils.isEmpty(mUserStorage.getToken())) {
            mPresenter.accountUserHome();
        } else {
            mCallbackChangeFragment.changeFragment(0);
        }
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
        openActivity(CouponActivity.class);
    }

    @OnClick(R.id.service_center)
    public void mServiceCenter() {
        //        openActivity(ServiceCenterActivity.class);
        new MaterialDialog.Builder(getActivity())
                .content("拨打客服电话：400-8917517")
                .positiveText("拨打")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:4008917517"));
                        startActivity(intent);
                    }
                })
                .show();
    }

    @OnClick(R.id.about_me)
    public void mAboutMe() {
        Intent intent = new Intent(getActivity(), AboutMeActivity.class);
        startActivity(intent);
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

    @OnClick(R.id.iv_mine_shezhi)
    public void mIvMineShezhi() {
        openActivity(SettingActivity.class);
    }

    // 切换到主页面
    private CallbackChangeFragment mCallbackChangeFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbackChangeFragment = (CallbackChangeFragment) context;
    }

}