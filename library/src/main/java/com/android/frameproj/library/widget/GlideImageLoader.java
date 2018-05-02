package com.android.frameproj.library.widget;

import android.content.Context;
import android.widget.ImageView;

import com.android.frameproj.library.bean.BannerItem;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by lh on 2018/5/2.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object bannerItem, ImageView imageView) {
        Glide.with(context).load(((BannerItem) bannerItem).pic).into(imageView);
    }

}