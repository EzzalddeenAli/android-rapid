package com.lh.rapid.ui.fragment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.bean.HomePageBean;

import java.util.List;

/**
 * Created by ceshi on 2017/12/19.
 */

public class CategoryCommNameAdapter extends RecyclerView.Adapter<CategoryCommNameAdapter.MyViewHolder> {

    private Fragment1 mFragment1;
    private Context context;
    List<HomePageBean.CategoryListsBean> categoryLists;

    private rvItemClickLitener itemClickLitener;

    int selection = 0;

    public CategoryCommNameAdapter(Fragment1 fragment1, Context context, List<HomePageBean.CategoryListsBean> categoryLists) {
        mFragment1 = fragment1;
        this.context = context;
        this.categoryLists = categoryLists;
    }

    public void setItemClickLitener(rvItemClickLitener itemClickLitener) {
        this.itemClickLitener = itemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category_name, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_cart_form_name.setText(categoryLists.get(i).getCategoryName());
//        myViewHolder.tv_cart_form_line.setBackgroundResource(categoryLists.get(i).getLine());
//        myViewHolder.ll_item_name.setBackgroundColor(categoryLists.get(i).getColor());

        if (i == selection) {
            //            myViewHolder.tv_cart_form_name.setBackgroundResource(R.drawable.cart_form_line);
            myViewHolder.tv_cart_form_name.setTextColor(context.getResources().getColor(R.color.tab_color));
            //            myViewHolder.tv_cart_form_line.setBackground(context.getDrawable(R.drawable.new_green_line));
            myViewHolder.tv_cart_form_line.setBackgroundResource(R.drawable.new_green_line);
            myViewHolder.ll_item_name.setBackgroundColor(context.getResources().getColor(R.color.white));

        } else {

            //            myViewHolder.tv_cart_form_name.setBackgroundResource(R.drawable.empty);
            myViewHolder.tv_cart_form_name.setTextColor(context.getResources().getColor(R.color.gray_text));
            myViewHolder.ll_item_name.setBackgroundColor(context.getResources().getColor(R.color.home_gray));
            myViewHolder.tv_cart_form_line.setBackgroundResource(R.drawable.empty);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickLitener.itemClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryLists == null ? 0 : categoryLists.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_cart_form_name, tv_cart_form_line;
        LinearLayout ll_item_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cart_form_name = (TextView) itemView.findViewById(R.id.tv_cart_form_name);
            tv_cart_form_line = (TextView) itemView.findViewById(R.id.tv_cart_form_line);
            ll_item_name = (LinearLayout) itemView.findViewById(R.id.ll_item_name);
        }
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public interface rvItemClickLitener {
        void itemClick(int position);
    }
}
