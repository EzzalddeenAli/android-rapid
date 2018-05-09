package com.lh.rapid.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lh.rapid.bean.HomePageBean;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by lh on 2018/5/2.
 */

public class MyGlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object bannerItem, ImageView imageView) {
        Glide.with(context).load(((HomePageBean.BnTopBean) bannerItem).getBnImgUrl()).into(imageView);
    }

}