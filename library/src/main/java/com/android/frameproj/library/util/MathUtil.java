package com.android.frameproj.library.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lh on 2018/2/28.
 */

public class MathUtil {

    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static String keepTwoNumbersAfterThePoint(String number) {
        try {
            if (!TextUtils.isEmpty(number)) {
                BigDecimal bigDecimal = new BigDecimal(number);
                return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            } else {
                return "0.00";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static String point2decimal(float decimal){
        DecimalFormat format=new DecimalFormat("0.00");
        return format.format(decimal);
    }

    public static String point2decimal(double decimal){
        DecimalFormat format=new DecimalFormat("0.00");
        return format.format(decimal);
    }

    public static int float2int(float value){
        return (int)value;
    }

}
