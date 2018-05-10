package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/10.
 */

public class AddressListBean {

    private String area;
    private String detailAddress;
    private String receiveGoodsName;
    private String phone;
    private int addressId;
    private int image;
    private int line;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getReceiveGoodsName() {
        return receiveGoodsName;
    }

    public void setReceiveGoodsName(String receiveGoodsName) {
        this.receiveGoodsName = receiveGoodsName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
