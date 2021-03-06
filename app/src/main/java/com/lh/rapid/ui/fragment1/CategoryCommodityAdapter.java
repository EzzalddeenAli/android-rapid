package com.lh.rapid.ui.fragment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.HomePageBean;

import java.util.List;


/**
 * Created by ceshi on 2017/12/19.
 */

public class CategoryCommodityAdapter extends RecyclerView.Adapter<CategoryCommodityAdapter.MyViewHolder> {

    private Fragment1 mFragment1;
    private Context context;
    private List<HomePageBean.CategoryListsBean.GoodListBean> list;
    private CategoryCommNameAdapter nameAdapter;
    private rvCateItemClickLitener itemClickLitener;

    public CategoryCommodityAdapter(Fragment1 fragment1, Context context, List<HomePageBean.CategoryListsBean.GoodListBean> list, CategoryCommNameAdapter nameAdapter) {
        mFragment1 = fragment1;
        this.context = context;
        this.list = list;
        this.nameAdapter = nameAdapter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category_comm, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_cart_comm_name.setText(list.get(i).getGoodsName());
        myViewHolder.tv_cart_comm_norm.setText(list.get(i).getWeight());
        myViewHolder.tv_cart_comm_price.setText(list.get(i).getGoodsPrice() + "");
        myViewHolder.tv_cart_comm_num.setText(list.get(i).getCounts() + "");

        if (list.get(i) != null) {
            if (list.get(i).getCounts() < 1) {
                myViewHolder.tv_cart_comm_num.setVisibility(View.INVISIBLE);
                myViewHolder.iv_cart_comm_minus.setVisibility(View.INVISIBLE);
                nameAdapter.notifyDataSetChanged();
            } else {
                myViewHolder.tv_cart_comm_num.setVisibility(View.VISIBLE);
                myViewHolder.iv_cart_comm_minus.setVisibility(View.VISIBLE);
                myViewHolder.tv_cart_comm_num.setText(String.valueOf(list.get(i).getCounts()));
                nameAdapter.notifyDataSetChanged();
            }
        } else {
            myViewHolder.tv_cart_comm_num.setVisibility(View.VISIBLE);
            myViewHolder.iv_cart_comm_minus.setVisibility(View.VISIBLE);
        }

        if (list.get(i).getGoodsImg() != null) {
            //            Glide
            //                    .with(context)
            //                    .load(list.get(i).getGoodsImg())
            //                    .centerCrop()
            //                    .placeholder(R.mipmap.icon_logo_image_default)
            //                    .crossFade()
            //                    .into(myViewHolder.iv_cart_comm);
            ImageLoaderUtil.getInstance().loadImage(list.get(i).getGoodsImg(), myViewHolder.iv_cart_comm);
        }


        myViewHolder.iv_cart_comm_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = mFragment1.getSelectedItemCountById(list.get(i).getGoodsId());
                Log.i("fyg", "iv_add" + String.valueOf(count));
                if (count < 1) {
                    myViewHolder.iv_cart_comm_minus.setAnimation(getShowAnimation());
                    myViewHolder.tv_cart_comm_num.setVisibility(View.VISIBLE);
                    myViewHolder.iv_cart_comm_minus.setVisibility(View.VISIBLE);
                }

                mFragment1.handlerCarNum(1, i, true);
                nameAdapter.notifyDataSetChanged();

                int[] loc = new int[2];
                myViewHolder.iv_cart_comm_add.getLocationInWindow(loc);
                for (int i = 0; i < loc.length; i++) {
                    Log.i("fyg", String.valueOf(loc[i]));
                }
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);
                ball.setImageResource(R.drawable.empty);


                myViewHolder.iv_cart_comm_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = mFragment1.getSelectedItemCountById(i);
                        Log.i("fyg", "iv_remove" + String.valueOf(count));
                        if (count < 2) {
                            myViewHolder.iv_cart_comm_minus.setAnimation(getHiddenAnimation());
                            myViewHolder.iv_cart_comm_minus.setVisibility(View.GONE);
                            myViewHolder.tv_cart_comm_num.setVisibility(View.GONE);
                        }
                        mFragment1.handlerCarNum(0, i, true);
                        nameAdapter.notifyDataSetChanged();
                    }
                });
            }

        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickLitener.itemClick(i);
            }
        });
    }

    //显示减号的动画
    private Animation getShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    //隐藏减号的动画
    private Animation getHiddenAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_cart_comm_name, tv_cart_comm_norm, tv_cart_comm_price, tv_cart_comm_num;
        ImageView iv_cart_comm_minus, iv_cart_comm_add, iv_cart_comm;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_cart_comm_name = (TextView) itemView.findViewById(R.id.tv_cart_comm_name);
            tv_cart_comm_norm = (TextView) itemView.findViewById(R.id.tv_cart_comm_norm);
            tv_cart_comm_price = (TextView) itemView.findViewById(R.id.tv_cart_comm_price);
            tv_cart_comm_num = (TextView) itemView.findViewById(R.id.tv_cart_comm_num);

            iv_cart_comm = (ImageView) itemView.findViewById(R.id.iv_cart_comm);
            iv_cart_comm_add = (ImageView) itemView.findViewById(R.id.iv_cart_comm_add);
            iv_cart_comm_minus = (ImageView) itemView.findViewById(R.id.iv_cart_comm_minus);
        }
    }

    public void setItemClickLitener(rvCateItemClickLitener itemClickLitener) {
        this.itemClickLitener = itemClickLitener;
    }

    public interface rvCateItemClickLitener {
        void itemClick(int position);
    }

}
