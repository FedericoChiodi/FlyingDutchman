package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trova un Utente dal suo username
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleted = 'N'")
    User findByUsername(@Param("username") String username);

    // Trova tutti gli utenti tranne me e quelli cancellati
    @Query("SELECT u FROM User u WHERE u <> :user AND u.deleted = 'N'")
    List<User> findAllUsersExceptMeAndDeleted(@Param("user") User user);
}