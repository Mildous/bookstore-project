package com.mildous.bookstore.dto;

import com.mildous.bookstore.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductSearchDto {

    private String dType;   // searchDateType

    private ProductSellStatus status; // searchSellStatus

    private String by;    // searchBy

    private String query = "";    // searchQuery

}
