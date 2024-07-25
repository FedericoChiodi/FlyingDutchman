package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Auction createAuction(Timestamp openingTimestamp, Product product) {
        Auction auction = new Auction();
        auction.setProduct_auctioned(product);
        auction.setOpening_timestamp(openingTimestamp);
        auction.setDeleted(false);
        auction.setProduct_sold(false);
        return auctionRepository.save(auction);
    }

    public void deleteAuction(Auction auction) {
        auction.setDeleted(true);
        auctionRepository.save(auction);
    }

    public Auction updateAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction findAuctionById(Long auctionID) {
        return auctionRepository.findById(auctionID).orElse(null);
    }

    public List<Auction> findByProductOwner(Product product) {
        return auctionRepository.findByProductOwner(product.getOwner());
    }

    public List<Auction> findByProductOwnerOpenNotDeleted(Product product) {
        return auctionRepository.findByProductOwnerAndDeletedIsFalseAndClosingTimestampIsNull(product.getOwner());
    }

    public List<Auction> findByOwner(User user) {
        return auctionRepository.findByOwner(user);
    }

    public List<Auction> findByOwnerNotPremium(User user) {
        return auctionRepository.findByOwnerNotPremium(user);
    }

    public List<Auction> findOpenAuctionsByOwnerNotDeleted(User user) {
        return auctionRepository.findByOwnerAndDeletedIsFalseAndClosingTimestampIsNullAndProductSoldIsFalse(user);
    }

    public List<Auction> findAllOpenAuctionsExceptUser(User user) {
        return auctionRepository.findByOwnerIsNotAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(user);
    }

    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }

    public List<Auction> findAllAuctionsExceptPremium() {
        return auctionRepository.findAllAuctionsExceptPremium();
    }

    public List<Auction> findAuctionByProductDescription(String description) {
        return auctionRepository.findByDescriptionContainingAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(description);
    }

    public List<Auction> findAuctionsByCategory(Category category) {
        return auctionRepository.findByCategoryAndClosingTimestampIsNullAndProductSoldIsFalseAndDeletedIsFalse(category);
    }
}
