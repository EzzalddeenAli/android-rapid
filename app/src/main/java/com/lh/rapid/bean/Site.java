package com.lh.rapid.bean;

/**
 * Created by ceshi on 2018/1/6.
 */

public class Site {
    private String site;
    private String name;
    private String phone;
    private int image;
    private String change;
    private int line;

    public Site(String site, String name, String phone, int image, String change, int line) {
        this.site = site;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.change = change;
        this.line = line;
    }

    public Site() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
