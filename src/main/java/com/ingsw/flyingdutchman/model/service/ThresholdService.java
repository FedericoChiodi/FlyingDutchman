package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.repository.ThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ThresholdService {

    private final ThresholdRepository thresholdRepository;

    @Autowired
    public ThresholdService(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    public Threshold createThreshold(Float price, Timestamp reservationDate, User buyer, Auction auction) {
        price = Math.round(price * 100.0) / 100.0f; // Arrotondamento a 2 decimali
        Threshold threshold = new Threshold();
        threshold.setPrice(price);
        threshold.setReservationDate(reservationDate);
        threshold.setUser(buyer);
        threshold.setAuction(auction);
        return thresholdRepository.save(threshold);
    }

    public void updateThreshold(Threshold threshold) {
        thresholdRepository.save(threshold);
    }

    public void deleteThreshold(Threshold threshold) {
        thresholdRepository.delete(threshold);
    }

    public Threshold findThresholdById(Long thresholdID) {
        return thresholdRepository.findById(thresholdID).orElse(null);
    }

    public List<Threshold> findThresholdsByUser(User user) {
        return thresholdRepository.findByUser(user);
    }

    public List<Threshold> findThresholdsByAuction(Auction auction) {
        return thresholdRepository.findByAuction(auction);
    }

    public List<Threshold> findAllThresholds() {
        return thresholdRepository.findAll();
    }
}
