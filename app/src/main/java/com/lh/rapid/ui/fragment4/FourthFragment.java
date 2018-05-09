package com.lh.rapid.ui.fragment4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseMainFragment;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class FourthFragment extends BaseMainFragment {

    private Toolbar mToolbar;
    private View mView;

    public static FourthFragment newInstance() {

        Bundle args = new Bundle();

        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fourth, container, false);
        return mView;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(Fragment4.class) == null) {
            // ShopFragment是flow包里的
            loadRootFragment(R.id.fl_fourth_container, Fragment4.newInstance());
        }
    }

    public void onBackToFirstFragment() {
        _mBackToFirstListener.onBackToFirstFragment();
    }

}
