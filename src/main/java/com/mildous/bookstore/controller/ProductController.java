package com.mildous.bookstore.controller;

import com.mildous.bookstore.dto.ProductDto;
import com.mildous.bookstore.dto.ProductSearchDto;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/admin/product/new")
    public String regProduct(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "product/productForm";
    }

    @PostMapping(value = "/admin/product/new")
    public String regProduct(@Valid ProductDto productDto, BindingResult bindingResult,
                             Model model, @RequestParam("productImgFile")List<MultipartFile> productImgFileList) {

        if(bindingResult.hasErrors()) {
            return "product/productForm";
        }

        if(productImgFileList.get(0).isEmpty() && productDto.getProductCode() == null) {
            model.addAttribute("errorMessage", "대표이미지는 1개 이상 등록해야합니다.");
            return "product/productForm";
        }

        try {
            productService.regProduct(productDto, productImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "등록 중 오류가 발생하였습니다.");
            return "product/productForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/product/{productCode}")
    public String productDetail(@PathVariable("productCode") Long productCode, Model model) {

        try {
            ProductDto productDto = productService.getProductDetail(productCode);
            model.addAttribute("productDto", productDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "해당 상품이 존재하지 않습니다.");
            model.addAttribute("productDto", new ProductDto());
            return "product/productForm";
        }

        return "product/productForm";
    }

    @PostMapping(value = "/admin/product/{productCode}")
    public String productUpdate(@Valid ProductDto productDto, BindingResult bindingResult,
                                @RequestParam("productImgFile") List<MultipartFile> productImgFileList,
                                Model model) {

        if(bindingResult.hasErrors()) {
            return "product/productForm";
        }
        if(productImgFileList.get(0).isEmpty() && productDto.getProductCode() == null) {
            model.addAttribute("errorMessage", "대표이미지는 1개 이상 등록해야합니다.");
            return "product/productForm";
        }

        try {
            productService.updateProduct(productDto, productImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "수정 중 오류가 발생하였습니다.");
            return "product/productForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/products", "/admin/products/{page}"})
    public String productManage(ProductSearchDto productSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Product> products = productService.getProductAdminPage(productSearchDto, pageable);
        model.addAttribute("products", products);
        model.addAttribute("productSearchDto", productSearchDto);
        model.addAttribute("maxPage", 5);
        return "product/productManage";
    }

}
