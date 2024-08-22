package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.AuctionRepository;
import org.jetbrains.annotations.NotNull;
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

    public void createAuction(Timestamp openingTimestamp, Product product) {
        Auction auction = new Auction();
        auction.setProduct_auctioned(product);
        auction.setOpening_timestamp(openingTimestamp);
        auction.setDeleted('N');
        auction.setProduct_sold('N');
        auctionRepository.save(auction);
    }

    public void deleteAuction(@NotNull Auction auction) {
        auction.setDeleted('Y');
        auctionRepository.save(auction);
    }

    public void updateAuction(Auction auction) {
        auctionRepository.save(auction);
    }

    public Auction findAuctionById(Long auctionID) {
        return auctionRepository.findById(auctionID).orElse(null);
    }

    public List<Auction> findByProductOpenNotDeleted(Product product) {
        return auctionRepository.findByProductOpenNotDeleted(product);
    }

    public List<Auction> findByOwner(User user) {
        return auctionRepository.findByOwner(user);
    }

    public List<Auction> findOpenAuctionsByOwnerNotDeleted(User user) {
        return auctionRepository.findOpenAuctionsByOwnerNotDeleted(user);
    }

    public List<Auction> findAllOpenAuctionsExceptUser(User user) {
        return auctionRepository.findAllOpenAuctionsExceptUser(user);
    }

    public List<Auction> findAuctionByProductDescription(String description) {
        return auctionRepository.findAuctionByProductDescription(description);
    }

    public List<Auction> findAuctionsByCategory(Category category) {
        return auctionRepository.findAuctionsByCategory(category);
    }
}