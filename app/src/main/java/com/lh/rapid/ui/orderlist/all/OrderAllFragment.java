package com.lh.rapid.ui.orderlist.all;

import android.os.Bundle;
import android.view.View;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.orderlist.OrderListComponent;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderAllFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        OrderAllFragment orderAllFragment = new OrderAllFragment();
        return orderAllFragment;
    }

    @Override
    public void initInjector() {
        getComponent(OrderListComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_list;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {

    }

    @Override
    public void initData() {

    }

}
