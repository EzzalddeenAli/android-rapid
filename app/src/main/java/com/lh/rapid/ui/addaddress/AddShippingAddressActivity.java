package com.lh.rapid.ui.addaddress;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.InputUtil;
import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.location.ChooseLocationActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import java.lang.reflect.Type;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2017/10/16.
 */
public class AddShippingAddressActivity extends BaseActivity implements AddShippingAddressContract.View {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.rl_name)
    RelativeLayout mRlName;
    @BindView(R.id.text_phone)
    TextView mTextPhone;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout mRlPhone;
    @BindView(R.id.text_address)
    TextView mTextAddress;
    @BindView(R.id.edit_address)
    TextView mEditAddress;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.text_house_number)
    TextView mTextHouseNumber;
    @BindView(R.id.edit_house_number)
    EditText mEditHouseNumber;
    @BindView(R.id.rl_house_number)
    RelativeLayout mRlHouseNumber;
    @BindView(R.id.btn_save_address)
    Button mBtnSaveAddress;
    private double longitude = -1;
    private double latitude = -1;

    @Inject
    AddShippingAddressPresenter mPresenter;
    private int mAddressId;
    private String mJsonAddress;

    @Override
    public int initContentView() {
        return R.layout.activity_add_shipping_address;
    }

    @Override
    public void initInjector() {
        DaggerAddShippingAddressComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("新增收货地址");

        mAddressId = getIntent().getIntExtra("addressId", -1);
        mJsonAddress = getIntent().getStringExtra("jsonAddress");
        if (mJsonAddress != null && !TextUtils.isEmpty(mJsonAddress)) {
            Type listType = new TypeToken<AddressListBean>() {
            }.getType();
            AddressListBean addressListBean = new Gson().fromJson(mJsonAddress, listType);
            mEditName.setText(addressListBean.getReceiveGoodsName());
            mEditPhone.setText(addressListBean.getPhone());
            mEditAddress.setText(addressListBean.getDetailAddress());
            mEditHouseNumber.setText(addressListBean.getArea());
        }

    }

    /**
     * 保存地址
     */
    @OnClick(R.id.btn_save_address)
    public void mBtnSaveAddress() {
        String name = mEditName.getText().toString().trim();
        String phone = mEditPhone.getText().toString().trim();
        String address = mEditAddress.getText().toString().trim();
        String houseNumber = mEditHouseNumber.getText().toString().trim();
        mPresenter.updateShippingAddress(mAddressId == -1 ? null : mAddressId + "", name, phone, houseNumber, address, longitude + "", latitude + "");
    }

    @Override
    public void updateShippingAddressSuccess(String s) {
        ToastUtil.showToast(s);
        InputUtil.hideSoftInput(AddShippingAddressActivity.this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onBackPressedSupport() {
        InputUtil.hideSoftInput(AddShippingAddressActivity.this);
        super.onBackPressedSupport();
    }

    @OnClick(R.id.rl_address)
    public void mRlAddress() {
        Intent intent = new Intent(AddShippingAddressActivity.this, ChooseLocationActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CHOOSE_LOCATION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CHOOSE_LOCATION_CODE && resultCode == Constants.RESULT_CHOOSE_LOCATION_CODE) {
            longitude = data.getDoubleExtra("longitude", -1);
            latitude = data.getDoubleExtra("latitude", -1);
            //            String name = data.getStringExtra("name");
            String addr = data.getStringExtra("addr");
            mEditAddress.setText(addr);
        }
    }
    
}
