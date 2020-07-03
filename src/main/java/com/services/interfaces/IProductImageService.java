package com.services.interfaces;

import com.interfaces.entities.ProductImage;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IProductImageService {
    Page<ProductImage> getAllProductImages(String orderBy, String direction, int page, int pageSize);
    Optional<ProductImage>  getProductImage(String id);
    String addProductImage(ProductImage product) throws Exception;
    boolean updateProductImage(String id,ProductImage product);
    boolean deleteProductImage(String id);
    boolean existsByproductImageId(String id);
    boolean existsByFileName(String fileName);
    Optional<ProductImage> getByFileName(String fileName);
}
