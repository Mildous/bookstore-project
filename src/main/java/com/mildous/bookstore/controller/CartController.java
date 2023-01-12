package com.mildous.bookstore.controller;

import com.mildous.bookstore.dto.CartProductDto;
import com.mildous.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity order(@Valid CartProductDto cartProductDto, BindingResult bindingResult, Principal principal) {
        // 카트에 담을 상품 정보를 받는 cartProductDto 객체에 데이터 바인딩 시 에러가 있는지를 검사
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // 현재 로그인한 회원의 이메일 정보를 email 변수에 저장
        String email = principal.getName();
        Long cartProductCode;

        try {
            // 화면으로부터 넘어온 카트에 담을 상품의 정보(cartProductDto)와 현재 로그인한 회원의 이메일 정보를 이용하여 카트에 상품을 담는 로직을 호출
            cartProductCode = cartService.addCart(cartProductDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 결과값으로 생성된 카트의 상품 코드와 요청이 성공하였다는 HTTP 응답 상태 코드를 반환
        return new ResponseEntity<Long>(cartProductCode, HttpStatus.OK);
    }
}
