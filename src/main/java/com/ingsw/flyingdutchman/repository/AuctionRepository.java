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
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // Trovare aste aperte e non cancellate da un Prodotto
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned = :product AND a.deleted = 'N' AND a.closing_timestamp IS NULL")
    List<Auction> findByProductOpenNotDeleted(@Param("product") Product product);

    // Trovare aste dall'Utente che le ha aperte
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.owner = :owner")
    List<Auction> findByOwner(@Param("owner") User owner);

    // Trovare aste aperte dall'Utente e non cancellate
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.owner = :owner AND a.deleted = 'N' AND a.closing_timestamp IS NULL AND a.product_sold = 'N'")
    List<Auction> findOpenAuctionsByOwnerNotDeleted(@Param("owner") User owner);

    // Trova tutte le aste aperte tranne quelle dell'Utente
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.owner <> :owner AND a.product_auctioned.deleted = 'N' AND a.closing_timestamp IS NULL AND a.product_sold = 'N'")
    List<Auction> findAllOpenAuctionsExceptUser(@Param("owner") User owner);

    // Trova Aste per la descrizione del Prodotto
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.description = :description AND a.deleted = 'N' AND a.product_sold = 'N'")
    List<Auction> findAuctionByProductDescription(@Param("description") String description);

    // Trova Aste dalla categoria del Prodotto
    @Query("SELECT a FROM Auction a WHERE a.product_auctioned.category = :category AND a.deleted = 'N' AND a.product_sold = 'N'")
    List<Auction> findAuctionsByCategory(@Param("category") Category category);
}