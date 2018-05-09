package com.lh.rapid.ui.fragment4;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.myshare.MyShareActivity;

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
    public void onError(Throwable throwable) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.rl_address_manager)
    public void mRlAddressManager(){
        openActivity(AddressManagerActivity.class);
    }

    @OnClick(R.id.my_share)
    public void mMyShare(){
        openActivity(MyShareActivity.class);
    }

    @OnClick(R.id.service_center)
    public void mServiceCenter(){

    }

    @OnClick(R.id.about_me)
    public void mAboutMe(){

    }

}