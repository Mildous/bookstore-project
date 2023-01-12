package com.mildous.bookstore.service;

import com.mildous.bookstore.dto.OrderDto;
import com.mildous.bookstore.dto.OrderHistoryDto;
import com.mildous.bookstore.dto.OrderProductDto;
import com.mildous.bookstore.entity.*;
import com.mildous.bookstore.repository.MemberRepository;
import com.mildous.bookstore.repository.OrderRepository;
import com.mildous.bookstore.repository.ProductImgRepository;
import com.mildous.bookstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductImgRepository productImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Product product = productRepository.findById(orderDto.getProductCode()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.newOrderProduct(product, orderDto.getCount());
        orderProductList.add(orderProduct);

        Order order = Order.newOrder(member, orderProductList);
        orderRepository.save(order);

        return order.getOrderCode();
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistoryDto> orderHistoryDtos = new ArrayList<>();

        for(Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
            List<OrderProduct> orderProducts = order.getOrderProducts();
            for(OrderProduct orderProduct : orderProducts) {
                ProductImg productImg = productImgRepository.findByProductProductCodeAndRepImgYn(orderProduct.getProduct().getProductCode(), "Y");
                OrderProductDto orderProductDto = new OrderProductDto(orderProduct, productImg.getImgUrl());
                orderHistoryDto.addOrderProduct(orderProductDto);
            }

            orderHistoryDtos.add(orderHistoryDto);
        }

        return new PageImpl<OrderHistoryDto>(orderHistoryDtos, pageable, totalCount);
    }

    // 주문 취소
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderCode, String email) {
        // 로그인한 사용자와 같은지 검증
        Member member = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderCode).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderCode) {
        Order order = orderRepository.findById(orderCode).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

}
