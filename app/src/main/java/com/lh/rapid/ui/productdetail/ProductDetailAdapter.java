package com.lh.rapid.ui.productdetail;

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

import com.lh.rapid.R;
import com.lh.rapid.bean.Hover;

import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.MyViewHolder> {

    private Context context;
    private List<Hover> list;

    public ProductDetailAdapter(Context context, List<Hover> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_product_detail, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_product_detail_name.setText(list.get(position).getName());
        holder.tv_product_gift.setText(list.get(position).getDiscounts());
        holder.tv_product_price.setText(list.get(position).getPrice());

        if (list.get(position) != null) {
            if (list.get(position).getNum() < 1) {
                holder.tv_product_num.setVisibility(View.INVISIBLE);
                holder.iv_product_minus.setVisibility(View.INVISIBLE);
            } else {
                holder.iv_product_minus.setVisibility(View.VISIBLE);
                holder.tv_product_num.setVisibility(View.VISIBLE);
                holder.tv_product_num.setText(list.get(position).getNum()+"");
            }
        } else {
            holder.iv_product_add.setVisibility(View.VISIBLE);
            holder.iv_product_minus.setVisibility(View.VISIBLE);
        }


        holder.iv_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = ((ProductDetailActivity) context).getSelectedItemCountById(list.get(position).getProduct_id());
                if (count < 1) {
                    holder.iv_product_minus.setAnimation(getShowAnimation());
                    holder.tv_product_num.setVisibility(View.VISIBLE);
                    holder.iv_product_minus.setVisibility(View.VISIBLE);
                }
                ((ProductDetailActivity) context).handlerCarNum(1, list.get(position), true);

                int[] loc = new int[2];
                holder.iv_product_add.getLocationInWindow(loc);
                for (int i = 0; i < loc.length; i++) {
                    Log.i("fyg", String.valueOf(loc[i]));
                }
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);
                ball.setImageResource(R.drawable.empty);
                ((ProductDetailActivity) context).setAnim(ball, startLocation);// 开始执行动画


                holder.iv_product_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = ((ProductDetailActivity) context).getSelectedItemCountById(list.get(position).getProduct_id());
                        Log.i("fyg", "iv_remove" + String.valueOf(count));
                        if (count < 2) {
                            holder.iv_product_minus.setAnimation(getHiddenAnimation());
                            holder.iv_product_minus.setVisibility(View.GONE);
                            holder.tv_product_num.setVisibility(View.GONE);
                        }
                        ((ProductDetailActivity) context).handlerCarNum(0, list.get(position), true);
                    }
                });
            }
        });
    }

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

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_product_detail_name, tv_product_gift,
                tv_product_price, tv_product_num;

        ImageView iv_product_add, iv_product_minus;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_product_detail_name = (TextView) itemView.findViewById(R.id.tv_product_detail_name);
            tv_product_gift = (TextView) itemView.findViewById(R.id.tv_product_gift);
            tv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            tv_product_num = (TextView) itemView.findViewById(R.id.tv_product_num);

            iv_product_add = (ImageView) itemView.findViewById(R.id.iv_product_add);
            iv_product_minus = (ImageView) itemView.findViewById(R.id.iv_product_minus);
        }
    }
}
