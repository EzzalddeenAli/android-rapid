package com.lh.rapid.ui;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.frameproj.library.dialog.LoadingDialog;
import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.JsonParseException;
import com.gyf.barlibrary.ImmersionBar;
import com.lh.rapid.AppManager;
import com.lh.rapid.Constants;
import com.lh.rapid.MyApplication;
import com.lh.rapid.R;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;
import com.lh.rapid.ui.login.LoginActivity;
import com.lh.rapid.util.SPUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import retrofit2.HttpException;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public abstract class BaseActivity extends SupportActivity {

    public ImmersionBar mImmersionBar;

    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //注入数据库
        getApplicationComponent().inject(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        setStatusBar();
        ButterKnife.bind(this);
        initInjector();
        initUiAndListener();
        AppManager.getAppManager().addActivity(this);
    }

    public void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .statusBarDarkFont(true, 0f)
                .flymeOSStatusBarFontColorInt(getResources().getColor(R.color.black))
                .fitsSystemWindows(true)
                .init();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    private void initTheme() {

    }

    /**
     * 设置view
     */
    public abstract int initContentView();

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * init UI && Listener
     */
    public abstract void initUiAndListener();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    /**
     * 打开新的Activity
     *
     * @param cls
     */
    public void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 设置返回键
     *
     * @param imageView
     */
    public void setImgBack(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof CommonApi.APIException) {
            ToastUtil.showToast(throwable.getMessage());
            if (((CommonApi.APIException) throwable).code == Constants.NO_USER) {
                // 用户未登录
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_EXIST) {
                // 在其他地方登录了
                mSPUtil.setIS_LOGIN(0);
                mSPUtil.setTOKNE("");
                mUserStorage.setToken("");
                // 这里可以用 mUserStorage.logout();替代,需要测试
                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_FREEZE) {
                // 用户被锁定
                mSPUtil.setIS_LOGIN(0);
                mSPUtil.setTOKNE("");
                mUserStorage.setToken("");
                openActivity(LoginActivity.class);
            }
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case Constants.UNAUTHORIZED:
                case Constants.FORBIDDEN:
                    //                    onPermissionError(ex);          //权限错误，需要实现
                    ToastUtil.showToast(getResources().getString(R.string.error_permission));
                    break;
                case Constants.NOT_FOUND:
                case Constants.REQUEST_TIMEOUT:
                case Constants.GATEWAY_TIMEOUT:
                case Constants.INTERNAL_SERVER_ERROR:
                case Constants.BAD_GATEWAY:
                case Constants.SERVICE_UNAVAILABLE:
                default:
                    //均视为网络错误
                    ToastUtil.showToast(getResources().getString(R.string.error_network));
                    break;
            }
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ToastUtil.showToast(getResources().getString(R.string.error_parse));
        } else if (throwable instanceof UnknownHostException) {
            ToastUtil.showToast(getResources().getString(R.string.error_network));
        } else if (throwable instanceof SocketTimeoutException) {
            //            ToastUtil.showToast(getResources().getString(R.string.error_overtime));
        } else if (throwable instanceof ConnectException) {
            ToastUtil.showToast(getResources().getString(R.string.error_connect));
        } else {
            //            ToastUtil.showToast(getResources().getString(R.string.error_unknow));
        }
    }

    public void showLoading() {
        mLoadingDialog = LoadingDialog.show(BaseActivity.this, "");
    }

    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    public void onError(Throwable throwable) {
        loadError(throwable);
    }

}
