package com.DTO;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class ProductImageDTO {
    private String productImageId;
    @NotBlank
    private String fileName;
    private MultipartFile fileImageAtt;
    private ProductDTO product;
    private byte[] fileImage;


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

    public MultipartFile getFileImageAtt() {
        return fileImageAtt;
    }

    public void setFileImageAtt(MultipartFile fileImageAtt) {
        this.fileImageAtt = fileImageAtt;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public byte[] getFileImage() {
        return fileImage;
    }

    public void setFileImage(byte[] fileImage) {
        this.fileImage = fileImage;
    }
}
