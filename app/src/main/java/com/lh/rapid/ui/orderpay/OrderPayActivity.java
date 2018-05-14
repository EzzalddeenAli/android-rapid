package com.lh.rapid.ui.orderpay;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderPayActivity extends BaseActivity {


    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private int mOrderId;
    private double mPrice;

    @Override
    public int initContentView() {
        return R.layout.activity_order_pay;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        mOrderId = getIntent().getIntExtra("orderId", -1);
        mPrice = getIntent().getDoubleExtra("price", -1);
        mTvPrice.setText("￥" + mPrice);

        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("订单支付");
    }


}
