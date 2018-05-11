package com.lh.rapid.bean;

import java.util.List;

/**
 * Created by lh on 2018/5/11.
 */

public class CartGoodsBean {

    private String circleName;
    private int circleId;
    private List<GoodsListsBean> goodsLists;

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public List<GoodsListsBean> getGoodsLists() {
        return goodsLists;
    }

    public void setGoodsLists(List<GoodsListsBean> goodsLists) {
        this.goodsLists = goodsLists;
    }

    public static class GoodsListsBean {

        private int quantity;
        private String goodsName;
        private int goodsId;
        private double price;
        private String goodsImgUrl;
        private int cartId;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getGoodsImgUrl() {
            return goodsImgUrl;
        }

        public void setGoodsImgUrl(String goodsImgUrl) {
            this.goodsImgUrl = goodsImgUrl;
        }

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }
    }
}
