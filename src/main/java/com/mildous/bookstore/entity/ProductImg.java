package com.mildous.bookstore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_img")
@Getter @Setter
public class ProductImg extends BaseEntity {

    @Id
    @Column(name = "product_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imgId;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code")
    private Product product;

    public void updateProductImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
