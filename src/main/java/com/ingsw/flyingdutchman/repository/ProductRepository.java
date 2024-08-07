package com.ingsw.flyingdutchman.repository;

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
    @Query("SELECT p FROM Product p  WHERE p.owner = :owner AND p.deleted = 'N'")
    List<Product> findByOwnerAndDeletedFalse(@Param("owner") User owner);

    // Trova i Prodotti di una determinata Categoria
    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p, Auction a WHERE p.deleted = 'N' AND a.product_sold = 'N' AND p.owner = :owner")
    List<Product> findProductByOwnerNotDeletedNotSold(@Param("owner") User owner);

    @Query("SELECT p FROM Product p, Auction a WHERE a.product_sold = 'N' AND p.deleted = 'N' AND p.owner = :owner AND a.deleted = 'Y'")
    List<Product> findProductByOwnerNotDeletedNotSoldNotInAuction(@Param("owner") User owner);

    @Query("SELECT p FROM Product p, Auction a WHERE a.product_sold = 'N' AND p.deleted = 'N' AND p.productID = :ID AND a.deleted = 'Y'")
    Product findProductByIdNotDeletedNotSoldNotInAuction(@Param("ID") Long ID);
}