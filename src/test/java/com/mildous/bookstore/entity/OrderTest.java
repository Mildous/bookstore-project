package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.ProductCategory;
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
        product.setRegDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());
        return product;
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
            op.setCount(10);
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
            op.setCount(10);
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
