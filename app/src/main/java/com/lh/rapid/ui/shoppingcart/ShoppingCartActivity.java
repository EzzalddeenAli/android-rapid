package com.lh.rapid.ui.shoppingcart;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.MathUtil;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.bean.Cart;
import com.lh.rapid.bean.CirLord;
import com.lh.rapid.bean.StroeInfo;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.main.MainActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/7.
 */

public class ShoppingCartActivity extends BaseActivity {

    public static final String EDITE = "edite";
    public static final String COMPLETE = "complete";
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.lv_cart_cate)
    ExpandableListView mLvCartCate;
    @BindView(R.id.bt_cart_jiesuan)
    Button mBtCartJiesuan;
    @BindView(R.id.tv_cart_total_price)
    TextView mTvCartTotalPrice;
    @BindView(R.id.rl_cart_topay)
    RelativeLayout mRlCartTopay;
    @BindView(R.id.iv_cart_down)
    ImageView mIvCartDown;
    @BindView(R.id.iv_cart_up)
    ImageView mIvCartUp;
    @BindView(R.id.lv_cart_out)
    ExpandableListView mLvCartOut;
    @BindView(R.id.iv_cart_bg)
    ImageView mIvCartBg;
    @BindView(R.id.fl_cart_show)
    FrameLayout mFlCartShow;
    @BindView(R.id.lv_cart_outs)
    ExpandableListView mLvCartOuts;
    @BindView(R.id.iv_cart_bgs)
    ImageView mIvCartBgs;
    @BindView(R.id.fl_cart_shows)
    FrameLayout mFlCartShows;
    @BindView(R.id.ll_cart_show)
    LinearLayout mLlCartShow;
    @BindView(R.id.iv_cart_choose)
    ImageView mIvCartChoose;
    @BindView(R.id.bt_cart_delete)
    Button mBtCartDelete;
    @BindView(R.id.ll_cart_delete)
    LinearLayout mLlCartDelete;

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
    /**
     * 购物车中总价格，全局静态变量
     */
    public static float totalPrice = 0;

    @Override
    public int initContentView() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        cartOneAdapter = new ShoppingCartOneAdapter(this, mCartInfo);
        cartTwoAdapter = new ShoppingCartTwoAdapter(this, cirLords);
        cartThreeAdapter = new ShoppingCartThreeAdapter(this, mCartInfo);

        mLvCartCate.setAdapter(cartOneAdapter);
        mLvCartOut.setAdapter(cartTwoAdapter);
        mLvCartOuts.setAdapter(cartThreeAdapter);

        mActionbar.setTitle("购物车");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setRightIvClickLitener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                if (EDITE.equalsIgnoreCase(mEditeType)) {
                    mActionbar.setRightText("完成");
                    mEditeType = COMPLETE;
                    mRlCartTopay.setVisibility(View.GONE);
                    mLlCartDelete.setVisibility(View.VISIBLE);
                    mFlCartShow.setVisibility(View.GONE);
                    mFlCartShows.setVisibility(View.VISIBLE);
                    //                    mFreight.setVisibility(View.GONE);
                    toggleType();
                    isEdit = true;

                    changeTotal();
                } else {
                    mActionbar.setRightText("编辑");
                    mEditeType = EDITE;
                    mRlCartTopay.setVisibility(View.VISIBLE);
                    mLlCartDelete.setVisibility(View.GONE);
                    mFlCartShow.setVisibility(View.VISIBLE);
                    mFlCartShows.setVisibility(View.GONE);
                    toggleType();
                    isEdit = false;
                    for (StroeInfo pd : mCartInfo) {
                        for (Cart cart : pd.cartList) {
                            if (cart.saleStatus != 1) {
                                pd.isChecked = false;
                                cart.isCheck = false;
                            }
                        }
                    }
                    cartOneAdapter.notifyDataSetChanged();
                    changeTotal();
                }
            }
        });
    }

    private void toggleType() {
        if (cartOneAdapter != null) {
            cartOneAdapter.setType(mEditeType);
            cartOneAdapter.notifyDataSetChanged();
        }
        if (EDITE.equalsIgnoreCase(mEditeType)) {
            mRlCartTopay.setVisibility(View.VISIBLE);
            mLlCartDelete.setVisibility(View.GONE);
        } else {
            mRlCartTopay.setVisibility(View.GONE);
            mLlCartDelete.setVisibility(View.VISIBLE);
        }
    }

    private synchronized void changeTotal() {
        boolean isAll = true;
        int count = 0;
        float total = 0;

        for (StroeInfo pd : mCartInfo) {
            for (Cart cart : pd.cartList) {
                if (!cart.isCheck) {
                    if (!isEdit) {
                        //                    if (cart.isSale && cart.quantity > 0)
                        if (cart.saleStatus == 1)
                            isAll = false;
                    } else {
                        isAll = false;
                    }
                    if (mSelectedCartsToPay.contains(cart))
                        mSelectedCartsToPay.remove(cart);
                } else {
                    if (cart.saleStatus == 1) {
                        if (!mSelectedCartsToPay.contains(cart))
                            mSelectedCartsToPay.add(cart);
                        count += cart.count;
                        //判断有促销价就使用促销价，没有促销价就使用原本的价格
                        if (cart.promotionPrice != null && cart.promotionPrice > 0 && cart.promotionPrice < cart.price) {
                            total += cart.count * cart.promotionPrice;
                        } else {
                            total += cart.count * cart.price;
                        }
                    }
                }
            }
        }

        totalPrice = total;
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((double) totalPrice);
        bd = bd.setScale(scale, roundingMode);
        totalPrice = bd.floatValue();
        //        if (count > 0)
        //            bt_cart_jiesuan.setText("结算(" + count + ")");
        //        else
        mBtCartJiesuan.setText("去结算");

        setFreight();

        Constants.isAllCheck = isAll;
        mIvCartChoose.setSelected(Constants.isAllCheck);
        //通知MainActivity修改购物车数量
        if (ShoppingCartActivity.this.getParent() instanceof MainActivity) {
            ((MainActivity) ShoppingCartActivity.this.getParent()).setCartNum(count);
        }
    }

    private void setFreight() {
        String price = "合计:￥" + MathUtil.point2decimal(totalPrice);
        mTvCartTotalPrice.setText(price);
    }

}
