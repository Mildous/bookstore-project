package com.mildous.bookstore.repository;

import com.mildous.bookstore.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    CartProduct findByCartIdAndProductProductCode(Long cartId, Long productCode);
}
