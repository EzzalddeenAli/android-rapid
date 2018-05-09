package com.lh.rapid.ui.fragment1;

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
public class FirstFragment extends BaseMainFragment {

    public static FirstFragment newInstance() {

        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(Fragment1.class) == null) {
            loadRootFragment(R.id.fl_first_container, Fragment1.newInstance());
        }
    }
}
