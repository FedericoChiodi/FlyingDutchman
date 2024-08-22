package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {

    private Auction auction;
    private Timestamp openingTimestamp;
    private Timestamp closingTimestamp;
    private Product product;

    @BeforeEach
    public void setup() {
        auction = new Auction();
        openingTimestamp = new Timestamp(System.currentTimeMillis());
        closingTimestamp = new Timestamp(System.currentTimeMillis() + 100000);
        product = new Product();
    }

    @Test
    public void testGetSetAuctionID() {
        Long auctionID = 1L;
        auction.setAuctionID(auctionID);
        assertEquals(auctionID, auction.getAuctionID());
    }

    @Test
    public void testGetSetOpeningTimestamp() {
        auction.setOpening_timestamp(openingTimestamp);
        assertEquals(openingTimestamp, auction.getOpening_timestamp());
    }

    @Test
    public void testGetSetClosingTimestamp() {
        auction.setClosing_timestamp(closingTimestamp);
        assertEquals(closingTimestamp, auction.getClosing_timestamp());
    }

    @Test
    public void testGetSetProductSold() {
        Character productSold = 'Y';
        auction.setProduct_sold(productSold);
        assertEquals(productSold, auction.getProduct_sold());
    }

    @Test
    public void testGetSetDeleted() {
        Character deleted = 'N';
        auction.setDeleted(deleted);
        assertEquals(deleted, auction.getDeleted());
    }

    @Test
    public void testGetSetProductAuctioned() {
        auction.setProduct_auctioned(product);
        assertEquals(product, auction.getProduct_auctioned());
    }

    @Test
    public void testAuctionInitialization() {
        assertNull(auction.getAuctionID());
        assertNull(auction.getOpening_timestamp());
        assertNull(auction.getClosing_timestamp());
        assertNull(auction.getProduct_sold());
        assertNull(auction.getDeleted());
        assertNull(auction.getProduct_auctioned());
    }
}
