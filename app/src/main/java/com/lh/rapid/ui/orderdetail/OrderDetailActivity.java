package com.lh.rapid.ui.orderdetail;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.OrderDetailBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/3.
 */
public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_order_no)
    TextView mTvOrderNo;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.statefulLayout)
    StatefulLayout mStatefulLayout;

    @Inject
    OrderDetailPresenter mPresenter;
    private int mOrderId;

    @Override
    public int initContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initInjector() {
        DaggerOrderDetailComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mOrderId = getIntent().getIntExtra("orderId", -1);
        mPresenter.orderDetail(mOrderId);

        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("订单详情");
    }

    @Override
    public void showLoading() {
        mStatefulLayout.showLoading();
    }

    @Override
    public void hideLoading(int type) {
        if (type == -1) {
            StatusfulConfig statusfulConfig = new StatusfulConfig.Builder()
                    .setOnErrorStateButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.orderDetail(mOrderId);
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
        } else if (type == 0) {
            mStatefulLayout.showContent();
        }
    }

    @Override
    public void orderDetailSuccess(OrderDetailBean orderDetailBean) {
        mTvName.setText(orderDetailBean.getReceiveName());
        mTvPhone.setText(orderDetailBean.getReceiveMobile());
        mTvAddress.setText(orderDetailBean.getReceiveAddress());
        mTvOrderNo.setText(orderDetailBean.getOrderNo());
        mTvStartTime.setText(orderDetailBean.getCreateDate());
        mTvEndTime.setText(orderDetailBean.getCreateDate());
        mTvOrderPrice.setText("￥"+orderDetailBean.getAmountTotal());

        // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
        String orderStatus = "";
        switch (orderDetailBean.getStatus()){
            case 1:
                orderStatus = "待付款";
                break;
            case 2:
                orderStatus = "准备中";
                break;
            case 3:
                orderStatus = "配送中";
                break;
            case 4:
                orderStatus = "已完成";
                break;
            case 5:
                orderStatus = "已取消";
                break;
        }
        mTvStatus.setText(orderStatus);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<OrderDetailBean.GoodsListBean> goodsListBeenList = new ArrayList<>();
        goodsListBeenList.addAll(orderDetailBean.getGoodsList());
        CommonAdapter commonAdapter = new CommonAdapter<OrderDetailBean.GoodsListBean>(OrderDetailActivity.this, R.layout.layout_order_detail_item, goodsListBeenList) {

            @Override
            protected void convert(ViewHolder holder, OrderDetailBean.GoodsListBean goodsListBean, int position) {
                holder.setImageUrl(R.id.iv_product, goodsListBean.getGoodsImg());
                holder.setText(R.id.tv_product_name, goodsListBean.getGoodsName());
                holder.setText(R.id.tv_product_price, "￥" + goodsListBean.getPrice());
                holder.setText(R.id.tv_product_count, "× " + goodsListBean.getCounts());
                holder.setText(R.id.tv_product_guige, "规格： " + goodsListBean.getCounts());
            }
        };
        mRecyclerView.setAdapter(commonAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(OrderDetailActivity.this, 2));
    }

    @Override
    public void orderFinishSuccess(String s) {
        ToastUtil.showToast(s);
    }

    @Override
    public void orderCancelSuccess(String s) {
        ToastUtil.showToast(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
