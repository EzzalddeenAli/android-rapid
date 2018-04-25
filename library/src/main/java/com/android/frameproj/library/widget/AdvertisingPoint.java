package com.android.frameproj.library.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.frameproj.library.R;
import com.android.frameproj.library.util.ScreenUtil;

/**
 * 循环图片小圆点
 */
public class AdvertisingPoint {

    private static final String TAG = "AdvertisingPoint";
    private Context context;
    private View point;

    public AdvertisingPoint(Context context, LinearLayout parent, boolean isFirst) {
        this.context = context;
        point = new View(context);
        point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal));
        parent.addView(point);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) point.getLayoutParams();
        params.width = ScreenUtil.dipToPx(context, 6);
        params.height = ScreenUtil.dipToPx(context, 6);
        if (!isFirst) {
            params.leftMargin = ScreenUtil.dipToPx(context, 10);
        }
        point.setLayoutParams(params);
        Log.i(TAG, "AdvertisingPoint:  ================================ ？？？");
    }

    public void setFocus(boolean isFocus) {
        if (isFocus) {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_select));
        } else {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal));
        }
    }

}