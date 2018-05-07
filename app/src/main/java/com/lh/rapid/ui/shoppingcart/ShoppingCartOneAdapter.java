package com.lh.rapid.ui.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.frameproj.library.util.MathUtil;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.Cart;
import com.lh.rapid.bean.StroeInfo;
import com.lh.rapid.ui.widget.SelectNumberView;
import com.lh.rapid.util.StrUtils;
import com.lh.rapid.util.Utils;

import java.util.List;


/**
 * Created by ceshi on 2018/1/9.
 */

public class ShoppingCartOneAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<StroeInfo> list;
    private ICartChangeListener changeListener;

    public ShoppingCartOneAdapter(Context context, List<StroeInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).cartList.size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).cartList.get(i1);
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

        GroupViewHolder groupViewHolder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_cart_group,null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.cbChoose = (CheckBox) view.findViewById(R.id.cb_cart_choose);
            groupViewHolder.name = (TextView) view.findViewById(R.id.tv_quan_zhu);
            groupViewHolder.ivGo = (ImageView) view.findViewById(R.id.iv_cart_go);

            view.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }

        StroeInfo group = list.get(i);
        groupViewHolder.name.setText(group.designation);
        groupViewHolder.ivGo.setImageResource(R.mipmap.icon_mine_right_b);
        groupViewHolder.cbChoose.setSelected(group.isChecked);
        groupViewHolder.cbChoose.setTag(i);
        groupViewHolder.cbChoose.setOnClickListener(listener);
        if (isEdite()){
            for (Cart cart:group.cartList) {
                if (cart.saleStatus==1){
                    groupViewHolder.cbChoose.setEnabled(true);
                    continue;
                }else {
                    groupViewHolder.cbChoose.setEnabled(false);
                    break;
                }
            }
        }else {
            groupViewHolder.cbChoose.setEnabled(true);
        }
        groupViewHolder.ivGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        final ChildViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_cart_product,null);
            holder = new ChildViewHolder();
            holder.cbCheck = (CheckBox) view.findViewById(R.id.iv_cart_item_check);
            holder.ivPic = (ImageView) view.findViewById(R.id.iv_cart_item_pic);
            holder.tvName = (TextView) view.findViewById(R.id.tv_cart_item_name);
            holder.tvWeight = (TextView) view.findViewById(R.id.tv_cart_item_weight);
            holder.tvPrice = (TextView) view.findViewById(R.id.tv_cart_item_price);
            holder.tvState = (TextView) view.findViewById(R.id.tv_cart_item_state);
            holder.numberView = (SelectNumberView) view.findViewById(R.id.sn_cart_item);

            view.setTag(holder);
        }else {
            holder= (ChildViewHolder) view.getTag();
        }

        final Cart cart = list.get(i).cartList.get(i1);
        if (cart.saleType==1){
            holder.tvName.setText(Utils.getSpannableString(context,cart.name));
        }else {
            holder.tvName.setText(cart.name);
        }
        holder.tvWeight.setText(cart.weight);
        if (cart.promotionPrice != null && cart.promotionPrice < cart.price && cart.promotionPrice > 0)
            cart.price = cart.promotionPrice;
        holder.tvPrice.setText(Utils.getSpannableString(MathUtil.point2decimal(cart.price)));

        if (cart.saleStatus == 1) {
            holder.numberView.setVisibility(View.VISIBLE);
//            holder.ivSellOut.setVisibility(View.GONE);
            holder.tvState.setVisibility(View.GONE);
            if (cart.isCheck) {
                holder.cbCheck.setSelected(true);
            } else {
                holder.cbCheck.setSelected(false);
            }
        } else {
            holder.numberView.setVisibility(View.GONE);
//            holder.ivSellOut.setVisibility(View.VISIBLE);
            if (cart.saleStatus == 2) {
                holder.tvState.setText("已售完");
            } else if (cart.saleStatus == 3) {
                holder.tvState.setText("已下架");
            }
            holder.tvState.setVisibility(View.VISIBLE);
            if (isEdite()) {
                holder.cbCheck.setSelected(false);
            } else {
                if (cart.isCheck) {
                    holder.cbCheck.setSelected(true);
                } else {
                    holder.cbCheck.setSelected(false);
                }
            }
        }

        holder.numberView.setMinLimit(cart.minOrderCnt);
//        holder.selectNumberView.setMaxLimit(cart.quantity);
        holder.numberView.setQuantity(cart.count);
        holder.numberView.setSelectCallback2(new SelectNumberView.ISelectCallback2() {

            @Override
            public void onResult(int qualitity) {
                if (changeListener != null) {
                    int preCount = cart.count;   //记住之前的数量
                    cart.count = qualitity;
                    boolean isSuccess = changeListener.onCartChange(i, i1, qualitity - preCount);
                    if (isSuccess) {       //成功修改数量后，修改数值显示
                        holder.numberView.setQuantity(qualitity);
                    } else {            //否则，恢复购物车数量
                        cart.count = preCount;
                        holder.numberView.setQuantity(preCount);
                    }
                } else {
                    holder.numberView.setQuantity(qualitity);
                }
            }

            @Override
            public void onMaxQuantity() {
                if (holder.numberView.getQuantity() == cart.veg_sale_maxnum) {
                    ToastUtil.showToast("购物车单个商品最大数量不能超过" + cart.veg_sale_maxnum);
                } else {
                    ToastUtil.showToast("已达到最大库存数");
                }
            }

            @Override
            public void onMinQuantity() {
                ToastUtil.showToast("已达到最小购买数量" + cart.minOrderCnt);
            }

        });

        holder.cbCheck.setSelected(cart.isCheck);
        holder.cbCheck.setTag(i + "," + i1);
        holder.cbCheck.setOnClickListener(listener);
        if (isEdite()) {
            if (cart.saleStatus == 1)
                holder.cbCheck.setEnabled(true);
            else
                holder.cbCheck.setEnabled(false);
        } else {
            holder.cbCheck.setEnabled(true);
        }
        String imgUrl = cart.imageUrl;
        imgUrl = StrUtils.urlEncode(imgUrl, "utf-8");

        if (!TextUtils.isEmpty(imgUrl))
            ImageLoaderUtil.getInstance().loadImage(imgUrl, R.mipmap.no_pic_homepage_sale2, holder.ivPic);
//            Picasso.with(context).load(imgUrl)
//                    .placeholder(R.drawable.no_pic_homepage_sale2)
//                    .config(Bitmap.Config.RGB_565)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .fit().into(holder.ivPic);
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
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
        CheckBox cbChoose;
        TextView name;
        ImageView ivGo;
    }

    class ChildViewHolder{

        CheckBox cbCheck;
        ImageView ivPic;
        TextView tvName,tvWeight,tvPrice,tvState;
        SelectNumberView numberView;
    }

    View.OnClickListener listener = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_cart_choose:
                    int groupPosition3 = Integer.parseInt(String.valueOf(v.getTag()));
                    if (changeListener != null)
                        changeListener.onCheck(groupPosition3, -1);
//                    selectGroup(mCartInfo, groupPosition3);
//                    notifyDataSetChanged();
                    break;
                case R.id.iv_cart_item_check:
                    String tag = String.valueOf(v.getTag());
                    if (tag.contains(",")) {
                        String s[] = tag.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        if (changeListener != null)
                            changeListener.onCheck(groupPosition, childPosition);
                        selectOne(list, groupPosition, childPosition);
//                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


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

    private String mType = ShoppingCartActivity.EDITE;

    private boolean isEdite() {
        return ShoppingCartActivity.EDITE.equalsIgnoreCase(mType);
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
