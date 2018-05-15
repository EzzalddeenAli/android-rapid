package com.lh.rapid.ui.fragment2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.dialog.LoadingDialog;
import com.android.frameproj.library.inter.OnItemClickListener;
import com.android.frameproj.library.interf.CallbackChangeFragment;
import com.lh.rapid.R;
import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.productlist.ProductListActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment2 extends BaseFragment implements Fragment2Contract.View {


    @Inject
    Fragment2Presenter mFragment2Presenter;
    @Inject
    Bus mBus;

    @BindView(R.id.recyclerViewLeft)
    RecyclerView mRecyclerViewLeft;
    @BindView(R.id.recyclerViewRight)
    RecyclerView mRecyclerViewRight;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    private List<CategoryOneLevelBean> mCategoryOneLevelBeanList;

    public static BaseFragment newInstance() {
        Fragment2 fragment2 = new Fragment2();
        return fragment2;
    }

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_2;
    }

    //  1
    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    //  2
    @Override
    public void getBundle(Bundle bundle) {

    }


    //  3
    @Override
    public void initUI(View view) {
        mActionbar.setTitle("分类");
        mActionbar.setLeftVisible(View.GONE);
        mBus.register(this);
        mFragment2Presenter.attachView(this);
        mFragment2Presenter.categoryOneLevel();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mCategoryOneLevelBeanList == null || mCategoryOneLevelBeanList.size() == 0) {
            mFragment2Presenter.categoryOneLevel();
        }
    }

    @Override
    public void initData() {

    }

    LoadingDialog mLoadingDialog;

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(getActivity(), "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void categoryOneLevelSuccess(List<CategoryOneLevelBean> categoryOneLevelBeanList) {
        showLeftRecyclerView(categoryOneLevelBeanList);
    }

    @Override
    public void categoryDetailsSuccess(List<CategoryDetailsBean> categoryDetailsBeanList) {
        showRightRecyclerView(categoryDetailsBeanList);
    }

    private void showLeftRecyclerView(final List<CategoryOneLevelBean> categoryOneLevelBeanList) {
        mCategoryOneLevelBeanList = categoryOneLevelBeanList;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewLeft.setLayoutManager(layoutManager);
        final ClassifyLeftAdapter classifyLeftAdapter = new ClassifyLeftAdapter(getActivity(), categoryOneLevelBeanList, 0);
        classifyLeftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                classifyLeftAdapter.setCheckPosition(position);
                mFragment2Presenter.categoryDetails(categoryOneLevelBeanList.get(position).getId());
            }
        });
        mRecyclerViewLeft.setAdapter(classifyLeftAdapter);
        mRecyclerViewLeft.setItemAnimator(new DefaultItemAnimator());

        mFragment2Presenter.categoryDetails(categoryOneLevelBeanList.get(0).getId());
    }

    private void showRightRecyclerView(List<CategoryDetailsBean> categoryDetailsBeanList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewRight.setLayoutManager(layoutManager);
        CommonAdapter rightAdapter = new CommonAdapter<CategoryDetailsBean>(getActivity(), R.layout.layout_classify_right, categoryDetailsBeanList) {
            @Override
            protected void convert(ViewHolder holder, final CategoryDetailsBean categoryDetailsBean, int position) {
                holder.setText(R.id.tv_classify_name, categoryDetailsBean.getCategoryName());
                RecyclerView recyclerView = holder.getView(R.id.recyclerView);

                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(layoutManager);
                CommonAdapter childAdapter = new CommonAdapter<CategoryDetailsBean.CategoryBean>(getActivity(), R.layout.layout_classify_right_child, categoryDetailsBean.getCategory()) {
                    @Override
                    protected void convert(ViewHolder holder, CategoryDetailsBean.CategoryBean categoryBean, int position) {
                        holder.setText(R.id.tv_classify, categoryBean.getCategoryName());
                        holder.setImageUrl(R.id.iv_classify, categoryBean.getPhotoPath());
                    }
                };
                childAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        int categoryid = categoryDetailsBean.getCategory().get(position).getId();
                        String categoryname = categoryDetailsBean.getCategory().get(position).getCategoryName();
                        Intent intent = new Intent(getActivity(), ProductListActivity.class);
                        intent.putExtra("categoryId", categoryid);
                        startActivity(intent);
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                recyclerView.setAdapter(childAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        };
        mRecyclerViewRight.setAdapter(rightAdapter);
        mRecyclerViewRight.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragment2Presenter.detachView();
    }

    // 切换到主页面
    private CallbackChangeFragment mCallbackChangeFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbackChangeFragment = (CallbackChangeFragment) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}