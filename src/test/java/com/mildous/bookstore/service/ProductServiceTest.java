package com.mildous.bookstore.service;

import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.ProductDto;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.entity.ProductImg;
import com.mildous.bookstore.repository.ProductImgRepository;
import com.mildous.bookstore.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImgRepository productImgRepository;

    List<MultipartFile> newMultipartFiles() throws Exception {

        List<MultipartFile> multipartFiles = new ArrayList<>();

        for(int i=0; i<5; i++) {
            String path = "C:/bookstore/product/";
            String imgName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imgName, "image/jpg", new byte[]{1,2,3,4});
            multipartFiles.add(multipartFile);
        }

        return multipartFiles;
    }

    @Test
    @DisplayName("등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void regProduct() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setProductName("테스트");
        dto.setProductSellStatus(ProductSellStatus.SELL);
        dto.setProductPrice(10000);
        dto.setStockAmount(100);
        dto.setProductDetail("설명");

        List<MultipartFile> multipartFileList = newMultipartFiles();
        Long productCode = productService.regProduct(dto, multipartFileList);

        List<ProductImg> productImgList = productImgRepository.findByProductProductCodeOrderByImgIdAsc(productCode);
        Product product = productRepository.findById(productCode).orElseThrow(EntityNotFoundException::new);

        assertEquals(dto.getProductName(), product.getProductName());
        assertEquals(dto.getProductSellStatus(), product.getProductSellStatus());
        assertEquals(dto.getProductPrice(), product.getProductPrice());
        assertEquals(dto.getStockAmount(), product.getStockAmount());
        assertEquals(dto.getProductDetail(), product.getProductDetail());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), productImgList.get(0).getOriImgName());
    }
}
