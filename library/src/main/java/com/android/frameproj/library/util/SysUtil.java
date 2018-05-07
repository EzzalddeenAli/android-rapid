package com.android.frameproj.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

public class SysUtil {

    /**
     * 屏幕宽度
     */
    private static int screenWidth;
    /**
     * 屏幕高度
     */
    private static int screenHeight;
    /**
     * 状态栏高度
     */
    private static int statusHeight;
    /**
     * 状态栏高度
     */
    private static int actionBarHeight;

    private static float densityDpi;

    private static float density;
    private static int sdk_version;

    /**
     * 判断屏幕是否是高密度
     */
    private static boolean largeScreen = true;
    /**
     * 超大屏手机
     */
    private static boolean isXLarge = false;
    /**
     * 1080p
     */
    private static boolean isXXLarge = false;

    public static void init(Context context) {

        if (null == context || (screenWidth > 0 && screenHeight > 0)) {
            return;
        }
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);

        screenWidth = metric.widthPixels; // 屏幕宽度（像素）
        screenHeight = metric.heightPixels; // 屏幕高度（像素）

        density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        if (densityDpi <= 240) {
            largeScreen = false;
        } else {
            largeScreen = true;
        }

        if (screenWidth > 700 && screenHeight > 900) {
            isXLarge = true;
        }

        if (screenWidth > 1000) {
            isXXLarge = true;
        }

    }

    public static int getActionBarHeight(Context context) {
        if (actionBarHeight == 0 && null != context) {
            TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{
                    android.R.attr.actionBarSize
            });
            actionBarHeight = (int) actionbarSizeTypedArray.getDimension(0, 0);
            actionbarSizeTypedArray.recycle();
        }
        return actionBarHeight;
    }


    public static int getScreenWidth(Context context) {
        init(context);
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        init(context);
        return screenHeight;
    }

    public static float getDensityDpi(Context context) {
        init(context);
        return densityDpi;
    }

    public static int getStatusHeight(Context context) {
//		init(context);
        if (statusHeight == 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
                Rect frame = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

                statusHeight = frame.top;
            }
        }
        return statusHeight;
    }

    public static boolean isHighDensity(Context context) {
        init(context);
        return density > 2.0;
    }

    public static boolean isMiddleDensity(Context context) {
        init(context);
        return density >= 2.0;
    }

    public static boolean isLargeScreen(Context context) {
        init(context);
        return largeScreen;
    }

    public static boolean isXLargeScreen(Context context) {
        init(context);
        return isXLarge;
    }

    public static boolean isXXLargeScreen(Context context) {
        init(context);
        return isXXLarge;
    }

    public static int getSdkVersion(Context context) {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 隐藏软键盘
    public static void hideInputManager(View view, Context ct) {
        try {
            InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("exception", "hideInputManager Catch error,skip it!", e);
        }
    }

    public static boolean isOpen(Context ct) {
        try {
            InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.isActive();
        } catch (Exception e) {
            Log.e("exception", "hideInputManager Catch error,skip it!", e);
        }
        return false;
    }

    public static void openInputManager(Context ct) {
        try {
            InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("exception", "hideInputManager Catch error,skip it!", e);
        }
    }
    /** 显示软键盘 */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /** 多少时间后显示软键盘 */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view==null)return;
        // 显示输入法
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                showInputMethod(view);
            }
        }, delayMillis);
    }

    public static boolean isFullScreen(Context context, ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return false;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            //  Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int listViewHeight = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        int screenHeight = SysUtil.getScreenHeight(context);
        return screenHeight <= listViewHeight;
//        listviewHeight = params.height;
    }
}
