package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Long> {

    // Trova le Prenotazioni piazzate da un Utente
    List<Threshold> findByUser(User user);

    // Trova le Prenotazioni di un'Asta
    List<Threshold> findByAuction(Auction auction);
}