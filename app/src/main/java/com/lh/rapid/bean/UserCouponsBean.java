package com.lh.rapid.bean;

import java.util.List;

/**
 * Created by lh on 2018/5/17.
 */

public class UserCouponsBean {

    private int counts;
    private int status;
    private List<CouponsListBean> couponsList;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CouponsListBean> getCouponsList() {
        return couponsList;
    }

    public void setCouponsList(List<CouponsListBean> couponsList) {
        this.couponsList = couponsList;
    }

    public static class CouponsListBean {

        private String expressTime; // 2018-05-24 16:16:03
        private String couponName; // 满20减3
        private String couponsMemo; // 注册用户送优惠卷
        private String createTime; // 2018-05-17 16:16:03
        private double fullMoney; // 20
        private double saveMoney; // 3
        private int couponId; // 1

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }
    }
}
