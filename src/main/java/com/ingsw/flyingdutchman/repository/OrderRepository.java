package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Trova Ordini dall'Utente che li ha piazzati
    List<Order> findByBuyer(User buyer);

}