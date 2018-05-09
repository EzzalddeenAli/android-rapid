package com.lh.rapid.ui.fragment3;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.frameproj.library.util.MathUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.Cart;
import com.lh.rapid.bean.CirLord;
import com.lh.rapid.bean.StroeInfo;
import com.lh.rapid.util.StrUtils;
import com.lh.rapid.util.Utils;

import java.util.List;



/**
 * Created by ceshi on 2018/1/11.
 */

public class ShoppingCartTwoAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CirLord> cirLords;
    private ICartChangeListener changeListener;

    public ShoppingCartTwoAdapter(Context context, List<CirLord> cirLords) {
        this.context = context;
        this.cirLords = cirLords;
    }

    @Override
    public int getGroupCount() {
        return cirLords.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return cirLords.get(i).cartList.size();
    }

    @Override
    public Object getGroup(int i) {
        return cirLords.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return cirLords.get(i).cartList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        GroupViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_cart_groupt,null);
            holder=new GroupViewHolder();
            holder.tvName= (TextView) view.findViewById(R.id.tv_cart_gp_name);

            view.setTag(holder);
        }else {
            holder= (GroupViewHolder) view.getTag();
        }

        CirLord group = cirLords.get(i);
        holder.tvName.setText(group.designation);

        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        ChildViewHolder holder;
        if (view==null) {
            view= LayoutInflater.from(context).inflate(R.layout.item_cart_products,null);
            holder=new ChildViewHolder();
            holder.iv_cart_pd_pic= (ImageView) view.findViewById(R.id.iv_cart_pd_pic);
            holder.tv_cart_pd_state= (TextView) view.findViewById(R.id.tv_cart_pd_state);
            holder.tv_cart_pd_name= (TextView) view.findViewById(R.id.tv_cart_pd_name);
            holder.tv_cart_pd_weight= (TextView) view.findViewById(R.id.tv_cart_pd_weight);
            holder.tv_cart_pd_price= (TextView) view.findViewById(R.id.tv_cart_pd_price);
            holder.tv_cart_pd_num= (TextView) view.findViewById(R.id.tv_cart_pd_num);

            view.setTag(holder);
        }else {
            holder= (ChildViewHolder) view.getTag();
        }

        final Cart cart = cirLords.get(i).cartList.get(i1);
        if (cart.saleType==1){
            holder.tv_cart_pd_name.setText(Utils.getSpannableString(context,cart.name));
        }else {
            holder.tv_cart_pd_name.setText(cart.name);
        }
        holder.tv_cart_pd_weight.setText(cart.weight);
        if (cart.promotionPrice != null && cart.promotionPrice < cart.price && cart.promotionPrice > 0)
            cart.price = cart.promotionPrice;
        holder.tv_cart_pd_weight.setText(Utils.getSpannableString(MathUtil.point2decimal(cart.price)));

        if (cart.saleStatus == 1) {
            holder.tv_cart_pd_num.setVisibility(View.VISIBLE);
//            holder.ivSellOut.setVisibility(View.GONE);
            holder.tv_cart_pd_state.setVisibility(View.GONE);
//            if (cart.isCheck) {
//                holder.cbCheck.setSelected(true);
//            } else {
//                holder.cbCheck.setSelected(false);
//            }
//        } else {
            holder.tv_cart_pd_num.setVisibility(View.GONE);
//            holder.ivSellOut.setVisibility(View.VISIBLE);
            if (cart.saleStatus == 2) {
                holder.tv_cart_pd_state.setText("已售完");
            } else if (cart.saleStatus == 3) {
                holder.tv_cart_pd_state.setText("已下架");
            }
            holder.tv_cart_pd_state.setVisibility(View.VISIBLE);
        }

        holder.tv_cart_pd_num.setText(cart.count);
        String imgUrl = cart.imageUrl;
        imgUrl = StrUtils.urlEncode(imgUrl, "utf-8");

        if (!TextUtils.isEmpty(imgUrl))
            ImageLoaderUtil.getInstance().loadImage(imgUrl, R.mipmap.no_pic_homepage_sale2, holder.iv_cart_pd_pic);
//            Picasso.with(context).load(imgUrl)
//                    .placeholder(R.drawable.no_pic_homepage_sale2)
//                    .config(Bitmap.Config.RGB_565)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .fit().into(holder.iv_cart_pd_pic);
        holder.iv_cart_pd_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeListener != null)
                    changeListener.onItemClick(i, i1);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder{
        TextView tvName;
    }

    class ChildViewHolder{
        ImageView iv_cart_pd_pic;
        TextView tv_cart_pd_state,tv_cart_pd_name,tv_cart_pd_weight,
                tv_cart_pd_price,tv_cart_pd_num;
    }


    /**
     * 点击组选择
     *
     * @param list
     * @param groudPosition
     * @return
     */
    public void selectGroup(List<StroeInfo> list, int groudPosition) {
        boolean isSelected = !(list.get(groudPosition).isChecked);
        list.get(groudPosition).isChecked = isSelected;
        for (int i = 0; i < list.get(groudPosition).cartList.size(); i++) {
            list.get(groudPosition).cartList.get(i).isCheck = isSelected;
        }
    }

    /**
     * 单选一个，需要判断整个组的标志，整个族的标志，是否被全选，取消，则
     * 除了选择全部和选择单个可以单独设置背景色，其他都是通过改变值，然后notify；
     *
     * @param list
     * @param groudPosition
     * @param childPosition
     * @return 是否选择全部
     */
    public void selectOne(List<StroeInfo> list, int groudPosition, int childPosition) {
//        list.get(groudPosition).cartList.get(childPosition).isCheck = !(list.get(groudPosition).cartList.get(childPosition).isCheck);//单个图标的处理
        list.get(groudPosition).isChecked = isSelectAllChild(list.get(groudPosition).cartList);//组图标的处理
    }

    /**
     * 组内所有子选项是否全部被选中
     *
     * @param list
     * @return
     */
    private boolean isSelectAllChild(List<Cart> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isSelectGroup = list.get(i).isCheck;
            if (!isSelectGroup) {
                return false;
            }
        }
        return true;
    }

    public void setType(String type) {
        mType = type;
    }

    private String mType = Fragment3.EDITE;

    private boolean isEdite() {
        return Fragment3.EDITE.equalsIgnoreCase(mType);
    }


    public void setChangeListener(ICartChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface ICartChangeListener {
        boolean onCartChange(int group, int child, int countChange);

        void onCheck(int group, int child);

        void onItemClick(int groupPosition, int childPosition);
    }
}
