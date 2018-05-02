package com.lh.rapid.bean;

/**
 * Created by lh on 2018/4/25.
 */

public class LoginEntity {

    private long validTime;
    private String token;

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
