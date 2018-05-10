package com.lh.rapid.ui.addressmanager;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.frameproj.library.decoration.RecyclerViewDivider;
import com.google.gson.Gson;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.addaddress.AddShippingAddressActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/5/7.
 */

public class AddressManagerActivity extends BaseActivity implements AddressManagerContract.View {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.rv_manage_address)
    RecyclerView mRvManageAddress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private ManagerSiteAdapter adapter;

    @Inject
    AddressManagerPresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_address_manager;
    }

    @Override
    public void initInjector() {
        DaggerAddressManagerComponent.builder()
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
        mActionbar.setTitle("地址管理");

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.loadDate();
            }
        });
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadDateCompleted(final List<AddressListBean> addressListBeans) {
        adapter = new ManagerSiteAdapter(this, addressListBeans);
        adapter.setChangeListener(new ManagerSiteAdapter.itemAddressChangeListener() {
            @Override
            public void changeListener(int position) {
                AddressListBean addressListBean = addressListBeans.get(position);
                Intent intent = new Intent(AddressManagerActivity.this, AddShippingAddressActivity.class);
                intent.putExtra("addressId", addressListBean.getAddressId());
                String jsonAddress = new Gson().toJson(addressListBean);
                intent.putExtra("jsonAddress", jsonAddress);
                startActivity(intent);
            }
        });
        mRvManageAddress.setLayoutManager(new LinearLayoutManager(this));
        mRvManageAddress.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.VERTICAL, 2, R.color.line));
        mRvManageAddress.setAdapter(adapter);

        mRefreshLayout.finishRefresh(300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.ll_manage_address)
    public void mLlManageAddress() {
        openActivity(AddShippingAddressActivity.class);
    }

}
