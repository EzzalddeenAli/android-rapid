package com.lh.rapid.ui.main;

import android.Manifest;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.bean.BannerItem;
import com.android.frameproj.library.decoration.RecyclerViewDivider;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.widget.GlideImageLoader;
import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.usercenter.UserCenterActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

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
    private RxPermissions mRxPermissions;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
    }

    @Override
    public void initUiAndListener() {
        mRxPermissions = new RxPermissions(MainActivity.this);
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

        initTitle();
        initBanner();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRvFormName.setLayoutManager(new LinearLayoutManager(this));
        mRvFormName.addItemDecoration(new RecyclerViewDivider(getApplicationContext(), LinearLayout.VERTICAL, 2, R.color.white));
        List<String> stringList = new ArrayList<>();
        stringList.add("江湖餐品");
        stringList.add("江湖餐品");
        stringList.add("江湖餐品");
        CommonAdapter commonAdapter = new CommonAdapter<String>(MainActivity.this, R.layout.item_category_name, stringList) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_cart_form_name,s);
            }

        };
        mRvFormName.setAdapter(commonAdapter);
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

    private void initBanner() {
        mMainBanner.setImageLoader(new GlideImageLoader());
        mMainBanner.setImages(BANNER_ITEMS);
        mMainBanner.setDelayTime(2000);
        mMainBanner.start();
    }

    public List<BannerItem> BANNER_ITEMS = new ArrayList<BannerItem>() {{
        add(new BannerItem("最后的骑士", "http://p1.meituan.net/movie/f1e42208897d8674bb7aab89fb078baf487236.jpg"));
        add(new BannerItem("三生三世十里桃花", "http://p1.meituan.net/movie/aa3c2bac8f9aaa557e63e20d56e214dc192471.jpg"));
        add(new BannerItem("豆福传", "http://p0.meituan.net/movie/07b7f22e2ca1820f8b240f50ee6aa269481512.jpg"));
    }};


    @OnClick(R.id.iv_home_mine)
    public void mIvHomeMine() {
        openActivity(UserCenterActivity.class);
    }

}
