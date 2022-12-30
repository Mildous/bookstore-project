package com.mildous.bookstore.entity;

import com.mildous.bookstore.dto.MemberDto;
import com.mildous.bookstore.repository.CartRepository;
import com.mildous.bookstore.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member joinNewMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("test@test.com");
        memberDto.setName("테스트");
        memberDto.setPhone("01012341234");
        memberDto.setPostcode("04524");
        memberDto.setAddress("서울특별시 중구 세종대로 110");
        memberDto.setDetailAddr("서울특별시청");
        memberDto.setExtraAddr(" (태평로 1가)");
        memberDto.setPassword("1234");
        return Member.joinNewMember(memberDto, passwordEncoder);
    }

    @Test
    @DisplayName("cart entity 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = joinNewMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId());
    }
}
