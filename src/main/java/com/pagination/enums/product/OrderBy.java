package com.pagination.enums.product;

public enum OrderBy {
    productId("productId"), title("title");
    private String OrderByCode;
    private OrderBy(String orderBy) {
        this.OrderByCode = orderBy;
    }
    public String getOrderByCode() {
        return this.OrderByCode;
    }
}
