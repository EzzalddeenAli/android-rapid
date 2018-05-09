package com.lh.rapid.ui.fragment2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.inter.OnItemClickListener;
import com.lh.rapid.R;
import com.lh.rapid.bean.CategoryOneLevelBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we-win on 2017/6/29.
 */

public class ClassifyLeftAdapter extends RecyclerView.Adapter<ClassifyLeftAdapter.SortKindHolder> {

    private List<CategoryOneLevelBean> mClassifyLeftBeen;
    private int mCheckPosition;
    private Context mContext;

    public void setCheckPosition(int checkPosition) {
        mCheckPosition = checkPosition;
    }

    public ClassifyLeftAdapter(Context context, List<CategoryOneLevelBean> classifyLeftBeen, int checkPosition) {
        mContext = context;
        this.mClassifyLeftBeen = classifyLeftBeen;
        mCheckPosition = checkPosition;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public SortKindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_classify_left, null);
        return new SortKindHolder(view);
    }

    private SortKindHolder sortKindHolder;

    @Override
    public void onBindViewHolder(final SortKindHolder holder, int position) {
        holder.rlParent.setBackgroundColor(Color.parseColor("#ffffff"));
        if (position == mCheckPosition) {
            changeItem(holder, true);
            sortKindHolder = holder;
        } else {
            changeItem(holder, false);
        }

        holder.textName.setText(mClassifyLeftBeen.get(position).getCategoryName());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sortKindHolder != null) {
                        changeItem(sortKindHolder, false);
                    }
                    sortKindHolder = holder;
                    changeItem(holder, true);
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(v, pos);
                }
            });
        }
        if (position == mClassifyLeftBeen.size()) {
            holder.dividerLine.setVisibility(View.GONE);
        }
    }

    private void changeItem(SortKindHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.redLine.setVisibility(View.VISIBLE);
            holder.dividerLine.setVisibility(View.GONE);
            holder.textName.setTextColor(mContext.getResources().getColor(R.color.md_white_1000));
        } else {
            holder.redLine.setVisibility(View.INVISIBLE);
            holder.dividerLine.setVisibility(View.VISIBLE);
            holder.textName.setTextColor(mContext.getResources().getColor(R.color.library_text_color_3));
        }
    }

    @Override
    public int getItemCount() {
        return mClassifyLeftBeen == null ? 0 : mClassifyLeftBeen.size();
    }

    public class SortKindHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.red_line)
        View redLine;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.rl_parent)
        RelativeLayout rlParent;
        @BindView(R.id.divider_line)
        View dividerLine;

        public SortKindHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
