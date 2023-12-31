package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Timestamp;

public interface OrderDAO {
    public Order create(
            Timestamp order_time,
            Float selling_price,
            Boolean bought_from_threshold,
            User buyer,
            Product product
    );
    public void delete(Order order);
    public void update(Order order);
    public Order findByOrderID(Long orderID);
    public Order findByProduct(Product product);
    public Order[] findByUser(User user);
}
