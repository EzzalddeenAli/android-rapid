package com.lh.rapid.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.lh.rapid.R;
import com.lh.rapid.ui.widget.CenteredImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ceshi on 2016/10/21.
 */
public class Utils {

    public static SpannableString getSpannableString(String text, String foreColor, int start, float size) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(foreColor));
        spannableString.setSpan(colorSpan, start, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(size);
        spannableString.setSpan(sizeSpan, text.length() - 2, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getSpannableString(String price) {
        String text = "￥" + price;
        SpannableString spannableString = new SpannableString(text);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        spannableString.setSpan(sizeSpan, text.length() - 2, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getSpannableString(Context mContext, String name) {
        SpannableString spannableString = new SpannableString("  " + name);
//        Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_yufu);
//        drawable.setBounds(0, 0, SysUtil.dip2px(mContext, 36), SysUtil.dip2px(mContext, 18));
        CenteredImageSpan imageSpan = new CenteredImageSpan(mContext, R.mipmap.icon_yufu);
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static SpannableString getShareSpannableString(Context mContext, String des, int position) {
        SpannableString spannableString = new SpannableString(des);
        CenteredImageSpan imageSpan = new CenteredImageSpan(mContext, R.mipmap.icon_fenxiang_r);
        spannableString.setSpan(imageSpan, position, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

//    // 判断一个字符串是否都为数字
//    public boolean isDigit(String strNum) {
//        return strNum.matches("[0-9]{1,}");
//    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 截取非数字
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 判断一个字符串是否含有数字
    public static boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
}
