package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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
    private Boolean product_sold;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productID", nullable = false)
    private Product product_auctioned;

    @OneToMany(mappedBy = "auction")
    private List<Threshold> thresholds;

    public Long getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(Long auctionID) {
        this.auctionID = auctionID;
    }

    public Timestamp getOpening_timestamp() {
        return opening_timestamp;
    }

    public void setOpening_timestamp(Timestamp opening_timestamp) {
        this.opening_timestamp = opening_timestamp;
    }

    public Timestamp getClosing_timestamp() {
        return closing_timestamp;
    }

    public void setClosing_timestamp(Timestamp closing_timestamp) {
        this.closing_timestamp = closing_timestamp;
    }

    public Boolean getProduct_sold() {
        return product_sold;
    }

    public void setProduct_sold(Boolean product_sold) {
        this.product_sold = product_sold;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Product getProduct_auctioned() {
        return product_auctioned;
    }

    public void setProduct_auctioned(Product product_auctioned) {
        this.product_auctioned = product_auctioned;
    }

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<Threshold> thresholds) {
        this.thresholds = thresholds;
    }
}
