package com.lh.rapid.ui.usercenter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/5/2.
 */

public class UserCenterActivity extends BaseActivity {

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


    @Override
    public int initContentView() {
        return R.layout.activity_user_center;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {

    }

    @OnClick(R.id.rl_address_manager)
    public void mRlAddressManager(){
        openActivity(AddressManagerActivity.class);
    }

    @OnClick(R.id.my_share)
    public void mMyShare(){

    }

    @OnClick(R.id.service_center)
    public void mServiceCenter(){

    }

    @OnClick(R.id.about_me)
    public void mAboutMe(){

    }

}
