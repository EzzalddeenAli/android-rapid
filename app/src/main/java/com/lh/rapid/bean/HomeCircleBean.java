package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/10.
 */

public class HomeCircleBean {

    private String bnImgUrl;
    private int circleId;
    private String address;
    private double distance;
    private String circleName;

    public String getBnImgUrl() {
        return bnImgUrl;
    }

    public void setBnImgUrl(String bnImgUrl) {
        this.bnImgUrl = bnImgUrl;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
}
