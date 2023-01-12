package com.mildous.bookstore.dto;

import com.mildous.bookstore.constant.ProductCategory;
import com.mildous.bookstore.constant.ProductSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainProductDto {
    private Long productCode;
    private String productName;
    private String productSubName;

    private ProductCategory category;

    private String author;

    private String publisher;
    private String imgUrl;
    private Integer productPrice;

    @QueryProjection
    public MainProductDto(Long productCode, String productName, String productSubName, ProductCategory category, String author, String publisher, String imgUrl, Integer productPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.productSubName = productSubName;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.imgUrl = imgUrl;
        this.productPrice = productPrice;
    }
}
