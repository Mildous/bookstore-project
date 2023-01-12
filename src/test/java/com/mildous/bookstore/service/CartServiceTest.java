package com.mildous.bookstore.service;

import com.mildous.bookstore.constant.ProductCategory;
import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.CartProductDto;
import com.mildous.bookstore.entity.CartProduct;
import com.mildous.bookstore.entity.Member;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.repository.CartProductRepository;
import com.mildous.bookstore.repository.MemberRepository;
import com.mildous.bookstore.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartProductRepository cartProductRepository;
    
    public Product savedProduct() {
        Product product = new Product();
        product.setProductName("테스트");
        product.setProductSubName("부제목");
        product.setAuthor("저자");
        product.setPublisher("출판사");
        product.setCategory(ProductCategory.ART);
        product.setProductPrice(10000);
        product.setProductDetail("테스트 테스트");
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
    @DisplayName("카트에 상품 담기 테스트")
    public void addCart() {
        Product product = savedProduct();
        Member member = saveMember();

        // 카트에 담을 상품과 수량을 cartProductDto 객체에 세팅
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setCount(5);
        cartProductDto.setProductCode(product.getProductCode());

        // 상품을 카트에 담는 로직 호출 결과, 생성된 카트의 상품 코드를 cartProductCode 변수에 저장
        Long cartProductCode = cartService.addCart(cartProductDto, member.getEmail());

        // 카트의 상품 코드를 이용하여 생성된 카트의 상품 정보를 조회
        CartProduct cartProduct = cartProductRepository.findById(cartProductCode).orElseThrow(EntityNotFoundException::new);

        // 상품 코드와 카트에 담긴 상품 코드가 같다면 테스트 통과
        assertEquals(product.getProductCode(), cartProduct.getProduct().getProductCode());
        // 카트에 담은 수량과 실제 카트에 저장된 수량이 같으면 테스트 통과
        assertEquals(cartProductDto.getCount(), cartProduct.getCount());
    }
}
