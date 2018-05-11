package com.lh.rapid.util;

import android.content.Context;
import android.widget.ImageView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.HomePageBean;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by lh on 2018/5/2.
 */

public class MyGlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object bannerItem, ImageView imageView) {
        //        Glide.with(context).load(((HomePageBean.BnTopBean) bannerItem).getBnImgUrl()).into(imageView);
        ImageLoaderUtil.getInstance().loadImage(((HomePageBean.BnTopBean) bannerItem).getBnImgUrl(),
                R.mipmap.no_pic_homepage_sale2, imageView);
    }

}