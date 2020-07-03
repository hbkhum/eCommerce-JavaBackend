package com.interfaces.entities;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;

    @NotBlank
    @Column(unique = true)
    private String title;

    @NotNull
    private float price;

    private String description;

    @OneToMany
    @JoinColumn(name="productId")
    private List<ProductImage> productImage=new ArrayList<>();;


    public Product(@NotBlank String title, @NotNull float price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public List<ProductImage> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<ProductImage> productImage) {
        this.productImage = productImage;
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



/*    public Set<ProductImage> getProductImage() { return productImage; }
    public void setProductImage(Set<ProductImage> productImage) { this.productImage = productImage; }*/

    /*public Product() {

    }*/



    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                '}';
    }
}