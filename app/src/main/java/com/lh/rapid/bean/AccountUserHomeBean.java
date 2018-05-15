package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/15.
 */

public class AccountUserHomeBean {

    private double userAmount;
    private double userCoin;
    private String avatarUrl;
    private String phone;
    private String nickName;
    private String cardId;

    public double getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(double userAmount) {
        this.userAmount = userAmount;
    }

    public double getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(double userCoin) {
        this.userCoin = userCoin;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
