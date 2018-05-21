package com.lh.rapid.ui.orderconfirm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.android.frameproj.library.dialog.InfoMsgHint;
import com.android.frameproj.library.util.TimeUtils;
import com.android.frameproj.library.util.ToastUtil;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.bean.DictionaryBean;
import com.lh.rapid.bean.OrderSubmitBean;
import com.lh.rapid.bean.OrderSubmitConfirmBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;
import com.lh.rapid.ui.coupon.CouponActivity;
import com.lh.rapid.ui.orderpay.OrderPayActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderConfirmActivity extends BaseActivity implements OrderConfirmContract.View {

    @Inject
    OrderConfirmPresenter mPresenter;

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.tv_commit)
    TextView mTvCommit;
    @BindView(R.id.tv_choose_coupon)
    TextView mTvChooseCoupon;
    @BindView(R.id.tv_distribution_fee)
    TextView mTvDistributionFee;
    @BindView(R.id.rl_distribution_fee)
    RelativeLayout mRlDistributionFee;

    private int mAddressId = -1;
    private String mParams;
    private CartGoodsBean mCartGoodsBean;
    private int mCouponId = -1;

    private String sendDate;
    private String sendTime;
    @Inject
    SPUtil mSPUtil;

    private boolean initTime = false;

    @Override
    public int initContentView() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public void initInjector() {
        DaggerOrderConfirmComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mParams = getIntent().getStringExtra("params");
        String cartGoodsBeanString = getIntent().getStringExtra("cartGoodsBean");
        mCartGoodsBean = new Gson().fromJson(cartGoodsBeanString, CartGoodsBean.class);
        mPresenter.addressDefault();
        mPresenter.commonDictionaryQuery();
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("填写订单");

        initRecyclerView();
        initChooseTime();
    }

    private void initChooseTime() {
        String dictionary_data = mSPUtil.getDICTIONARY_DATA();
        if (!TextUtils.isEmpty(dictionary_data)) {
            Type listType = new TypeToken<List<DictionaryBean>>() {
            }.getType();
            List<DictionaryBean> dictionaryBeans = new Gson().fromJson(dictionary_data, listType);
            String currentTime = TimeUtils.getCurrentTimeMillis();
            for (int i = 1; i < 8; i++) {
                String otherTime = (Long.valueOf(currentTime) + i * 60 * 60 * 24) + "";
                String dayTime = TimeUtils.getYearAndDay(otherTime);
                options1Items.add(dayTime);
                List<String> stringList = new ArrayList<>();
                for (int j = 0; j < dictionaryBeans.size(); j++) {
                    stringList.add(dictionaryBeans.get(j).getDictionaryValue());
                }
                options2Items.add(stringList);
            }
            initTime = true;
        } else {
            mPresenter.commonDictionaryQuery();
        }

    }

    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();

    @OnClick(R.id.rl_send_time)
    public void mRlSendTime() {
        if (initTime) {
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String data = options1Items.get(options1) + "  " + options2Items.get(options1).get(option2);
                    mTvEndTime.setText(data);
                    sendDate = options1Items.get(options1);
                    sendTime = options2Items.get(options1).get(option2);
                }
            }).setTitleText("送货时间选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .setOutSideCancelable(false)// default is true
                    .build();
            pvOptions.setPicker(options1Items, options2Items);
            pvOptions.show();
        } else {
            ToastUtil.showToast("初始化送货时间请稍后");
            mPresenter.commonDictionaryQuery();
        }
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(OrderConfirmActivity.this));
        CommonAdapter commonAdapter = new CommonAdapter<CartGoodsBean.GoodsListsBean>(OrderConfirmActivity.this, R.layout.layout_order_detail_item, mCartGoodsBean.getGoodsLists()) {

            @Override
            protected void convert(ViewHolder holder, final CartGoodsBean.GoodsListsBean goodsListBean, int position) {
                holder.setImageUrl(R.id.iv_product, goodsListBean.getGoodsImgUrl());
                holder.setText(R.id.tv_product_name, goodsListBean.getGoodsName());
                holder.setText(R.id.tv_product_price, "￥" + goodsListBean.getPrice());
                holder.setText(R.id.tv_product_count, "× " + goodsListBean.getQuantity());
                holder.setText(R.id.tv_product_guige, "规格： " + goodsListBean.getQuantity());
            }

        };
        mRecyclerView.setAdapter(commonAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(OrderConfirmActivity.this, 2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void orderSubmitConfirmSuccess(OrderSubmitConfirmBean orderSubmitConfirmBean) {
        int flgRangeIn = orderSubmitConfirmBean.getFlgRangeIn(); // 0:不在配送范围内   1:在配送范围内
        double price = orderSubmitConfirmBean.getPrice();
        mTvOrderPrice.setText("￥" + price);
        double expressAmount = orderSubmitConfirmBean.getExpressAmount();
        mTvDistributionFee.setText("￥" + expressAmount);
        if (flgRangeIn == 0) {
            InfoMsgHint infoMsgHint = new InfoMsgHint(OrderConfirmActivity.this);
            infoMsgHint.setOKListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderConfirmActivity.this, AddressManagerActivity.class);
                    intent.putExtra("comefrom", 1);
                    startActivityForResult(intent, Constants.REQUEST_LOCATION_MANAGER_CODE);
                }
            });
            infoMsgHint.setCancleListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            infoMsgHint.setContent("您选择的送货地址不在配送范围内，请重新选择地址或按当前送货地址重新选择商品！",
                    "", "重选地址", "重选商品");
            infoMsgHint.show();
        }
    }

    @Override
    public void orderSubmitSuccess(OrderSubmitBean orderSubmitBean) {
        int orderId = orderSubmitBean.getOrderId();
        double price = orderSubmitBean.getPrice();
        Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("price", price);
        startActivity(intent);
    }

    @Override
    public void addressDefaultSuccess(AddressListBean addressListBean) {
        if (addressListBean != null) {
            mTvName.setText(addressListBean.getReceiveGoodsName());
            mTvPhone.setText(addressListBean.getPhone());
            mTvAddress.setText(addressListBean.getDetailAddress());
            mAddressId = addressListBean.getAddressId();
            mPresenter.orderSubmitConfirm(mAddressId + "", mSPUtil.getCIRCLE_ID() + "", mParams);
        }
    }

    @Override
    public void commonDictionaryQuerySuccess(List<DictionaryBean> dictionaryBeanList) {
        if (dictionaryBeanList != null && dictionaryBeanList.size() > 0) {
            mSPUtil.setDICTIONARY_DATA(new Gson().toJson(dictionaryBeanList));
            options1Items.clear();
            options2Items.clear();
            initChooseTime();
        } else {
            ToastUtil.showToast("获取送货时间失败");
        }
    }

    @OnClick(R.id.tv_commit)
    public void mTvCommit() {
        mPresenter.orderSubmit(mAddressId + "", mSPUtil.getCIRCLE_ID() + "", mCouponId + "",
                sendDate, sendTime, mParams);
    }


    @OnClick(R.id.rl_receive_address)
    public void mRlReceiveAddress() {
        Intent intent = new Intent(OrderConfirmActivity.this, AddressManagerActivity.class);
        intent.putExtra("comefrom", 1);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_MANAGER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOCATION_MANAGER_CODE && resultCode == Constants.RESULT_LOCATION_MANAGER_CODE) {
            String addressListBeanString = data.getStringExtra("addressListBean");
            AddressListBean addressListBean = new Gson().fromJson(addressListBeanString, AddressListBean.class);
            mPresenter.orderSubmitConfirm(addressListBean.getAddressId() + "", mSPUtil.getCIRCLE_ID() + "", mParams);
            mTvName.setText(addressListBean.getReceiveGoodsName());
            mTvPhone.setText(addressListBean.getPhone());
            mTvAddress.setText(addressListBean.getDetailAddress());
            mAddressId = addressListBean.getAddressId();
        } else if (requestCode == Constants.REQUEST_COUPON_CODE && resultCode == Constants.RESULT_COUPON_CODE) {
            mCouponId = data.getIntExtra("couponId", -1);
            String couponName = data.getStringExtra("couponName");
            double fullMoney = data.getDoubleExtra("fullMoney", -1);
            double saveMoney = data.getDoubleExtra("saveMoney", -1);
            mTvChooseCoupon.setText(couponName);
        }
    }

    @OnClick(R.id.rl_choose_coupon)
    public void mRlChooseCoupon() {
        Intent intent = new Intent(OrderConfirmActivity.this, CouponActivity.class);
        intent.putExtra("comefrom", 1);
        startActivityForResult(intent, Constants.REQUEST_COUPON_CODE);
    }

}
