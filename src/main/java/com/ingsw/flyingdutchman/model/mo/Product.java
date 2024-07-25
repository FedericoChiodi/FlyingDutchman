package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private Long productID;

    @Column(name = "description")
    private String description;

    @Column(name = "min_price")
    private Float min_price;

    @Column(name = "starting_price")
    private Float starting_price;

    @Column(name = "current_price")
    private Float current_price;

    @Column(name = "image")
    private String image;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerID", nullable = false)
    private User owner;

    @OneToOne(mappedBy = "product")
    private Order order;

    @OneToMany(mappedBy = "product_auctioned")
    private List<Auction> auctions;

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getMin_price() {
        return min_price;
    }

    public void setMin_price(Float min_price) {
        this.min_price = min_price;
    }

    public Float getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(Float starting_price) {
        this.starting_price = starting_price;
    }

    public Float getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Float current_price) {
        this.current_price = current_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }
}
