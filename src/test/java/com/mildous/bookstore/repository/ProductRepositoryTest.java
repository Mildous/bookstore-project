package com.mildous.bookstore.repository;

import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품 등록 테스트")
    public void createProductTest() {
        Product product = new Product();
        product.setProductName("test product");
        product.setProductPrice(10000);
        product.setProductDetail("test product description");
        product.setProductSellStatus(ProductSellStatus.SELL);
        product.setStockAmount(100);
        product.setRegDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        System.out.println(savedProduct.toString());
    }

    public void createProductList() {
        for(int i=1; i<=10; i++) {
            Product product = new Product();
            product.setProductName("test product" + i);
            product.setProductPrice(10000 + i);
            product.setProductDetail("test product description" + i);
            product.setProductSellStatus(ProductSellStatus.SELL);
            product.setStockAmount(100);
            product.setRegDate(LocalDateTime.now());
            product.setUpdateDate(LocalDateTime.now());
            Product savedProduct = productRepository.save(product);
        }
    }
    
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByProductNameTest() {
        this.createProductList();
        List<Product> productList = productRepository.findByProductName("test product1");
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("상품명과 상품설명 OR 테스트")
    public void findByProductNameOrProductDetail() {
        this.createProductList();
        List<Product> productList = productRepository.findByProductNameOrProductDetail("test product1", "test product description7");
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("상품가격 LessThan 테스트")
    public void findByProductPriceLessThanTest() {
        this.createProductList();
        List<Product> productList = productRepository.findByProductPriceLessThan(10003);
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("상품가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createProductList();
        List<Product> productList = productRepository.findByProductPriceLessThanOrderByProductPriceDesc(10003);
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("@Query 상품 조회 테스트")
    public void findByProductDetailTest() {
        this.createProductList();
        List<Product> productList = productRepository.findByProductDetail("test product description");
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {
        this.createProductList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct qProduct = QProduct.product;
        JPAQuery<Product> query = queryFactory.selectFrom(qProduct)
                .where(qProduct.productSellStatus.eq(ProductSellStatus.SELL))
                .where(qProduct.productDetail.like("%" + "test product description" + "%"))
                .orderBy(qProduct.productPrice.desc());

        List<Product> productList = query.fetch();
        for(Product product : productList) {
            System.out.println(product.toString());
        }
    }

    public void createProductList2() {
        for(int i=1; i<=5; i++) {
            Product product = new Product();
            product.setProductName("test product" + i);
            product.setProductPrice(10000 + i);
            product.setProductDetail("description" + i);
            product.setProductSellStatus(ProductSellStatus.SELL);
            product.setStockAmount(100);
            product.setRegDate(LocalDateTime.now());
            product.setUpdateDate(LocalDateTime.now());
            productRepository.save(product);
        }

        for(int i=6; i<=10; i++) {
            Product product = new Product();
            product.setProductName("test product" + i);
            product.setProductPrice(10000 + i);
            product.setProductDetail("description" + i);
            product.setProductSellStatus(ProductSellStatus.SOLD_OUT);
            product.setStockAmount(0);
            product.setRegDate(LocalDateTime.now());
            product.setUpdateDate(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        this.createProductList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QProduct qProduct = QProduct.product;
        String detail = "description";
        int price = 10004;
        String sellStat = "SELL";

        booleanBuilder.and(qProduct.productDetail.like("%" + detail + "%"));
        booleanBuilder.and(qProduct.productPrice.gt(price));

        if(StringUtils.equals(sellStat, ProductSellStatus.SELL)) {
            booleanBuilder.and(qProduct.productSellStatus.eq(ProductSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPageResult = productRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements: " + productPageResult.getTotalElements());

        List<Product> resultProductList = productPageResult.getContent();
        for(Product resultProduct : resultProductList) {
            System.out.println(resultProduct.toString());
        }
    }

}