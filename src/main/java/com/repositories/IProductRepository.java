package com.repositories;

import com.interfaces.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IProductRepository extends JpaRepository<Product, String> {
    //@Query("SELECT p From Product p where p.title = ?1")
    //public Product findByTitle(String title);
    //@Query("select p from Product p inner join p. ar where ar.idArea = :idArea")
    //@Query("select u.userName from User u inner join u.area ar where ar.idArea = :idArea")
    //
    //@Query("SELECT p From Product")
    //public List<Product> findAll();
    public List<Product> findTop10ByOrderByTitleDesc();

    @Query("FROM Product p LEFT JOIN FETCH p.productImage WHERE p.productId = ?1 ")
    Optional<Product> findByIdJoinProductImage(String id);
    boolean existsByTitle(String title);
    boolean existsByProductId(String id);
    Optional<Product> findByTitle(String id);
}
