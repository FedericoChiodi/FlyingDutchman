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

    public Order createOrder(Timestamp orderTime, Float sellingPrice, Boolean boughtFromThreshold, User buyer, Product product) {
        sellingPrice = Math.round(sellingPrice * 100.0f) / 100.0f; // Arrotondamento a 2 decimali
        Order order = new Order();
        order.setOrderTime(orderTime);
        order.setSellingPrice(sellingPrice);
        order.setBoughtFromThreshold(boughtFromThreshold);
        order.setBuyer(buyer);
        order.setProduct(product);
        return orderRepository.save(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByBuyer(user);
    }

    public Order findOrderByProduct(Product product) {
        return orderRepository.findByProduct(product);
    }
}
