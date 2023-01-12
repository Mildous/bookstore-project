package com.mildous.bookstore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_product_code")
    private Long orderProductCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_code")
    private Order order;

    private int orderPrice;

    private int count;

    public static OrderProduct newOrderProduct(Product product, int count) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setCount(count);
        orderProduct.setOrderPrice(product.getProductPrice());

        product.removeStock(count);
        return orderProduct;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    public void cancel() {
        this.getProduct().addStock(count);
    }

}
