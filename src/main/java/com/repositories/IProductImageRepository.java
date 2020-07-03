package com.repositories;


import com.interfaces.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IProductImageRepository extends JpaRepository<ProductImage, String> {

    boolean existsByproductImageId(String id);
    boolean existsByFileName(String fileName);
    Optional<ProductImage> findByFileName(String fileName);
}
