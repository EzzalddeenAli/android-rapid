package com.lh.rapid.ui.fragment1;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.wrapper.LoadMoreWrapper;
import com.android.frameproj.library.decoration.RecyclerViewDivider;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.CategoryName;
import com.lh.rapid.bean.GoodBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.location.ChooseLocationActivity;
import com.lh.rapid.ui.main.MainComponent;
import com.lh.rapid.ui.productdetail.ProductDetailActivity;
import com.lh.rapid.util.MyGlideImageLoader;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class Fragment1 extends BaseFragment implements Fragment1Contract.View, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.iv_home_place)
    ImageView mIvHomePlace;
    @BindView(R.id.tv_home_location)
    TextView mTvHomeLocation;
    @BindView(R.id.iv_home_down)
    ImageView mIvHomeDown;
    @BindView(R.id.iv_home_mine)
    ImageView mIvHomeMine;
    @BindView(R.id.iv_home_search)
    ImageView mIvHomeSearch;
    @BindView(R.id.et_home_name)
    EditText mEtHomeName;
    @BindView(R.id.iv_home_delete)
    ImageView mIvHomeDelete;
    @BindView(R.id.tv_home_search)
    TextView mTvHomeSearch;
    @BindView(R.id.iv_alf)
    ImageView mIvAlf;
    @BindView(R.id.main_banner)
    Banner mMainBanner;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.rv_form_name)
    RecyclerView mRvFormName;
    @BindView(R.id.rv_form_detail)
    RecyclerView mRvFormDetail;
    @BindView(R.id.cl_main)
    CoordinatorLayout mClMain;
    @BindView(R.id.tv_cart_product_detail)
    TextView mTvCartProductDetail;
    @BindView(R.id.tv_cart_product_detail_send)
    TextView mTvCartProductDetailSend;
    @BindView(R.id.iv_footer_cart_product_detail)
    ImageView mIvFooterCartProductDetail;
    @BindView(R.id.tv_cart_num_product_detail)
    TextView mTvCartNumProductDetail;
    @BindView(R.id.rl_footer_cart_product_detail)
    RelativeLayout mRlFooterCartProductDetail;
    @BindView(R.id.tv_favorite_product_detail_chose)
    TextView mTvFavoriteProductDetailChose;
    @BindView(R.id.rl_main_bottom)
    RelativeLayout mRlMainBottom;
    @BindView(R.id.ll_cart)
    LinearLayout mLlCart;
    @BindView(R.id.tv_circle_name)
    TextView mTvCircleName;
    private RxPermissions mRxPermissions;
    private CategoryCommNameAdapter nameAdapter;
    private CategoryCommodityAdapter commodityAdapter;

    private List<CategoryName> mNames = new ArrayList<>();
    private List<GoodBean> list3 = new ArrayList<GoodBean>();
    private List<GoodBean> list4 = new ArrayList<GoodBean>();
    private List<GoodBean> list5 = new ArrayList<GoodBean>();
    private List<HomePageBean.CategoryListsBean.GoodListBean> goodBeans = new ArrayList<>();
    private SparseArray<GoodBean> selectedList;

    public static int num = 0;
    private ViewGroup anim_mask_layout;//动画层
    Double totleMoney = 0.00;
    private static DecimalFormat df;

    @Inject
    Fragment1Presenter mPresenter;
    private CommonAdapter mRecommendCommonAdapter;

    private String mScanResult;
    private int mCircleId;

    public static BaseFragment newInstance() {
        Fragment1 fragment1 = new Fragment1();
        return fragment1;
    }

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_1;
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
        mPresenter.attachView(this);
        mRxPermissions = new RxPermissions(getActivity());
        mRxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                        } else {
                            ToastUtil.showToast("没有权限部分功能不能正常运行!");
                        }
                    }
                });


        selectedList = new SparseArray<>();
        df = new DecimalFormat("0.00");

        initTitle();
        mPresenter.loadDate(2 + "");
    }

    private void initTitle() {
        mEtHomeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEtHomeName.getText().toString().equals("")) {
                    mIvHomeDelete.setVisibility(View.GONE);
                    mTvHomeSearch.setVisibility(View.GONE);
                } else {
                    mIvHomeDelete.setVisibility(View.VISIBLE);
                    mTvHomeSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onLoadMoreRequested() {

    }

    @OnClick(R.id.iv_home_mine)
    public void mIvHomeMine() {

    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id) {
        GoodBean temp = selectedList.get(id);
        if (temp == null) {
            return 0;
        }
        return temp.getNum();
    }

    public void handlerCarNum(int type, GoodBean goodsBean, boolean refreshGoodList) {
        if (type == 0) {
            GoodBean temp = selectedList.get(goodsBean.getProduct_id());
            if (temp != null) {
                if (temp.getNum() < 2) {
                    goodsBean.setNum(0);
                    selectedList.remove(goodsBean.getProduct_id());

                } else {
                    int i = goodsBean.getNum();
                    goodsBean.setNum(--i);
                }
            }

        } else if (type == 1) {
            GoodBean temp = selectedList.get(goodsBean.getProduct_id());
            if (temp == null) {
                goodsBean.setNum(1);
                selectedList.append(goodsBean.getProduct_id(), goodsBean);
            } else {
                int i = goodsBean.getNum();
                goodsBean.setNum(++i);
            }
        }
        update(refreshGoodList);
    }

    private void update(boolean refreshGoodList) {
        int size = selectedList.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            GoodBean item = selectedList.valueAt(i);
            count += item.getNum();
            totleMoney += item.getNum() * Double.parseDouble(item.getPrice());
        }
        if (count < 1) {
            mTvCartNumProductDetail.setVisibility(View.GONE);
            mTvFavoriteProductDetailChose.setText("还未选择商品");
            totleMoney = 0.00;
            mTvCartProductDetailSend.setText("¥20.0起送");
            mTvCartProductDetailSend.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            mTvCartProductDetailSend.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_b7b7b7));
        } else {
            Double mAmount = Double.valueOf(df.format(totleMoney));
            mTvCartProductDetail.setVisibility(View.VISIBLE);
            BigDecimal bAmount = new BigDecimal(mAmount.toString());
            if (bAmount.compareTo(new BigDecimal(20.0)) < 0) {

                mTvCartProductDetailSend.setText("还差" + new BigDecimal(20.0).subtract(bAmount) + "起送");
                mTvCartNumProductDetail.setVisibility(View.VISIBLE);
                mTvCartProductDetailSend.setVisibility(View.VISIBLE);
                mTvCartProductDetail.setVisibility(View.GONE);
            } else {
                mTvCartProductDetailSend.setVisibility(View.GONE);
                mTvCartProductDetail.setVisibility(View.VISIBLE);
            }
            totleMoney = 0.00;
            mTvFavoriteProductDetailChose.setText("¥" + bAmount);
        }
        mTvCartNumProductDetail.setText(String.valueOf(count));

        if (commodityAdapter != null) {
            commodityAdapter.notifyDataSetChanged();
        }

        if (nameAdapter != null) {
            nameAdapter.notifyDataSetChanged();
        }

    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        mIvFooterCartProductDetail.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }

    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setCartNum(int num) {
        this.num = num;
        if (num > 0) {
            mTvCartNumProductDetail.setVisibility(View.VISIBLE);
            mTvCartNumProductDetail.setText(num + "");
        } else {
            mTvCartNumProductDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadDateCompleted(HomePageBean homePageBean) {
        initBanner(homePageBean.getBnTop());
        initRecyclerView(homePageBean.getCategoryLists());
        mTvCircleName.setText(homePageBean.getCircleName());
        mCircleId = homePageBean.getCircleId();
    }

    private void initBanner(List<HomePageBean.BnTopBean> bnTop) {
        mMainBanner.setImageLoader(new MyGlideImageLoader());
        mMainBanner.setImages(bnTop);
        mMainBanner.setDelayTime(2000);
        mMainBanner.start();
    }

    private void initRecyclerView(final List<HomePageBean.CategoryListsBean> categoryLists) {
        //商品
        for (int j = 10; j < 25; j++) {
            GoodBean goodsBean = new GoodBean();
            goodsBean.setName("胡辣汤");
            goodsBean.setProduct_id(j);
            goodsBean.setCategory_id(j);
            goodsBean.setIcon("http://g.hiphotos.baidu.com/image/pic/item/03087bf40ad162d9ec74553b14dfa9ec8a13cd7a.jpg");
            goodsBean.setWeight("40");
            goodsBean.setPrice("10");
            list5.add(goodsBean);
        }

        mRvFormName.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvFormName.addItemDecoration(new RecyclerViewDivider(getActivity().getApplicationContext(), LinearLayout.VERTICAL, 2, R.color.white));
        nameAdapter = new CategoryCommNameAdapter(this, getActivity(), categoryLists);
        mRvFormName.setAdapter(nameAdapter);


        goodBeans.clear();
        goodBeans.addAll(categoryLists.get(0).getGoodList());
        mRvFormDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvFormDetail.addItemDecoration(new RecyclerViewDivider(getActivity().getApplicationContext(), LinearLayout.VERTICAL, 2, R.color.line));
        commodityAdapter = new CategoryCommodityAdapter(this, getActivity(), goodBeans, nameAdapter);
        mRvFormDetail.setAdapter(commodityAdapter);

        nameAdapter.setItemClickLitener(new CategoryCommNameAdapter.rvItemClickLitener() {
            @Override
            public void itemClick(int position) {
                if(mNames.get(position).getList()!=null){
                    Log.i("fyg", "list.get(position).getList():" + mNames.get(position).getList());
                    goodBeans.clear();
                    goodBeans.addAll(categoryLists.get(position).getGoodList());
                }
                nameAdapter.setSelection(position);
                nameAdapter.notifyDataSetChanged();
                commodityAdapter.notifyDataSetChanged();
            }
        });

        commodityAdapter.setItemClickLitener(new CategoryCommodityAdapter.rvCateItemClickLitener() {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("goodsId",goodBeans.get(position).getGoodsId());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.rl_location)
    public void mRlLocation(View view){
        openActivity(ChooseLocationActivity.class);
    }
}