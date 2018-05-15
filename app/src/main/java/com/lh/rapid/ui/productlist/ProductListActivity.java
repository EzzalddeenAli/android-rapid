package com.lh.rapid.ui.productlist;

import android.content.Intent;
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
import com.lh.rapid.bean.ProductListBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.productdetail.ProductDetailActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/2.
 */

public class ProductListActivity extends BaseActivity implements ProductListContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;

    @Inject
    ProductListPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
    private int mCategoryId;
    private int mCircleId;
    private List<ProductListBean> mGoodsDetailBeans = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_product_list;
    }

    @Override
    public void initInjector() {
        DaggerProductListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {

        mCategoryId = getIntent().getIntExtra("categoryId", -1);
        mCircleId = mSPUtil.getCIRCLE_ID();
        mPresenter.attachView(this);
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("商品列表");

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.onRefresh(null, null, null, null, mCategoryId + "", mCircleId + "");

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
    public void onRefreshCompleted(List<ProductListBean> goodsDetailBeanList, boolean isClear) {
        if (isClear) {
            mRefreshLayout.finishRefresh(300);
            mGoodsDetailBeans.clear();
        } else {
            mRefreshLayout.finishLoadMore(300);
        }
        mGoodsDetailBeans.addAll(goodsDetailBeanList);
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ProductListBean>(ProductListActivity.this, R.layout.layout_product_list, mGoodsDetailBeans) {
                @Override
                protected void convert(ViewHolder holder, final ProductListBean goodsDetailBean, int position) {
                    holder.setImageUrl(R.id.iv_cart_item_pic,goodsDetailBean.getGoodsImg());
                    holder.setText(R.id.tv_cart_item_name, goodsDetailBean.getGoodsName());
                    holder.setText(R.id.tv_cart_item_weight, goodsDetailBean.getWeight());
                    holder.setText(R.id.tv_cart_item_price, "￥"+goodsDetailBean.getGoodsPrice());
                    holder.getView(R.id.sn_cart_item).setVisibility(View.GONE);
                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                    intent.putExtra("goodsId", mGoodsDetailBeans.get(position).getGoodsId());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mLinearLayoutManager = new LinearLayoutManager(ProductListActivity.this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(ProductListActivity.this, 1));
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
