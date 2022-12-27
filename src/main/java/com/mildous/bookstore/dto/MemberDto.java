package com.mildous.bookstore.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class MemberDto {

    @NotBlank(message = "이름은 필수 입력사항입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력사항입니다.")
    @Length(min=8, max=20, message = "비밀번호는 8자 이상, 20자 이하로 입력해주세요.")
    private String password;

    // ^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$
    @NotEmpty(message = "전화번호는 필수 입력사항입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "전화번호는 10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;

    @NotEmpty(message = "우편번호 찾기를 클릭하여 우편번호를 입력하세요.")
    private String postcode;

    @NotEmpty
    private String address;

    @NotEmpty(message = "상세주소는 필수 입력사항입니다.")
    private String detailAddr;

    private String extraAddr;
}
