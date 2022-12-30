package com.mildous.bookstore.repository;

import com.mildous.bookstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom {

    List<Product> findByProductName(String productName);

    List<Product> findByProductNameOrProductDetail(String productName, String productDetail);

    List<Product> findByProductPriceLessThan(Integer productPrice);

    List<Product> findByProductPriceLessThanOrderByProductPriceDesc(Integer productPrice);

    @Query("select p from Product p where p.productDetail like %:productDetail% order by p.productPrice desc")
    List<Product> findByProductDetail(@Param("productDetail") String productDetail);

}
