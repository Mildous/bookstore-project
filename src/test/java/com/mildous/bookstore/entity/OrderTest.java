package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.repository.MemberRepository;
import com.mildous.bookstore.repository.OrderProductRepository;
import com.mildous.bookstore.repository.OrderRepository;
import com.mildous.bookstore.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;

    public Product newProduct() {
        Product p = new Product();
        p.setProductName("상품 테스트");
        p.setProductPrice(10000);
        p.setProductDetail("상품설명");
        p.setProductSellStatus(ProductSellStatus.SELL);
        p.setStockAmount(100);
        p.setRegDate(LocalDateTime.now());
        p.setUpdateDate(LocalDateTime.now());
        return p;
    }

    @Test
    @DisplayName("cascade 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0; i<3; i++) {
            Product p = this.newProduct();
            productRepository.save(p);
            OrderProduct op = new OrderProduct();
            op.setProduct(p);
            op.setOrderAmount(10);
            op.setOrderPrice(1000);
            op.setOrder(order);
            order.getOrderProducts().add(op);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getOrderCode())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderProducts().size());
    }

    @Autowired
    MemberRepository memberRepository;
    public Order newOrder() {
        Order order = new Order();

        for(int i=0; i<3; i++) {
            Product p = newProduct();
            productRepository.save(p);
            OrderProduct op = new OrderProduct();
            op.setProduct(p);
            op.setOrderAmount(10);
            op.setOrderPrice(10000);
            op.setOrder(order);
            order.getOrderProducts().add(op);
        }

        Member m = new Member();
        memberRepository.save(m);

        order.setMember(m);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("orphanRemoval 테스트")
    public void orphanRemovalTest() {
        Order order = this.newOrder();
        order.getOrderProducts().remove(0);
        em.flush();
    }

    @Autowired
    OrderProductRepository orderProductRepo;

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.newOrder();
        Long orderProductCode = order.getOrderProducts().get(0).getOrderProductCode();
        em.flush();
        em.clear();

        OrderProduct op = orderProductRepo.findById(orderProductCode)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class: " + op.getOrder().getClass());
        System.out.println("==========================================");
        op.getOrder().getOrderDate();
        System.out.println("==========================================");
    }

}
