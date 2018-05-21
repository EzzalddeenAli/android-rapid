package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/14.
 */

public class OrderSubmitConfirmBean {

    private double price;
    private int flgRangeIn;
    private double expressAmount;

    public double getExpressAmount() {
        return expressAmount;
    }

    public void setExpressAmount(double expressAmount) {
        this.expressAmount = expressAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getFlgRangeIn() {
        return flgRangeIn;
    }

    public void setFlgRangeIn(int flgRangeIn) {
        this.flgRangeIn = flgRangeIn;
    }
}
