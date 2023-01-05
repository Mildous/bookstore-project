package com.mildous.bookstore.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainProductDto {
    private Long productCode;
    private String productName;
    private String productDetail;
    private String imgUrl;
    private Integer productPrice;

    @QueryProjection
    public MainProductDto(Long productCode, String productName, String productDetail, String imgUrl, Integer productPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.productDetail = productDetail;
        this.imgUrl = imgUrl;
        this.productPrice = productPrice;
    }
}
