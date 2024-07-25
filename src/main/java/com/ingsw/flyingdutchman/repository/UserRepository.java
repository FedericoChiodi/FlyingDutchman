package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Trova un utente per username
    Optional<User> findByUsername(String username);

    // Trova utenti per ruolo
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.deleted = false")
    List<User> findByRole(String role);

    // Trova tutti gli utenti non eliminati
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllUsers();

    // Trova tutti gli utenti non eliminati eccetto l'utente specificato
    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.userID <> :userID")
    List<User> findAllUsersExceptMeAndDeleted(Long userID);

}
