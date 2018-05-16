package com.lh.rapid.ui.setting;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.CommonUtils;
import com.android.frameproj.library.util.DataClearManager;
import com.android.frameproj.library.util.DataSizeManager;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2017/10/16.
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.rl_version_code)
    RelativeLayout mRlVersionCode;
    @BindView(R.id.tv_cache)
    TextView mTvCache;
    @BindView(R.id.rl_cache)
    RelativeLayout mRlCache;
    @BindView(R.id.btn_exit)
    TextView mBtnExit;

    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;

    @Override
    public int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initInjector() {
    }

    @Override
    public void initUiAndListener() {
        mActionbar.setTitle("设置");
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });

        mTvVersion.setText(CommonUtils.getVersionName(this));
        mTvCache.setText(DataSizeManager.getTotalCacheSize(this));
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(SettingActivity.this)
                        .content("确定退出登录吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DataClearManager.cleanApplicationData(SettingActivity.this);
                                mSPUtil.setIS_LOGIN(0);
                                mSPUtil.setTOKNE("");
                                mUserStorage.setToken("");
                                setResult(Constants.RESULT_EXIST_CODE);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.rl_cache)
    public void mRlCache() {
        DataClearManager.cleanApplicationData(SettingActivity.this);
        mTvCache.setText(DataSizeManager.getTotalCacheSize(SettingActivity.this));
        ToastUtil.showToast("清除缓存完成");
    }

}
