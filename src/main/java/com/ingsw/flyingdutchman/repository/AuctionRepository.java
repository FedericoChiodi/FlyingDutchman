package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByProductOwner(User owner);
    List<Auction> findByProductOwnerAndDeletedIsFalseAndClosingTimestampIsNull(User owner);
    List<Auction> findByOwner(User owner);
    List<Auction> findByOwnerAndDeletedIsFalseAndClosingTimestampIsNullAndProductSoldIsFalse(User owner);
    List<Auction> findByOwnerIsNotAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(User owner);
    List<Auction> findByDescriptionContainingAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(String description);
    List<Auction> findByCategoryAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(Category category);

    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.owner = :owner AND a.product_auctioned.productID <> 1")
    List<Auction> findByOwnerNotPremium(@Param("owner") User owner);

    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.productID <> 1")
    List<Auction> findAllAuctionsExceptPremium();
}
