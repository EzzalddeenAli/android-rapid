package com.lh.rapid.ui.productdetail;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.widget.AdvertView;
import com.android.frameproj.library.widget.CommonWebView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lh.rapid.R;
import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.bean.Hover;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.ui.widget.PullUpToLoadMore;
import com.lh.rapid.util.CommonEvent;
import com.lh.rapid.util.SPUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/7.
 */

public class ProductDetailActivity extends BaseActivity implements ProductDetailContract.View {

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
    @BindView(R.id.rl_product_bottom)
    RelativeLayout mRlProductBottom;
    @BindView(R.id.av_product_detail)
    AdvertView mAvProductDetail;
    @BindView(R.id.rv_product_detail)
    RecyclerView mRvProductDetail;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_product_weight)
    TextView mTvProductWeight;
    @BindView(R.id.ll_product_weight)
    LinearLayout mLlProductWeight;
    @BindView(R.id.view_line_two)
    View mViewLineTwo;
    @BindView(R.id.tv_product_time)
    TextView mTvProductTime;
    @BindView(R.id.ll_product_time)
    LinearLayout mLlProductTime;
    @BindView(R.id.wb_product_detail)
    CommonWebView mWbProductDetail;
    @BindView(R.id.ptlm_product)
    PullUpToLoadMore mPtlmProduct;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;

    private SparseArray<Hover> selectedList;
    private List<Hover> list;
    private Hover hover;
    Double totleMoney = 0.00;
    private static DecimalFormat df;
    private ProductDetailAdapter adapter;
    private ViewGroup anim_mask_layout;//动画层
    private List<Hover> hovers = new ArrayList<>();
    private GoodsDetailBean mGoodsDetailBean;

    private int mGoodsId;
    @Inject
    ProductDetailPresenter mPresenter;
    private String mGoodsDesc;

    @Inject
    Bus mBus;
    @Inject
    SPUtil mSPUtil;

    @Override
    public int initContentView() {
        return R.layout.activity_product_detail;
    }

    @Override
    public void initInjector() {
        DaggerProductDetailComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mBus.register(this);
        selectedList = new SparseArray<>();
        df = new DecimalFormat("0.00");

        mGoodsId = getIntent().getIntExtra("goodsId", -1);
        mPresenter.attachView(this);
        mPresenter.loadDate(mSPUtil.getCIRCLE_ID()+"", mGoodsId + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAvProductDetail != null)
            mAvProductDetail.stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAvProductDetail != null)
            mAvProductDetail.startAutoScroll();
    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id) {
        Hover temp = selectedList.get(id);
        if (temp == null) {
            return 0;
        }
        return temp.getNum();
    }

    public void handlerCarNum(int type, Hover hover, boolean refreshGoodList) {
        int count = -1;
        if (type == 0) {
            Hover temp = selectedList.get(hover.getProduct_id());
            if (temp != null) {
                if (temp.getNum() < 2) {
                    hover.setNum(0);
                    selectedList.remove(hover.getProduct_id());
                    count = 0;
                } else {
                    int i = hover.getNum();
                    hover.setNum(--i);
                    count = i;
                }
            }
        } else if (type == 1) {
            Hover temp = selectedList.get(hover.getProduct_id());
            if (temp == null) {
                hover.setNum(1);
                selectedList.append(hover.getProduct_id(), hover);
                count = 1;
            } else {
                int i = hover.getNum();
                hover.setNum(++i);
                count = i;
            }
        }
        update(refreshGoodList);
        mBus.post(new CommonEvent().new ProductCartCountEvent(count));
    }

    private void update(boolean refreshGoodList) {
        int size = selectedList.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Hover item = selectedList.valueAt(i);
            count += item.getNum();
            totleMoney += item.getNum() * Double.parseDouble(item.getPrice());
        }
        if (count < 1) {
            mTvCartNumProductDetail.setVisibility(View.GONE);
            mTvFavoriteProductDetailChose.setText("还未选择商品");
            totleMoney = 0.00;
            mTvCartProductDetailSend.setText("¥20.0起送");
            mTvCartProductDetailSend.setTextColor(ContextCompat.getColor(this, R.color.white));
            mTvCartProductDetailSend.setBackgroundColor(ContextCompat.getColor(this, R.color.color_b7b7b7));
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

        if (adapter != null) {
            adapter.notifyDataSetChanged();
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

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
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

    @Override
    public void onLoadDateCompleted(final GoodsDetailBean goodsDetailBean) {
        hovers = getData(goodsDetailBean);
        mGoodsDetailBean = goodsDetailBean;
        adapter = new ProductDetailAdapter(this, hovers);
        mRvProductDetail.setLayoutManager(new LinearLayoutManager(this));
        mRvProductDetail.setAdapter(adapter);
        mTvProductWeight.setText(goodsDetailBean.getStandard());
        mTvProductTime.setText(goodsDetailBean.getSendTime());
        mPtlmProduct.setIProductDetailListener(new PullUpToLoadMore.IProductDetailListener() {
            @Override
            public void onProductDetail() {
                mWbProductDetail.setVisibility(View.VISIBLE);
                mWbProductDetail.setCommonWebView(goodsDetailBean.getGoodsDesc());
            }
        });

        List<BaseSliderView> baseSliderViews = new ArrayList<>();
        List<String> imgs = goodsDetailBean.getImgs();
        for (int i = 0; i < imgs.size(); i++) {
            final String image = imgs.get(i);
            BaseSliderView baseSliderView = new BaseSliderView(ProductDetailActivity.this) {
                @Override
                public View getView() {
                    ImageView imageView = new ImageView(ProductDetailActivity.this);
                    ImageLoaderUtil.getInstance().loadImage(image, imageView);
                    return imageView;
                }
            };
            baseSliderViews.add(baseSliderView);
        }
        mAvProductDetail.setAdvert(baseSliderViews);
    }

    private List<Hover> getData(GoodsDetailBean goodsDetailBean) {
        List<Hover> list = new ArrayList<>();
        Hover hover = new Hover();
        hover.setName(goodsDetailBean.getGoodsName());
        hover.setDiscounts(goodsDetailBean.getShareDesc());
        hover.setPrice(goodsDetailBean.getSalePrice() + "");
        hover.setNum(goodsDetailBean.getCounts());
        list.add(hover);
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mBus.unregister(this);
    }

    @Override
    public void cartGoodsAddSuccess(String s) {
//        ToastUtil.showToast(s);
    }

    @Override
    public void cartGoodsDeleteSuccess(String s) {
//        ToastUtil.showToast(s);
    }

    @Subscribe
    public void productCartCountEvent(CommonEvent.ProductCartCountEvent productCartCountEvent) {

        if (productCartCountEvent != null && productCartCountEvent.getCount() != -1) {
            mPresenter.cartGoodsAdd(mGoodsDetailBean.getGoodsId() + "", productCartCountEvent.getCount() + "", mSPUtil.getCIRCLE_ID() + "");
        } else {

        }
    }

}
