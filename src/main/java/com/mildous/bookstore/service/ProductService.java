package com.mildous.bookstore.service;

import com.mildous.bookstore.dto.MainProductDto;
import com.mildous.bookstore.dto.ProductDto;
import com.mildous.bookstore.dto.ProductImgDto;
import com.mildous.bookstore.dto.ProductSearchDto;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.entity.ProductImg;
import com.mildous.bookstore.repository.ProductImgRepository;
import com.mildous.bookstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final ProductImgService productImgService;

    public Long createProduct(ProductDto productDto, List<MultipartFile> productFileList) throws Exception {

        // create Product
        Product product = productDto.product();
        productRepository.save(product);

        // create image-file
        for(int i=0; i<productFileList.size(); i++) {
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);
            if(i == 0) {
                productImg.setRepImgYn("Y");
            } else {
                productImg.setRepImgYn("N");
            }
            productImgService.saveProductImg(productImg, productFileList.get(i));
        }

        return product.getProductCode();
    }

    @Transactional(readOnly = true)
    public ProductDto getProductDetail(Long productCode) {

        List<ProductImg>  productImgList = productImgRepository.findByProductProductCodeOrderByImgIdAsc(productCode);
        List<ProductImgDto> productImgDtos = new ArrayList<>();
        for(ProductImg productImg : productImgList) {
            ProductImgDto productImgDto = ProductImgDto.of(productImg);
            productImgDtos.add(productImgDto);
        }

        Product product = productRepository.findById(productCode).orElseThrow(EntityNotFoundException::new);
        ProductDto productDto = ProductDto.of(product);
        productDto.setProductImgDtos(productImgDtos);
        return productDto;
    }

    public Long updateProduct(ProductDto productDto, List<MultipartFile> productImgFileList) throws Exception {

        Product product = productRepository.findById(productDto.getProductCode()).orElseThrow(EntityNotFoundException::new);
        product.updateProduct(productDto);

        List<Long> productImgIds = productDto.getProductImgIds();
        for(int i=0; i<productImgFileList.size(); i++) {
            productImgService.updateProductImg(productImgIds.get(i), productImgFileList.get(i));
        }

        return product.getProductCode();
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductAdminPage(ProductSearchDto productSearchDto, Pageable pageable) {
        return productRepository.getProductAdminPage(productSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        return productRepository.getMainProductPage(productSearchDto, pageable);
    }
}
