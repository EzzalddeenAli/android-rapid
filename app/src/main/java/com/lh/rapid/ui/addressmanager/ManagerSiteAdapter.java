package com.lh.rapid.ui.addressmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.rapid.R;
import com.lh.rapid.bean.Site;

import java.util.List;

public class ManagerSiteAdapter extends RecyclerView.Adapter<ManagerSiteAdapter.MyViewHolder> {

    private Context context;
    private List<Site> list;
    private itemAddressChangeListener changeListener;

    public ManagerSiteAdapter(Context context, List<Site> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_manager_site,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.tv_site_address.setText(list.get(position).getSite());
        holder.tv_site_name.setText(list.get(position).getName());
        holder.tv_site_phone.setText(list.get(position).getPhone());
//        holder.tv_site_change.setText(list.get(position).getChange());
        holder.iv_site_address.setImageResource(list.get(position).getImage());

        holder.view_line.setBackgroundResource(R.drawable.line_bg);

        holder.ll_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeListener.changeListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_site_change,tv_site_address,tv_site_name,tv_site_phone;
        LinearLayout ll_change_address;
        View view_line;
        ImageView iv_site_address;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_site_address= (TextView) itemView.findViewById(R.id.tv_site_address);
            tv_site_change= (TextView) itemView.findViewById(R.id.tv_site_change);
            tv_site_name= (TextView) itemView.findViewById(R.id.tv_site_name);
            tv_site_phone= (TextView) itemView.findViewById(R.id.tv_site_phone);
            ll_change_address= (LinearLayout) itemView.findViewById(R.id.ll_change_address);
            iv_site_address= (ImageView) itemView.findViewById(R.id.iv_site_address);
            view_line=itemView.findViewById(R.id.view_line);
        }
    }

    public void setChangeListener(itemAddressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface itemAddressChangeListener{
        void changeListener(int position);
    }

}
