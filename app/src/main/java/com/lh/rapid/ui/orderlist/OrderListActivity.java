package com.lh.rapid.ui.orderlist;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.frameproj.library.adapter.SmartFragmentStatePagerAdapter;
import com.android.frameproj.library.widget.NoScrollViewPager;
import com.lh.rapid.R;
import com.lh.rapid.injector.HasComponent;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.orderlist.all.OrderAllFragment;
import com.lh.rapid.ui.widget.MyActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/3.
 */

public class OrderListActivity extends BaseActivity implements HasComponent<OrderListComponent> {


    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;
    private OrderListComponent mMyOrderComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initInjector() {
        mMyOrderComponent = DaggerOrderListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        mMyOrderComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("我的订单");

        OrderFragmentPagerAdapter orderFragmentPagerAdapter = new OrderFragmentPagerAdapter(getSupportFragmentManager());
        orderFragmentPagerAdapter.addFragment(OrderAllFragment.newInstance(), "全部订单");
        mViewPager.setAdapter(orderFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public OrderListComponent getComponent() {
        return mMyOrderComponent;
    }

    public class OrderFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public OrderFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
