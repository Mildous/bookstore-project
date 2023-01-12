package com.mildous.bookstore.dto;

import com.mildous.bookstore.constant.ProductCategory;
import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ProductDto {

    private Long productCode;

    @NotBlank(message = "도서명을 입력해주세요.")
    private String productName;

    private String productSubName;

    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;

    @NotBlank(message = "저자를 입력해주세요.")
    private String author;

    private ProductCategory category;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer productPrice;

    @NotNull(message = "재고 수량을 입력해주세요.")
    private Integer stockAmount;

    @NotBlank(message = "상세설명을 입력해주세요.")
    private String productDetail;

    private ProductSellStatus productSellStatus;

    private List<ProductImgDto> productImgDtos = new ArrayList<>();

    private List<Long> productImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Product product() {
        return modelMapper.map(this, Product.class);
    }

    public static ProductDto of(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
