package com.lh.rapid.bean;

import java.io.Serializable;

/**
 * Created by yuyong on 2015/6/23.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -4827677246897657688L;

    /**
     * 名称
      */
    public String name;

    /**
     * 购物车ID
     */
    public Integer cartId;

    /**
     * 商品ID
     */
    public Integer vegId;


    /**
     * 购买数量
     */
    public Integer count;


    /**
     * 图片url
     */
    public String imageUrl;

    /**
     * 价格
     */
    public Float price;


    /**
     * 最小起订量
     */
    public Integer minOrderCnt;


    /**
     * 商品编号
     */
    public String number;

    /**
     * 促销价
     */
    public Float promotionPrice;

    /**
     * 规格
     */
    public String weight;

//    /**
//     * 是否上架
//     */
//    public Boolean isSale;
//
    /**
     * 库存数量
     */
    public Integer quantity;

    /**
     * 商品销售状态
     *
     * 1正常 2售罄 3下架
     */
    public int saleStatus;

    /**
     * 购物车商品限购数量
     */
    public int veg_sale_maxnum;

    /**
     * 销售商品 0普通商品 1预付商品
     */
    public int saleType;

    /**
     *  非服务器返回
     *  购物车界面是否被选择,默认被选中
     */
    public boolean isCheck = true;
    /**
     * 非服务器返回
     * 抢购商品ID
     */
    public Integer rushBuyActivityID;

    @Override
    public boolean equals(Object object) {
        if(object == null)
            return false;
        if(! (object instanceof Cart))
            return  false;
        else{
            Cart cart = (Cart) object;
            return (cart.vegId == this.vegId && cart.cartId == this.cartId);
        }

    }
}
