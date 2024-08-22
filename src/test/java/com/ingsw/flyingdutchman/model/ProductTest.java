package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private Category category;
    private User owner;
    private Order order;
    private List<Auction> auctions;

    @BeforeEach
    public void setup() {
        product = new Product();
        category = new Category();
        owner = new User();
        order = new Order();
        auctions = new ArrayList<>();
    }

    @Test
    public void testGetSetProductID() {
        Long productID = 1L;
        product.setProductID(productID);
        assertEquals(productID, product.getProductID());
    }

    @Test
    public void testGetSetDescription() {
        String description = "Product Description";
        product.setDescription(description);
        assertEquals(description, product.getDescription());
    }

    @Test
    public void testGetSetMinPrice() {
        Float minPrice = 10.99f;
        product.setMin_price(minPrice);
        assertEquals(minPrice, product.getMin_price());
    }

    @Test
    public void testGetSetStartingPrice() {
        Float startingPrice = 20.50f;
        product.setStarting_price(startingPrice);
        assertEquals(startingPrice, product.getStarting_price());
    }

    @Test
    public void testGetSetCurrentPrice() {
        Float currentPrice = 15.75f;
        product.setCurrent_price(currentPrice);
        assertEquals(currentPrice, product.getCurrent_price());
    }

    @Test
    public void testGetSetImage() {
        String image = "image.jpg";
        product.setImage(image);
        assertEquals(image, product.getImage());
    }

    @Test
    public void testGetSetDeleted() {
        Character deleted = 'N';
        product.setDeleted(deleted);
        assertEquals(deleted, product.getDeleted());
    }

    @Test
    public void testGetSetCategory() {
        product.setCategory(category);
        assertEquals(category, product.getCategory());
    }

    @Test
    public void testGetSetOwner() {
        product.setOwner(owner);
        assertEquals(owner, product.getOwner());
    }

    @Test
    public void testGetSetOrder() {
        product.setOrder(order);
        assertEquals(order, product.getOrder());
    }

    @Test
    public void testGetSetAuctions() {
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        auctions.add(auction1);
        auctions.add(auction2);

        product.setAuctions(auctions);
        assertEquals(auctions, product.getAuctions());
        assertEquals(2, product.getAuctions().size());
    }

}
