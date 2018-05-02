package com.lh.rapid.ui.myshare;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.android.frameproj.library.util.log.Logger;
import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/2.
 */

public class MyShareActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_my_share;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(500);
                Logger.i("onRefresh ... ");
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(500);
            }
        });
        mRefreshLayout.setEnableLoadMore(false);
//        mRefreshLayout.autoRefresh();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("string " + i);
        }
        mCommonAdapter = new CommonAdapter<String>(MyShareActivity.this, R.layout.layout_my_share, data) {
            @Override
            protected void convert(ViewHolder holder, final String string, int position) {
                holder.setText(R.id.textView, string);
            }
        };
        mLinearLayoutManager = new LinearLayoutManager(MyShareActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCommonAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(MyShareActivity.this, 1));
    }

}
