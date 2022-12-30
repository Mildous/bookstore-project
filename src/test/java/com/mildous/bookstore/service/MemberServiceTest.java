package com.mildous.bookstore.service;

import com.mildous.bookstore.dto.MemberDto;
import com.mildous.bookstore.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    @DisplayName("회원가입 테스트")
    public void joinTest() {
        Member member = joinNewMember();
        Member joinedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), joinedMember.getEmail());
        assertEquals(member.getName(), joinedMember.getName());
        assertEquals(member.getPhone(), joinedMember.getPhone());
        assertEquals(member.getPostcode(), joinedMember.getPostcode());
        assertEquals(member.getAddress(), joinedMember.getAddress());
        assertEquals(member.getDetailAddr(), joinedMember.getDetailAddr());
        assertEquals(member.getExtraAddr(), joinedMember.getExtraAddr());
        assertEquals(member.getPassword(), joinedMember.getPassword());
        assertEquals(member.getRole(), joinedMember.getRole());
    }

    @Test
    @DisplayName("중복 가입 테스트")
    public void DuplicateJoinTest() {
        Member member1 = joinNewMember();
        Member member2 = joinNewMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 이메일입니다.", e.getMessage());
    }
}
