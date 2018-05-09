package com.android.frameproj.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.frameproj.library.R;
import com.android.frameproj.library.util.ScreenUtil;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends LinearLayout {
    private ImageView mIcon;
    private Context mContext;
    private int mTabPosition = -1;
    private TextView mTextView;

    public BottomBarTab(Context context, @DrawableRes int icon,@DrawableRes int icon_grey, String text) {
        this(context, null, icon,icon_grey,text);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon, int icon_grey, String text) {
        this(context, attrs, 0, icon,icon_grey,text);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, int icon_grey, String text) {
        super(context, attrs, defStyleAttr);
        init(context, icon,icon_grey,text);
    }

    private int icon ;
    private int icon_grey ;
    private void init(Context context, int icon, int icon_grey, String text) {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, ScreenUtil.dipToPx(context,3),0, ScreenUtil.dipToPx(context,3));
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        params.setMargins(0,5,0,0);
        mIcon.setImageResource(icon_grey);
        mIcon.setLayoutParams(params);
        addView(mIcon);

        mTextView = new TextView(context);
        mTextView.setText(text);
        LayoutParams paramsText = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextView.setSingleLine();
        mTextView.setLayoutParams(paramsText);
        mTextView.setGravity(Gravity.CENTER);
        paramsText.setMargins(0,5,0,5);
        mTextView.setTextColor(ContextCompat.getColor(context, R.color.library_text_color_6));
        mTextView.setTextSize(10);
        addView(mTextView);

        this.icon = icon;
        this.icon_grey = icon_grey;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setImageResource(icon);
            mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.library_red));
        } else {
            mIcon.setImageResource(icon_grey);
            mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.library_text_color_6));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }

}