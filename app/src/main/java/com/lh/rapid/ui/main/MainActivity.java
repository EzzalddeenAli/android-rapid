package com.lh.rapid.ui.main;

import android.Manifest;
import android.view.View;
import android.widget.Button;

import com.android.frameproj.library.bean.BannerItem;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.banner)
    Banner mBanner;

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
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserCenterActivity.class);
            }
        });

        initBanner();
    }

    private void initBanner() {
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(BANNER_ITEMS);
        mBanner.setDelayTime(2000);
        mBanner.start();
    }

    public List<BannerItem> BANNER_ITEMS = new ArrayList<BannerItem>(){{
        add(new BannerItem("最后的骑士", "http://p1.meituan.net/movie/f1e42208897d8674bb7aab89fb078baf487236.jpg"));
        add(new BannerItem("三生三世十里桃花", "http://p1.meituan.net/movie/aa3c2bac8f9aaa557e63e20d56e214dc192471.jpg"));
        add(new BannerItem("豆福传", "http://p0.meituan.net/movie/07b7f22e2ca1820f8b240f50ee6aa269481512.jpg"));
    }};

}
