package com.mildous.bookstore.dto;

import com.mildous.bookstore.constant.OrderStatus;
import com.mildous.bookstore.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistoryDto {

    private Long orderCode;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderProductDto> orderProductDtoList = new ArrayList<>();

    public OrderHistoryDto(Order order) {
        this.orderCode = order.getOrderCode();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderProduct(OrderProductDto orderProductDto) {
        orderProductDtoList.add(orderProductDto);
    }
}
