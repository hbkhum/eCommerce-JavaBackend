package com.services.interfaces;

import com.interfaces.entities.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface IProductService {
    Page<Product> getAllProducts(String orderBy, String direction, int page, int pageSize);
    Optional<Product> getProduct(String id);
    String addProduct(Product entity) throws Exception;
    boolean updateProduct(String id,Product entity);
    boolean deleteProduct(String id);

    boolean existsByTitle(String title);
    boolean existsByProductId(String id);
    Optional<Product> getByTitle(String title);
}


