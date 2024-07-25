package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.owner = :owner")
    List<Product> findByOwner(@Param("owner") User owner);

    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    List<Product> findAllProducts();

    @Query("SELECT p FROM Product p WHERE p.owner = :user AND p.deleted = false")
    List<Product> findByOwnerNotDeleted(@Param("user") User user);

    @Query("SELECT p FROM Product p JOIN p.auctions a WHERE a.id = :auctionId")
    Product findByAuction(@Param("auctionId") Long auctionId);

}
