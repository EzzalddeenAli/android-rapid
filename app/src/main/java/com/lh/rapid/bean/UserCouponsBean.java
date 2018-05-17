package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/17.
 */

public class UserCouponsBean {

    private String expressTime; // 2018-05-24 16:16:03
    private String couponName; // 满20减3
    private String couponsMemo; // 注册用户送优惠卷
    private double fullMoney; // 20
    private double saveMoney; // 3
    private String createTime; // 2018-05-17 16:16:03

    public String getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(String expressTime) {
        this.expressTime = expressTime;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponsMemo() {
        return couponsMemo;
    }

    public void setCouponsMemo(String couponsMemo) {
        this.couponsMemo = couponsMemo;
    }

    public double getFullMoney() {
        return fullMoney;
    }

    public void setFullMoney(double fullMoney) {
        this.fullMoney = fullMoney;
    }

    public double getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(double saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
