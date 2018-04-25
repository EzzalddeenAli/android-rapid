package com.lh.rapid.ui;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.JsonParseException;
import com.lh.rapid.Constants;
import com.lh.rapid.R;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.injector.HasComponent;
import com.lh.rapid.ui.login.LoginActivity;
import com.lh.rapid.util.SPUtil;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import retrofit2.HttpException;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public abstract class BaseFragment extends SupportFragment {

    protected BaseActivity baseActivity;
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(initContentView(), null);
        ButterKnife.bind(this, rootView);
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        if (getActivity() instanceof BaseActivity) {
            baseActivity = (BaseActivity) getActivity();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    public void openActivity(Class<?> cls) {
        startActivity(new Intent(baseActivity, cls));
    }

    public abstract void initInjector();

    public abstract int initContentView();

    /**
     * 得到Activity传进来的值
     */
    public abstract void getBundle(Bundle bundle);

    /**
     * 初始化控件
     */
    public abstract void initUI(View view);

    /**
     * 在监听器之前把数据准备好
     */
    public abstract void initData();

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof CommonApi.APIException) {
            ToastUtil.showToast(throwable.getMessage());
            if (((CommonApi.APIException) throwable).code == Constants.NO_USER) {
                // 用户未登录
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_EXIST) {
                // 在其他地方登录了
                SPUtil spUtil = new SPUtil(baseActivity);
                spUtil.setIS_LOGIN(0);
                spUtil.setTOKNE("");
                UserStorage userStorage = new UserStorage(baseActivity,spUtil);
                userStorage.setToken("");
                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_FREEZE) {
                // 用户被锁定
                SPUtil spUtil = new SPUtil(baseActivity);
                spUtil.setIS_LOGIN(0);
                spUtil.setTOKNE("");
                UserStorage userStorage = new UserStorage(baseActivity,spUtil);
                userStorage.setToken("");
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
        } else if (throwable instanceof SocketTimeoutException) {    //超时
            //            ToastUtil.showToast(getResources().getString(R.string.error_overtime));
        } else {
            //            ToastUtil.showToast(getResources().getString(R.string.error_unknow));
        }
    }

}
