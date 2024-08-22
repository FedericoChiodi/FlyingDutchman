package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class ThresholdTest {

    private Threshold threshold;
    private User user;
    private Auction auction;

    @BeforeEach
    public void setup() {
        threshold = new Threshold();
        user = new User();
        auction = new Auction();
    }

    @Test
    public void testGetSetThresholdID() {
        Long thresholdID = 1L;
        threshold.setThresholdID(thresholdID);
        assertEquals(thresholdID, threshold.getThresholdID());
    }

    @Test
    public void testGetSetPrice() {
        Float price = 100.50f;
        threshold.setPrice(price);
        assertEquals(price, threshold.getPrice());
    }

    @Test
    public void testGetSetReservationDate() {
        Timestamp reservationDate = new Timestamp(System.currentTimeMillis());
        threshold.setReservationDate(reservationDate);
        assertEquals(reservationDate, threshold.getReservationDate());
    }

    @Test
    public void testGetSetUser() {
        threshold.setUser(user);
        assertEquals(user, threshold.getUser());
    }

    @Test
    public void testGetSetAuction() {
        threshold.setAuction(auction);
        assertEquals(auction, threshold.getAuction());
    }

}
