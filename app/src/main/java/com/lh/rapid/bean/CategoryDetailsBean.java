package com.lh.rapid.bean;

import java.util.List;

/**
 * Created by lh on 2017/10/20.
 */

public class CategoryDetailsBean {

    private String categoryName;
    private int categoryId;

    private List<CategoryBean> category;

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

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class CategoryBean {
        private String photoPath;
        private int categoryId;
        private String categoryName;

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public int getId() {
            return categoryId;
        }

        public void setId(int id) {
            this.categoryId = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
