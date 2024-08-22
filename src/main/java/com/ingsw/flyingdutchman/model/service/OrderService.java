package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(Timestamp orderTime, Float sellingPricePar, Character boughtFromThreshold, User buyer, Product product) {
        Float sellingPrice = Math.round(sellingPricePar * 100.0f) / 100.0f; // Arrotondamento a 2 decimali
        Order order = new Order();
        order.setOrderTime(orderTime);
        order.setSellingPrice(sellingPrice);
        order.setBoughtFromThreshold(boughtFromThreshold);
        order.setBuyer(buyer);
        order.setProduct(product);
        orderRepository.save(order);
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByBuyer(user);
    }
}