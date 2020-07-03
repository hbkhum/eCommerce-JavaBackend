package com.pagination.enums.productImage;

public enum OrderBy {
    productImageId("productImageId"), fileName("fileName");
    private String OrderByCode;
    private OrderBy(String orderBy) {
        this.OrderByCode = orderBy;
    }
    public String getOrderByCode() {
        return this.OrderByCode;
    }
}
