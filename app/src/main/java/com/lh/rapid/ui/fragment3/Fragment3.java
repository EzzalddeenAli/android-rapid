package com.lh.rapid.ui.fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.decoration.DividerGridItemDecoration;
import com.lh.rapid.R;
import com.lh.rapid.bean.Cart;
import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.bean.CirLord;
import com.lh.rapid.bean.StroeInfo;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.orderconfirm.OrderConfirmActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.ui.widget.SelectNumberView;
import com.lh.rapid.util.SPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.otto.Bus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class Fragment3 extends BaseFragment implements Fragment3Contract.View {

    public static final String EDITE = "edite";
    public static final String COMPLETE = "complete";
    @BindView(R.id.fake_status_bar)
    View mFakeStatusBar;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private ShoppingCartOneAdapter cartOneAdapter;
    private ShoppingCartTwoAdapter cartTwoAdapter;
    private ShoppingCartThreeAdapter cartThreeAdapter;
    private List<Cart> mSelectedCartsToPay = new ArrayList<>();
    private List<Cart> mSelected = new ArrayList<>();
    private String mEditeType = EDITE;
    private boolean isEdit = false;//是否是编辑，默认false
    private List<StroeInfo> mCartInfo = new ArrayList<>();
    private List<CirLord> cirLords = new ArrayList<>();
    public Map<String, StroeInfo> SCartInfo = new HashMap<>();
    public Map<Integer, Cart> SCart = new HashMap<>();
    @Inject
    Fragment3Presenter mPresenter;
    @Inject
    Bus mBus;
    @Inject
    SPUtil mSPUtil;

    public static BaseFragment newInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_3;
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
        mBus.register(this);
        mPresenter.attachView(this);

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
    public void initData() {
        cartTwoAdapter = new ShoppingCartTwoAdapter(getActivity(), cirLords);
        cartThreeAdapter = new ShoppingCartThreeAdapter(getActivity(), mCartInfo);


        mActionbar.setTitle("购物车");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {

            }
        });
    }


    private List<CartGoodsBean> mCartGoodsBeanArrayList = new ArrayList<>();

    @Override
    public void onLoadDateCompleted(List<CartGoodsBean> cartGoodsBeans) {
        mCartGoodsBeanArrayList.clear();
        mCartGoodsBeanArrayList.addAll(cartGoodsBeans);
        mRefreshLayout.finishRefresh(300);
        updateRecyclerView();
    }

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;

    private void updateRecyclerView() {
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<CartGoodsBean>(getActivity(), R.layout.layout_cart_item, mCartGoodsBeanArrayList) {
                @Override
                protected void convert(ViewHolder holder, final CartGoodsBean cartGoodsBean, final int position) {

                    holder.setText(R.id.tv_quan_zhu, cartGoodsBean.getCircleName());
                    holder.getView(R.id.bt_cart_jiesuan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                            startActivity(intent);
                        }
                    });

                    BigDecimal sumBigDecimal = new BigDecimal(0);
                    for (int i = 0; i < cartGoodsBean.getGoodsLists().size(); i++) {
                        CartGoodsBean.GoodsListsBean goodsListsBean = cartGoodsBean.getGoodsLists().get(i);
                        double price = goodsListsBean.getPrice();
                        int quantity = goodsListsBean.getQuantity();
                        BigDecimal quantityBigDecimal = new BigDecimal(quantity);
                        BigDecimal priceBigDecimal = new BigDecimal(price);
                        BigDecimal bigDecimal = priceBigDecimal.multiply(quantityBigDecimal);
                        sumBigDecimal = sumBigDecimal.add(bigDecimal);
                    }
                    String sum = sumBigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                    holder.setText(R.id.tv_cart_total_price, "￥" + sum);

                    RecyclerView recyclerViewChild = holder.getView(R.id.recyclerViewChild);
                    recyclerViewChild.setLayoutManager(new LinearLayoutManager(getActivity()));
                    CommonAdapter commonAdapter = new CommonAdapter<CartGoodsBean.GoodsListsBean>(getActivity(), R.layout.item_cart_product, cartGoodsBean.getGoodsLists()) {

                        @Override
                        protected void convert(final ViewHolder holder, final CartGoodsBean.GoodsListsBean goodsListsBean, int position) {
                            holder.setImageUrl(R.id.iv_cart_item_pic, goodsListsBean.getGoodsImgUrl());
                            holder.setText(R.id.tv_cart_item_name, goodsListsBean.getGoodsName());
                            holder.setText(R.id.tv_cart_item_price, "￥" + goodsListsBean.getPrice());

                            final SelectNumberView numberView = holder.getView(R.id.sn_cart_item);
                            numberView.setQuantity(goodsListsBean.getQuantity());
                            numberView.setSelectCallback(new SelectNumberView.ISelectCallback() {
                                @Override
                                public void onResult(int index, int qualitity) {
                                    mPresenter.cartGoodsAdd(goodsListsBean.getGoodsId() + "", qualitity + "", mSPUtil.getCIRCLE_ID() + "");
                                    goodsListsBean.setQuantity(qualitity);
                                    mCommonAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onMaxQuantity() {

                                }

                                @Override
                                public void onMinQuantity() {

                                }
                            });

//                            SwipeLayout swipeLayout = holder.getView(R.id.swipeLayout);
//                            swipeLayout.setLeftSwipeEnabled(true);
                            //删除订单
                            holder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mPresenter.cartGoodsDelete(goodsListsBean.getGoodsId() + "", mSPUtil.getCIRCLE_ID() + "");
                                }
                            });
                        }

                    };

                    RecyclerView.Adapter adapter = recyclerViewChild.getAdapter();
                    if (adapter == null) {
                        recyclerViewChild.addItemDecoration(new DividerGridItemDecoration(getActivity(), 2));
                        recyclerViewChild.setItemAnimator(new DefaultItemAnimator());
                    }
                    recyclerViewChild.setAdapter(commonAdapter);

                }
            };
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), 3));
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mRecyclerView.isComputingLayout() == false)) {
                mCommonAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void cartGoodsAddSuccess(String s) {

    }

    @Override
    public void cartGoodsDeleteSuccess(String s) {
        mRefreshLayout.autoRefresh();
    }

}