package com.lh.rapid.ui.shoppingcart;

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
import com.lh.rapid.bean.StroeInfo;
import com.lh.rapid.util.StrUtils;
import com.lh.rapid.util.Utils;

import java.util.List;


/**
 * Created by ceshi on 2018/1/12.
 */

public class ShoppingCartThreeAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<StroeInfo> list;
    private ICartChangeListener changeListener;

    public ShoppingCartThreeAdapter(Context context, List<StroeInfo> list) {
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
        final GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart_groupts, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.iv_cart_gpt_choose = (ImageView) view.findViewById(R.id.iv_cart_gpt_choose);
            groupViewHolder.tv_cart_name_quan = (TextView) view.findViewById(R.id.tv_cart_name_quan);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }

        //        if (){}
        final StroeInfo group = list.get(i);
        groupViewHolder.tv_cart_name_quan.setText(group.designation);
        groupViewHolder.iv_cart_gpt_choose.setSelected(group.isChecked);
        groupViewHolder.iv_cart_gpt_choose.setTag(i);
        groupViewHolder.iv_cart_gpt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdite()) {
                    for (Cart cart : group.cartList) {
                        if (cart.saleStatus == 1) {
                            groupViewHolder.iv_cart_gpt_choose.setEnabled(true);
                            continue;
                        } else {
                            groupViewHolder.iv_cart_gpt_choose.setEnabled(false);
                            break;
                        }
                    }
                } else {
                    groupViewHolder.iv_cart_gpt_choose.setEnabled(true);
                }
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart_productst, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.iv_cart_item_checks = (ImageView) view.findViewById(R.id.iv_cart_item_checks);
            childViewHolder.iv_cart_pd_pics = (ImageView) view.findViewById(R.id.iv_cart_pd_pics);
            childViewHolder.tv_cart_pd_states = (TextView) view.findViewById(R.id.tv_cart_pd_states);
            childViewHolder.tv_cart_pd_names = (TextView) view.findViewById(R.id.tv_cart_pd_names);
            childViewHolder.tv_cart_pd_weights = (TextView) view.findViewById(R.id.tv_cart_pd_weights);
            childViewHolder.tv_cart_pd_prices = (TextView) view.findViewById(R.id.tv_cart_pd_prices);
            childViewHolder.tv_cart_pd_nums = (TextView) view.findViewById(R.id.tv_cart_pd_nums);

            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }

        Cart cart = list.get(i).cartList.get(i1);
        if (cart.saleType == 1) {
            childViewHolder.tv_cart_pd_names.setText(Utils.getSpannableString(context, cart.name));
        } else {
            childViewHolder.tv_cart_pd_names.setText(cart.name);
        }
        childViewHolder.tv_cart_pd_weights.setText(cart.weight);
        if (cart.promotionPrice != null && cart.promotionPrice < cart.price && cart.promotionPrice > 0)
            cart.price = cart.promotionPrice;
        childViewHolder.tv_cart_pd_prices.setText(Utils.getSpannableString(MathUtil.point2decimal(cart.price)));

        if (cart.saleStatus == 1) {
            childViewHolder.tv_cart_pd_nums.setVisibility(View.VISIBLE);
            //            holder.ivSellOut.setVisibility(View.GONE);
            childViewHolder.tv_cart_pd_states.setVisibility(View.GONE);
            if (cart.isCheck) {
                childViewHolder.iv_cart_item_checks.setSelected(true);
            } else {
                childViewHolder.iv_cart_item_checks.setSelected(false);
            }
        } else {
            childViewHolder.tv_cart_pd_nums.setVisibility(View.GONE);
            //            holder.ivSellOut.setVisibility(View.VISIBLE);
            if (cart.saleStatus == 2) {
                childViewHolder.tv_cart_pd_states.setText("已售完");
            } else if (cart.saleStatus == 3) {
                childViewHolder.tv_cart_pd_states.setText("已下架");
            }
            childViewHolder.tv_cart_pd_states.setVisibility(View.VISIBLE);
            if (isEdite()) {
                childViewHolder.iv_cart_item_checks.setSelected(false);
            } else {
                if (cart.isCheck) {
                    childViewHolder.iv_cart_item_checks.setSelected(true);
                } else {
                    childViewHolder.iv_cart_item_checks.setSelected(false);
                }
            }
        }

        childViewHolder.tv_cart_pd_nums.setText("X" + cart.count);

        childViewHolder.iv_cart_item_checks.setSelected(cart.isCheck);
        childViewHolder.iv_cart_item_checks.setTag(i + "," + i1);
        childViewHolder.iv_cart_item_checks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int groupPosition3 = Integer.parseInt(String.valueOf(view.getTag()));
                if (changeListener != null)
                    changeListener.onCheck(groupPosition3, -1);
            }
        });

        if (isEdite()) {
            if (cart.saleStatus == 1)
                childViewHolder.iv_cart_item_checks.setEnabled(true);
            else
                childViewHolder.iv_cart_item_checks.setEnabled(false);
        } else {
            childViewHolder.iv_cart_item_checks.setEnabled(true);
        }
        String imgUrl = cart.imageUrl;
        imgUrl = StrUtils.urlEncode(imgUrl, "utf-8");
        if (!TextUtils.isEmpty(imgUrl))
            ImageLoaderUtil.getInstance().loadImage(imgUrl, R.mipmap.no_pic_homepage_sale2, childViewHolder.iv_cart_pd_pics);
//                    Picasso.with(context).load(imgUrl)
//                            .placeholder(R.drawable.no_pic_homepage_sale2)
//                            .config(Bitmap.Config.RGB_565)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                            .fit().into(childViewHolder.iv_cart_pd_pics);
        childViewHolder.iv_cart_pd_pics.setOnClickListener(new View.OnClickListener() {
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


    class GroupViewHolder {
        ImageView iv_cart_gpt_choose;
        TextView tv_cart_name_quan;
    }

    class ChildViewHolder {
        ImageView iv_cart_item_checks, iv_cart_pd_pics;
        TextView tv_cart_pd_states, tv_cart_pd_names, tv_cart_pd_weights,
                tv_cart_pd_prices, tv_cart_pd_nums;
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
