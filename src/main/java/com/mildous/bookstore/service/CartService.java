package com.mildous.bookstore.service;

import com.mildous.bookstore.dto.CartProductDto;
import com.mildous.bookstore.entity.Cart;
import com.mildous.bookstore.entity.CartProduct;
import com.mildous.bookstore.entity.Member;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.repository.CartProductRepository;
import com.mildous.bookstore.repository.CartRepository;
import com.mildous.bookstore.repository.MemberRepository;
import com.mildous.bookstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    public Long addCart(CartProductDto cartProductDto, String email) {
        // 카트에 담을 상품 엔티티 조회
        Product product = productRepository.findById(cartProductDto.getProductCode()).orElseThrow(EntityNotFoundException::new);
        // 현재 로그인한 회원 엔티티 조회
        Member member = memberRepository.findByEmail(email);
        // 현재 로그인한 회원의 장바구니 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        
        // 카트에 처음으로 상품을 담을 경우 카트 엔티티 생성
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 해당 상품이 카트에 이미 담겨있는지 조회
        CartProduct savedCartProduct = cartProductRepository.findByCartIdAndProductProductCode(cart.getId(), product.getProductCode());

        // 카트에 있던 상품일 경우
        if(savedCartProduct != null) {
            // 기존의 수량에 더하기
            savedCartProduct.addCount(cartProductDto.getCount());
            return savedCartProduct.getId();
        } else {
            // 카트 엔티티, 상품 엔티티, 담을 수량을 이용하여 CartProduct 엔티티 생성
            CartProduct cartProduct = CartProduct.createCartProduct(cart, product, cartProductDto.getCount());
            // 카트에 담길 상품을 저장
            cartProductRepository.save(cartProduct);
            return cartProduct.getId();
        }

    }
}
