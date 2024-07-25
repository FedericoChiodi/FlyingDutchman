package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "THRESHOLD")
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thresholdID")
    private Long thresholdID;

    @Column(name = "price")
    private Float price;

    @Column(name = "reservation_date")
    private Timestamp reservationDate;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "auctionID")
    private Auction auction;

    public Long getThresholdID() {
        return thresholdID;
    }

    public void setThresholdID(Long thresholdID) {
        this.thresholdID = thresholdID;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}
