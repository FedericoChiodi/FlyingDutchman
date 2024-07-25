package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(String username, String password, String firstname, String surname, Date birthdate, String address, Short civic_number, Short cap, String city, String state, String email, String cel_number, String role) {
        return userDAO.create(username, password, firstname, surname, birthdate, address, civic_number, cap, city, state, email, cel_number, role);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public User findByUserID(Long userID) {
        return userDAO.findById(userID).orElse(null);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> findByRole(String role) {
        return userDAO.findByRole(role);
    }

    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    public List<User> findAllUsersExceptMeAndDeleted(Long userID) {
        return userDAO.findAllByDeletedFalseAndUserIDNot(userID);
    }
}
