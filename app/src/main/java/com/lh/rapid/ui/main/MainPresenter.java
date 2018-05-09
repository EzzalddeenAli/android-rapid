package com.lh.rapid.ui.main;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.fragment1.Fragment1;
import com.lh.rapid.ui.fragment2.Fragment2;
import com.lh.rapid.ui.fragment3.Fragment3;
import com.lh.rapid.ui.fragment4.Fragment4;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mMainView;

    //存储fragment
    private SparseArray<BaseFragment> fragmentTabMap = new SparseArray<>();

    //之前选中tab
    private int preSelect = -1;
    //当前选中tab
    private int nowSelect = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public MainPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
        disposables.clear();
    }

    @Override
    public void onTabClick(int position) {
        if (position != nowSelect) {
            preSelect = nowSelect;
            nowSelect = position;
            changeTab();
        }
    }

    @Override
    public void initFragment() {
        changeTab();
    }


    private void changeTab() {
        if (preSelect != -1) {
            mMainView.hideFragment(fragmentTabMap.get(preSelect));
        }
        BaseFragment mFragment = null;
        mFragment = fragmentTabMap.get(nowSelect);
        if (mFragment == null) {
            if (nowSelect == 0) {
                mFragment = Fragment1.newInstance();
            } else if (nowSelect == 1) {
                mFragment = Fragment2.newInstance();
            } else if (nowSelect == 2) {
                mFragment = Fragment3.newInstance();
            } else if (nowSelect == 3) {
                mFragment = Fragment4.newInstance();
            }
            if (mFragment != null) {
                fragmentTabMap.put(nowSelect, mFragment);
            }
            mMainView.addFragment(mFragment);
        } else {
            mMainView.showFragment(mFragment);
        }
    }

}