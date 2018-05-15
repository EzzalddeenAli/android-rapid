package com.lh.rapid.ui.addressmanager;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.frameproj.library.decoration.RecyclerViewDivider;
import com.android.frameproj.library.inter.OnItemClickListener;
import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.Gson;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.inter.OnCartGoodsDelete;
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
    private int mComefrom;

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
        mComefrom = getIntent().getIntExtra("comefrom", -1);
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
        adapter.setOnCartGoodsDelete(new OnCartGoodsDelete() {
            @Override
            public void cartGoodsDelete(int addressId) {
                mPresenter.addressDelete(addressId + "");
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        if(mComefrom == 1) {
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    AddressListBean addressListBean = addressListBeans.get(position);
                    Intent data = new Intent();
                    data.putExtra("addressListBean",new Gson().toJson(addressListBean));
                    setResult(Constants.RESULT_LOCATION_MANAGER_CODE,data);
                    finish();
                }
            });
        }
        mRvManageAddress.setLayoutManager(new LinearLayoutManager(this));
        mRvManageAddress.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.VERTICAL, 2, R.color.line));
        mRvManageAddress.setAdapter(adapter);

        mRefreshLayout.finishRefresh(300);
    }

    @Override
    public void addressDeleteSuccess(String s) {
        ToastUtil.showToast(s);
        mRefreshLayout.autoRefresh();
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
