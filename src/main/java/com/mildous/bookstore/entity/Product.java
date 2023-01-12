package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.ProductCategory;
import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.ProductDto;
import com.mildous.bookstore.exception.OutOfStockException;
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

    @Lob
    private String productSubName;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

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
        this.productSubName = productDto.getProductSubName();
        this.author = productDto.getAuthor();
        this.publisher = productDto.getPublisher();
        this.category = productDto.getCategory();
        this.productPrice = productDto.getProductPrice();
        this.productDetail = productDto.getProductDetail();
        this.productSellStatus = productDto.getProductSellStatus();
        this.stockAmount = productDto.getStockAmount();
    }

    public void addStock(int stockAmount) {
        this.stockAmount += stockAmount;
    }

    public void removeStock(int stockAmount) {
        int restStock = this.stockAmount - stockAmount;
        if(restStock < 0) {
            throw new OutOfStockException("재고가 부족합니다. (현재 재고 수량: " + this.stockAmount + ")");
        }
        this.stockAmount = restStock;
    }

}
