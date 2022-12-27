package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.Role;
import com.mildous.bookstore.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private String postcode;
    private String address;
    private String detailAddr;
    private String extraAddr;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member joinNewMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setPostcode(memberDto.getPostcode());
        member.setAddress(memberDto.getAddress());
        member.setDetailAddr(memberDto.getDetailAddr());
        member.setExtraAddr(memberDto.getExtraAddr());
        String password = passwordEncoder.encode(memberDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }

}
