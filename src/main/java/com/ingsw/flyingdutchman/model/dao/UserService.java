package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(String username, String password, String firstname, String surname, Date birthdate, String address,
                       Short civic_number, Short cap, String city, String state, String email, String cel_number,
                       String role, @NotNull String deleted) {
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
        user.setDeleted(deleted.equals("Y"));
        return userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete(@NotNull User user) {
        user.setDeleted(true); // Logicamente elimina l'utente (soft delete)
        userRepository.save(user);
    }

    public User findByUserID(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public List<User> findAllUsersExceptMeAndDeleted(@NotNull User user) {
        return userRepository.findAllUsersExceptMeAndDeleted(user.getUserID());
    }
}
