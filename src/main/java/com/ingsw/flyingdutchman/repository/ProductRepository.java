package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Trova i Prodotti dall'Utente che lo vende
    List<Product> findByOwner(User owner);

    // Trova i Prodotti dall'Utente che lo vende, ma non cancellato
    List<Product> findByOwnerAndDeletedFalse(User owner);

    // Trova i Prodotti di una determinata Categoria
    List<Product> findByCategory(Category category);

    // Trova il prodotto venduto in un'asta
    @Query("SELECT p FROM Product p JOIN Auction a WHERE a = :auction")
    Product findByAuction(@Param("auction") Auction auction);
}