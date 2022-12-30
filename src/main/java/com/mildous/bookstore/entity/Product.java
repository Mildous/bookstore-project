package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.ProductDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="product")
@Getter @Setter @ToString
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_code")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productCode;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column(nullable = false)
    private int stockAmount;

    @Lob
    @Column(nullable = false)
    private String productDetail;

    @Enumerated(EnumType.STRING)
    private ProductSellStatus productSellStatus;

    public void updateProduct(ProductDto productDto) {
        this.productName = productDto.getProductName();
        this.productPrice = productDto.getProductPrice();
        this.productDetail = productDto.getProductDetail();
        this.productSellStatus = productDto.getProductSellStatus();
        this.stockAmount = productDto.getStockAmount();
    }

}
