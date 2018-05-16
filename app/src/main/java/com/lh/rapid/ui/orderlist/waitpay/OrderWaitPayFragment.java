package com.lh.rapid.ui.orderlist.waitpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.lh.rapid.R;
import com.lh.rapid.bean.OrderBean;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.orderdetail.OrderDetailActivity;
import com.lh.rapid.ui.orderlist.OrderListComponent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderWaitPayFragment extends BaseFragment implements OrderWaitPayContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;
    private List<OrderBean> mOrderBeanList = new ArrayList<>();

    @Inject
    OrderWaitPayPresenter mPresenter;

    public static BaseFragment newInstance() {
        OrderWaitPayFragment OrderWaitPayFragment = new OrderWaitPayFragment();
        return OrderWaitPayFragment;
    }

    @Override
    public void initInjector() {
        getComponent(OrderListComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_list;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.onRefresh(1);

            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mPresenter.onLoadMore();
            }
        });
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRefreshCompleted(List<OrderBean> orderBeanList, boolean isClear) {
        if (isClear) {
            mRefreshLayout.finishRefresh(300);
            mOrderBeanList.clear();
        } else {
            mRefreshLayout.finishLoadMore(300);
        }
        mOrderBeanList.addAll(orderBeanList);
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<OrderBean>(getActivity(), R.layout.layout_order_list, mOrderBeanList) {
                @Override
                protected void convert(ViewHolder holder, final OrderBean orderBean, int position) {
                    holder.setText(R.id.tv_order_time, orderBean.getCreateDate());
                    holder.setText(R.id.tv_order_no, orderBean.getOrderNo());
                    holder.setText(R.id.tv_sum_price, "￥"+orderBean.getTotalPrice());

                    // 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
                    String orderStatus = "";
                    switch (orderBean.getStatus()){
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
                    holder.setText(R.id.tv_state, orderStatus);

                    RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    CommonAdapter commonAdapter = new CommonAdapter<OrderBean.GoodsListBean>(getActivity(), R.layout.layout_order_detail_item, orderBean.getGoodsList()) {

                        @Override
                        protected void convert(ViewHolder holder, final OrderBean.GoodsListBean goodsListBean, int position) {
                            holder.setImageUrl(R.id.iv_product, goodsListBean.getGoodsImg());
                            holder.setText(R.id.tv_product_name, goodsListBean.getGoodsName());
                            holder.setText(R.id.tv_product_price, "￥" + goodsListBean.getPrice());
                            holder.setText(R.id.tv_product_count, "× " + goodsListBean.getCounts());
                            holder.setText(R.id.tv_product_guige, "规格： " + goodsListBean.getCounts());
                        }

                    };
                    recyclerView.setAdapter(commonAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), 2));
                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    OrderBean orderBean = mOrderBeanList.get(position);
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("orderId", orderBean.getOrderId());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), 1));
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mRecyclerView.isComputingLayout() == false)) {
                mCommonAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void enableLoadMore(boolean isLoadAll) {
        mRefreshLayout.setEnableLoadMore(isLoadAll);
    }

    @Override
    public void onEmpty() {
        mRefreshLayout.finishRefresh(300);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
