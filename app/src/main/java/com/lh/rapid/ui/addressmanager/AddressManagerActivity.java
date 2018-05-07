package com.lh.rapid.ui.addressmanager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.frameproj.library.decoration.RecyclerViewDivider;
import com.lh.rapid.R;
import com.lh.rapid.bean.Site;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lh on 2018/5/7.
 */

public class AddressManagerActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.rv_manage_address)
    RecyclerView mRvManageAddress;
    @BindView(R.id.ll_manage_address)
    LinearLayout mLlManageAddress;
    private ManagerSiteAdapter adapter;
    private List<Site> list=new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_address_manager;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        list=getData();
        adapter=new ManagerSiteAdapter(this,getData());
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("地址管理");
        mRvManageAddress.setLayoutManager(new LinearLayoutManager(this));
        mRvManageAddress.addItemDecoration(new RecyclerViewDivider(this,LinearLayout.VERTICAL,2,R.color.line));
        mRvManageAddress.setAdapter(adapter);

        adapter.setChangeListener(new ManagerSiteAdapter.itemAddressChangeListener() {
            @Override
            public void changeListener(int position) {
//                Intent intent = new Intent(AddressManagerActivity.this,);
//                startActivity(intent);
            }
        });

    }

    private List<Site> getData() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Site site = new Site();
            site.setSite("南大路458号");
            site.setName("×××");
            site.setPhone("12345678911");
            site.setImage(R.mipmap.icon_address_xiugai);
            site.setLine(R.drawable.line_bg);
            list.add(site);
        }
        return list;
    }



}
