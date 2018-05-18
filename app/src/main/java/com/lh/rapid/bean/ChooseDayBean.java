package com.lh.rapid.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by lh on 2018/5/18.
 */

public class ChooseDayBean implements IPickerViewData {

    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String getPickerViewText() {
        return day;
    }
}
