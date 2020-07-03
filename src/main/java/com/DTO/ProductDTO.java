package com.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductDTO {
    private String productId;
    @NotBlank
    private String title;
    @NotNull
    private float price;
    private String description;
   // @JsonBackReference(value="productImage")
    private List<ProductImageDTO> productImage;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductImageDTO> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<ProductImageDTO> productImage) {
        this.productImage = productImage;
    }
}
