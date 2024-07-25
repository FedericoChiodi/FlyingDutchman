package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByRole(String role);

    List<User> findAllByDeletedFalseAndUserIDNot(Long userID);

    default User create(String username, String password, String firstname, String surname, Date birthdate, String address, Short civic_number, Short cap, String city, String state, String email, String cel_number, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setBirthdate(birthdate);
        user.setAddress(address);
        user.setCivic_number(civic_number);
        user.setCap(cap);
        user.setCity(city);
        user.setState(state);
        user.setEmail(email);
        user.setCel_number(cel_number);
        user.setRole(role);
        user.setDeleted(false);
        return save(user);
    }

    default void update(User user) {
        save(user);
    }

    default void delete(User user) {
        user.setDeleted(true);
        save(user);
    }
}
