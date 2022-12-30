package com.mildous.bookstore.controller;

import com.mildous.bookstore.dto.MemberDto;
import com.mildous.bookstore.entity.Member;
import com.mildous.bookstore.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member joinNewMember(String email, String password) {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(email);
        memberDto.setPassword(password);
        memberDto.setName("테스트");
        memberDto.setPhone("01012341234");
        memberDto.setPostcode("04524");
        memberDto.setAddress("서울특별시 중구 세종대로 110");
        memberDto.setDetailAddr("서울특별시청");
        memberDto.setExtraAddr(" (태평로 1가)");
        Member member = Member.joinNewMember(memberDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginTest() throws Exception {
        String email = "test@test.com";
        String password = "1234";
        this.joinNewMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void failLoginTest() throws Exception {
        String email = "test@test.com";
        String password = "1234";
        this.joinNewMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password("123123"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}
