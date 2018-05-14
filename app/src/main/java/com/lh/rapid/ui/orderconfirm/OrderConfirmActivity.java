package com.lh.rapid.ui.orderconfirm;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.google.gson.Gson;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.bean.OrderSubmitBean;
import com.lh.rapid.bean.OrderSubmitConfirmBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.addressmanager.AddressManagerActivity;
import com.lh.rapid.ui.orderpay.OrderPayActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderConfirmActivity extends BaseActivity implements OrderConfirmContract.View {

    @Inject
    OrderConfirmPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
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

    private int mAddressId;
    private String mParams;
    private CartGoodsBean mCartGoodsBean;

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
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("填写订单");

        initRecyclerView();
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

    @OnClick(R.id.tv_commit)
    public void mTvCommit() {
        mPresenter.orderSubmit(mAddressId + "", mSPUtil.getCIRCLE_ID() + "", mParams);
    }


    @OnClick(R.id.rl_receive_address)
    public void mRlReceiveAddress() {
        Intent intent = new Intent(OrderConfirmActivity.this, AddressManagerActivity.class);
        intent.putExtra("comefrom", 1);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_MANAGER_CODE);
    }

    @OnClick(R.id.rl_send_time)
    public void mRlSendTime() {

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
        }
    }

}
