package com.mildous.bookstore.entity;

import com.mildous.bookstore.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="product")
@Getter @Setter @ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productCode;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column(nullable = false)
    private int stockAmount;

    @Lob
    @Column(nullable = false)
    private String productDetail;

    @Enumerated(EnumType.STRING)
    private ProductSellStatus productSellStatus;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;

}
