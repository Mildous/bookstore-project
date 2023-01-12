package com.mildous.bookstore.service;

import com.mildous.bookstore.constant.OrderStatus;
import com.mildous.bookstore.constant.ProductCategory;
import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.OrderDto;
import com.mildous.bookstore.entity.Member;
import com.mildous.bookstore.entity.Order;
import com.mildous.bookstore.entity.OrderProduct;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.repository.MemberRepository;
import com.mildous.bookstore.repository.OrderRepository;
import com.mildous.bookstore.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    public Product saveProduct() {
        Product product = new Product();
        product.setProductName("상품 테스트");
        product.setProductSubName("부제목");
        product.setAuthor("저자");
        product.setPublisher("출판사");
        product.setCategory(ProductCategory.ART);
        product.setProductPrice(10000);
        product.setProductDetail("상품설명");
        product.setProductSellStatus(ProductSellStatus.SELL);
        product.setStockAmount(100);
        return productRepository.save(product);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Product product = saveProduct();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setProductCode(product.getProductCode());

        Long orderCode = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderCode).orElseThrow(EntityNotFoundException::new);

        List<OrderProduct> orderProducts = order.getOrderProducts();

        int totalPrice = orderDto.getCount()*product.getProductPrice();

        assertEquals(totalPrice, order.getTotalPrice());

    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Product product = saveProduct();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setProductCode(product.getProductCode());
        Long orderCode = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderCode).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderCode);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(100, product.getStockAmount());
    }
}
