package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AUCTION")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auctionID")
    private Long auctionID;

    @Column(name = "opening_timestamp")
    private Timestamp opening_timestamp;

    @Column(name = "closing_timestamp")
    private Timestamp closing_timestamp;

    @Column(name = "is_product_sold")
    private Character product_sold;

    @Column(name = "deleted")
    private Character deleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productID", nullable = false)
    private Product product_auctioned;

    public Long getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(Long auctionID) {
        this.auctionID = auctionID;
    }

    public void setOpening_timestamp(Timestamp opening_timestamp) {
        this.opening_timestamp = opening_timestamp;
    }

    public Timestamp getOpening_timestamp() { return opening_timestamp; }

    public Timestamp getClosing_timestamp() {
        return closing_timestamp;
    }

    public void setClosing_timestamp(Timestamp closing_timestamp) {
        this.closing_timestamp = closing_timestamp;
    }

    public Character getProduct_sold() {
        return product_sold;
    }

    public void setProduct_sold(Character product_sold) {
        this.product_sold = product_sold;
    }

    public Character getDeleted() {
        return deleted;
    }

    public void setDeleted(Character deleted) {
        this.deleted = deleted;
    }

    public Product getProduct_auctioned() {
        return product_auctioned;
    }

    public void setProduct_auctioned(Product product_auctioned) {
        this.product_auctioned = product_auctioned;
    }
}
