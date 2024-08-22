package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.OrderService;
import com.ingsw.flyingdutchman.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Timestamp orderTime = new Timestamp(System.currentTimeMillis());
        Float sellingPricePar = 123.456f;
        Character boughtFromThreshold = 'N';
        User buyer = new User();
        Product product = new Product();

        orderService.createOrder(orderTime, sellingPricePar, boughtFromThreshold, buyer, product);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testFindOrdersByUser() {
        User buyer = new User();
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findByBuyer(buyer)).thenReturn(orders);

        List<Order> result = orderService.findOrdersByUser(buyer);

        assertEquals(2, result.size());
        assertEquals(orders, result);
        verify(orderRepository, times(1)).findByBuyer(buyer);
    }
}
