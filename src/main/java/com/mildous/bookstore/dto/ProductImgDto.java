package com.mildous.bookstore.dto;

import com.mildous.bookstore.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ProductImgDto {

    private Long imgId;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDto of(ProductImg productImg) {
        return modelMapper.map(productImg, ProductImgDto.class);
    }
}
