package com.mildous.bookstore.service;

import com.mildous.bookstore.entity.ProductImg;
import com.mildous.bookstore.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImgService {

    @Value("${productImgLocation}")
    private String productImgLocation;

    private final ProductImgRepository productImgRepository;

    private final FileService fileService;

    public void saveProductImg(ProductImg productImg, MultipartFile productImgFile) throws Exception {
        String oriImgName = productImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // file upload
        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());
            imgUrl = "/images/product/" + imgName;
        }

        // save product image's info
        productImg.updateProductImg(oriImgName, imgName, imgUrl);
        productImgRepository.save(productImg);
    }

    public void updateProductImg(Long imgId, MultipartFile productImgFile) throws Exception {
        if(!productImgFile.isEmpty()) {
            ProductImg savedImg = productImgRepository.findById(imgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 삭제
            if(!StringUtils.isEmpty(savedImg.getImgName())) {
                fileService.deleteFile(productImgLocation + "/" + savedImg.getImgName());
            }

            String oriImgName = productImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());
            String imgUrl = "/images/product/" + imgName;
            savedImg.updateProductImg(oriImgName, imgName, imgUrl);
        }
    }
}
