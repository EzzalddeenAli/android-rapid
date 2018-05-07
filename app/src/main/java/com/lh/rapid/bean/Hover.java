package com.lh.rapid.bean;

/**
 * Created by ceshi on 2018/1/8.
 */

public class Hover {

    public int product_id;
    public int category_id;
    private String name;
    private String discounts;
    private String price;
    private int num;
    public int is_package_status; // required
    public String icon; // required
    public String specification; // required
    //    public List<ItemBean> package_product_info; // required
    public int cart_num; // required

//    public Hover(int product_id, int category_id, String name, String discounts, String price, int num, int is_package_status, String icon, String specification, int cart_num) {
//        this.product_id = product_id;
//        this.category_id = category_id;
//        this.name = name;
//        this.discounts = discounts;
//        this.price = price;
//        this.num = num;
//        this.is_package_status = is_package_status;
//        this.icon = icon;
//        this.specification = specification;
//        this.cart_num = cart_num;
//    }
//
//    public Hover() {
//    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIs_package_status() {
        return is_package_status;
    }

    public void setIs_package_status(int is_package_status) {
        this.is_package_status = is_package_status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getCart_num() {
        return cart_num;
    }

    public void setCart_num(int cart_num) {
        this.cart_num = cart_num;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "product_id=" + product_id +
                ", category_id=" + category_id +
                ", name='" + name + '\'' +
                ", is_package_status=" + is_package_status +
                ", icon='" + icon + '\'' +
                ", weight='" + discounts + '\'' +
                ", price='" + price + '\'' +
                ", specification='" + specification + '\'' +
                ", num=" + num +
                ", cart_num=" + cart_num +
                '}';
    }

}
