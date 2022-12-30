package com.mildous.bookstore.repository;

import com.mildous.bookstore.dto.ProductSearchDto;
import com.mildous.bookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> getProductAdminPage(ProductSearchDto productSearchDto, Pageable pageable);

}
