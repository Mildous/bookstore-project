package com.mildous.bookstore.repository;

import com.mildous.bookstore.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    List<ProductImg> findByProductProductCodeOrderByImgIdAsc(Long productCode);

}
