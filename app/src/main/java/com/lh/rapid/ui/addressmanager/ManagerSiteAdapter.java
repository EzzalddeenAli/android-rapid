package com.lh.rapid.ui.addressmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.inter.OnItemClickListener;
import com.daimajia.swipe.SwipeLayout;
import com.lh.rapid.R;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.inter.OnCartGoodsDelete;

import java.util.List;

public class ManagerSiteAdapter extends RecyclerView.Adapter<ManagerSiteAdapter.MyViewHolder> {

    private Context context;
    private List<AddressListBean> list;
    private itemAddressChangeListener changeListener;

    public ManagerSiteAdapter(Context context, List<AddressListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_manager_site, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_site_address.setText(list.get(position).getDetailAddress());
        holder.tv_site_name.setText(list.get(position).getReceiveGoodsName());
        holder.tv_site_phone.setText(list.get(position).getPhone());
        holder.iv_site_address.setImageResource(list.get(position).getImage());
        holder.view_line.setBackgroundResource(R.drawable.line_bg);
        holder.ll_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeListener.changeListener(position);
            }
        });

        holder.swipeLayout.setLeftSwipeEnabled(true);
        //删除订单
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCartGoodsDelete.cartGoodsDelete(list.get(position).getAddressId());
            }
        });
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });

    }

    private OnCartGoodsDelete mOnCartGoodsDelete;

    public void setOnCartGoodsDelete(OnCartGoodsDelete onCartGoodsDelete) {
        mOnCartGoodsDelete = onCartGoodsDelete;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_site_change, tv_site_address, tv_site_name, tv_site_phone;
        LinearLayout ll_change_address;
        View view_line;
        ImageView iv_site_address;
        SwipeLayout swipeLayout;
        Button btn_delete;
        RelativeLayout rl_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_site_address = (TextView) itemView.findViewById(R.id.tv_site_address);
            tv_site_change = (TextView) itemView.findViewById(R.id.tv_site_change);
            tv_site_name = (TextView) itemView.findViewById(R.id.tv_site_name);
            tv_site_phone = (TextView) itemView.findViewById(R.id.tv_site_phone);
            ll_change_address = (LinearLayout) itemView.findViewById(R.id.ll_change_address);
            iv_site_address = (ImageView) itemView.findViewById(R.id.iv_site_address);
            view_line = itemView.findViewById(R.id.view_line);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }

    }

    public void setChangeListener(itemAddressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface itemAddressChangeListener {
        void changeListener(int position);
    }

}
