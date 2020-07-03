package com.interfaces.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productImageId;
    @NotBlank
    private String fileName;
    @Lob
    private byte[] fileImage;
    // @Transient
    // private MultipartFile fileImageAtt;
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private Product product;

    public ProductImage() {
    }

    public ProductImage(@NotBlank String fileName, byte[] fileImage ) {
        this.fileName = fileName;
        this.fileImage = fileImage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileImage() {
        return fileImage;
    }

    public void setFileImage(byte[] fileImage) {
        this.fileImage = fileImage;
    }



    /*public MultipartFile getFileImageAtt() {
        return fileImageAtt;
    }

    public void setFileImageAtt(MultipartFile fileImageAtt) {
        this.fileImageAtt = fileImageAtt;
    }*/

}
