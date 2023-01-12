package com.mildous.bookstore.dto;

import com.mildous.bookstore.entity.OrderProduct;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderProductDto {

    private String productName;
    private int count;
    private int orderPrice;
    private String imgUrl;

    public OrderProductDto(OrderProduct orderProduct, String imgUrl) {
        this.productName = orderProduct.getProduct().getProductName();
        this.count = orderProduct.getCount();
        this.orderPrice = orderProduct.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
