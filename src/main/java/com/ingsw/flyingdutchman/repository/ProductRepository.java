package com.ingsw.flyingdutchman.repository;

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
    @Query("SELECT p FROM Product p WHERE p.owner = :owner")
    List<Product> findByOwner(@Param("owner") User owner);

    // Trova i Prodotti dall'Utente che lo vende, ma non cancellati o venduti
    @Query("SELECT p FROM Product p WHERE p.owner = :owner AND p.deleted = 'N' AND p NOT IN (SELECT a.product_auctioned FROM Auction a WHERE a.product_auctioned = p AND a.product_sold = 'Y')")
    List<Product> findByOwnerAndDeletedFalseNotSold(@Param("owner") User owner);

    // Trova i prodotti dall'Utente che sono stati venduti e non cancellati
    @Query("SELECT p FROM Product p WHERE p.owner = :owner AND p.deleted = 'N' AND p IN (SELECT a.product_auctioned FROM Auction a WHERE a.product_auctioned = p AND a.product_sold = 'Y')")
    List<Product> findByOwnerAndDeletedFalseSold(@Param("owner") User owner);

    // Trova prodotti che non sono all'asta in questo momento
    @Query("SELECT p FROM Product p WHERE p.owner = :owner AND p.deleted = 'N' AND p NOT IN (SELECT a.product_auctioned FROM Auction a WHERE a.product_auctioned = p AND a.deleted = 'N')")
    List<Product> findByOwnerNotInAuctions(@Param("owner") User owner);
}