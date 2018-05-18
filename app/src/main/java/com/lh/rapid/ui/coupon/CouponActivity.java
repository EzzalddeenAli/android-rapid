package com.lh.rapid.ui.coupon;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.lh.rapid.Constants;
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
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;

    @Inject
    CouponPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
    private int mComefrom;
    private int currentStatus = 0;

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
        mComefrom = getIntent().getIntExtra("comefrom", -1);
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("我的优惠券");

        mTabLayout.addTab(mTabLayout.newTab().setText("未使用"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已使用"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已过期"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentStatus = tab.getPosition();
                mRefreshLayout.autoRefresh();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                currentStatus = tab.getPosition();
                mRefreshLayout.autoRefresh();
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.loadDate(currentStatus + "");

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
    public void loadError(Throwable throwable) {
        super.loadError(throwable);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onRefreshCompleted(final List<UserCouponsBean> goodsDetailBeanList) {
        mRefreshLayout.finishRefresh();
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<UserCouponsBean>(CouponActivity.this, R.layout.layout_coupon, goodsDetailBeanList) {
                @Override
                protected void convert(ViewHolder holder, final UserCouponsBean userCouponsBean, int position) {
                    holder.setText(R.id.tv_name, userCouponsBean.getCouponName());
                    holder.setText(R.id.tv_time, userCouponsBean.getExpressTime());
                }
            };
            if (mComefrom == 1) {
                mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        UserCouponsBean userCouponsBean = goodsDetailBeanList.get(position);
                        Intent intent = new Intent();
                        intent.putExtra("couponId", userCouponsBean.getCouponId());
                        intent.putExtra("couponName", userCouponsBean.getCouponName());
                        intent.putExtra("fullMoney", userCouponsBean.getFullMoney());
                        intent.putExtra("saveMoney", userCouponsBean.getSaveMoney());
                        setResult(Constants.RESULT_COUPON_CODE, intent);
                        finish();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
            }
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
