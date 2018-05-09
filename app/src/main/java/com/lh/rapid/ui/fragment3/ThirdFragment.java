package com.lh.rapid.ui.fragment3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseMainFragment;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class ThirdFragment extends BaseMainFragment {

    public static ThirdFragment newInstance() {

        Bundle args = new Bundle();

        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(Fragment3.class) == null) {
            // ShopFragment是flow包里的
            loadRootFragment(R.id.fl_third_container, Fragment3.newInstance());
        }
    }

    public void onBackToFirstFragment() {
        _mBackToFirstListener.onBackToFirstFragment();
    }

}
