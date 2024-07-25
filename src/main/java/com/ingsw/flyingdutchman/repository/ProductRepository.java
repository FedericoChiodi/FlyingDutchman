package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwner(User owner);
    List<Product> findByOwnerAndDeletedFalse(User owner);
    List<Product> findByCategory(Category category);
    Product findByAuction(Auction auction);
}
