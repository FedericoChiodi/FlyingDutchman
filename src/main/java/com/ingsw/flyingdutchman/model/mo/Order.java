package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private Long orderID;

    @Column(name = "selling_price")
    private Float sellingPrice;

    @Column(name = "order_time")
    private Timestamp orderTime;

    @Column(name = "bought_from_threshold")
    private Boolean boughtFromThreshold;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    // Getters and Setters
    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Boolean isBoughtFromThreshold() {
        return boughtFromThreshold;
    }

    public void setBoughtFromThreshold(Boolean boughtFromThreshold) {
        this.boughtFromThreshold = boughtFromThreshold;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
