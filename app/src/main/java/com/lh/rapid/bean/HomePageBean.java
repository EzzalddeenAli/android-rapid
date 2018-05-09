package com.lh.rapid.bean;

import java.util.List;

/**
 * Created by lh on 2018/5/9.
 */

public class HomePageBean {

    private int circleId;
    private String circleName;
    private List<BnTopBean> bnTop;
    private List<CategoryListsBean> categoryLists;

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<BnTopBean> getBnTop() {
        return bnTop;
    }

    public void setBnTop(List<BnTopBean> bnTop) {
        this.bnTop = bnTop;
    }

    public List<CategoryListsBean> getCategoryLists() {
        return categoryLists;
    }

    public void setCategoryLists(List<CategoryListsBean> categoryLists) {
        this.categoryLists = categoryLists;
    }

    public static class BnTopBean {

        private String bnImgUrl;
        private String bnRelationUrl;
        private int sort;

        public String getBnImgUrl() {
            return bnImgUrl;
        }

        public void setBnImgUrl(String bnImgUrl) {
            this.bnImgUrl = bnImgUrl;
        }

        public String getBnRelationUrl() {
            return bnRelationUrl;
        }

        public void setBnRelationUrl(String bnRelationUrl) {
            this.bnRelationUrl = bnRelationUrl;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public static class CategoryListsBean {
        private String categoryName;
        private int categoryId;
        private List<GoodListBean> goodList;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public List<GoodListBean> getGoodList() {
            return goodList;
        }

        public void setGoodList(List<GoodListBean> goodList) {
            this.goodList = goodList;
        }

        public static class GoodListBean {

            private String goodsImg;
            private int goodsId;
            private int counts;
            private double goodsPrice;
            private String weight;
            private String goodsName;
            private String goodsDesc;

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
            }

            public double getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(double goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsDesc() {
                return goodsDesc;
            }

            public void setGoodsDesc(String goodsDesc) {
                this.goodsDesc = goodsDesc;
            }
        }
    }
}
