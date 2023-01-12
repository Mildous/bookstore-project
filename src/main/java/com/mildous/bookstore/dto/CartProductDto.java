package com.mildous.bookstore.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartProductDto {

    @NotNull(message = "상품 코드는 필수 입니다.")
    private Long productCode;

    @Min(value = 1, message = "최소 1개 이상 담아야 합니다.")
    private int count;

}
