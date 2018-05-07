package com.lh.rapid.bean;

import java.util.List;

/**
 * Created by ceshi on 2017/12/19.
 */

public class CategoryName {

    private String kind;
    private List<GoodBean> list;
    private int line;
    private int color;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<GoodBean> getList() {
        return list;
    }

    public void setList(List<GoodBean> list) {
        this.list = list;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CatograyBean{" +
                "kind='" + kind + '\'' +
                ", list=" + list +
                ",line=" + line +
                ",color=" + color +
                '}';
    }

}
