package com.lh.rapid.ui.coupon;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.lh.rapid.R;
import com.lh.rapid.bean.UserCouponsBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/2.
 */

public class CouponActivity extends BaseActivity implements CouponContract.View {

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
    CouponPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;

    @Override
    public int initContentView() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initInjector() {
        DaggerCouponComponent.builder()
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
        mActionbar.setTitle("我的优惠券");

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.loadDate();

            }
        });
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRefreshCompleted(List<UserCouponsBean> goodsDetailBeanList) {
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<UserCouponsBean>(CouponActivity.this, R.layout.layout_coupon, goodsDetailBeanList) {
                @Override
                protected void convert(ViewHolder holder, final UserCouponsBean userCouponsBean, int position) {

                }
            };
            mLinearLayoutManager = new LinearLayoutManager(CouponActivity.this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(CouponActivity.this, 1));
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mRecyclerView.isComputingLayout() == false)) {
                mCommonAdapter.notifyDataSetChanged();
            }
        }
    }

}
