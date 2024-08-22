package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order order;
    private User buyer;
    private Product product;

    @BeforeEach
    public void setup() {
        order = new Order();
        buyer = new User();
        product = new Product();
    }

    @Test
    public void testGetSetSellingPrice() {
        Float sellingPrice = 100.99f;
        order.setSellingPrice(sellingPrice);
        assertEquals(sellingPrice, order.getSellingPrice());
    }

    @Test
    public void testGetSetOrderTime() {
        Timestamp orderTime = new Timestamp(System.currentTimeMillis());
        order.setOrderTime(orderTime);
        assertEquals(orderTime, order.getOrderTime());
    }

    @Test
    public void testGetSetBoughtFromThreshold() {
        Character boughtFromThreshold = 'Y';
        order.setBoughtFromThreshold(boughtFromThreshold);
        assertEquals(boughtFromThreshold, order.getBoughtFromThreshold());
    }

    @Test
    public void testGetSetBuyer() {
        order.setBuyer(buyer);
        assertEquals(buyer, order.getBuyer());
    }

    @Test
    public void testGetSetProduct() {
        order.setProduct(product);
        assertEquals(product, order.getProduct());
    }

}
