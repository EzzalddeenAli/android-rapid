package com.lh.rapid.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by lh on 2018/5/17.
 */

public class SexBean implements IPickerViewData {

    private int sex;
    private String desc;

    public SexBean(int sex, String desc) {
        this.sex = sex;
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getPickerViewText() {
        return desc;
    }
}
