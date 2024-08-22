package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private List<Product> products;

    @BeforeEach
    public void setup() {
        user = new User();
        products = new ArrayList<>();
    }

    @Test
    public void testGetSetUserID() {
        Long userID = 1L;
        user.setUserID(userID);
        assertEquals(userID, user.getUserID());
    }

    @Test
    public void testGetSetUsername() {
        String username = "test_user";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetSetPassword() {
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testGetSetFirstname() {
        String firstname = "John";
        user.setFirstname(firstname);
        assertEquals(firstname, user.getFirstname());
    }

    @Test
    public void testGetSetSurname() {
        String surname = "Doe";
        user.setSurname(surname);
        assertEquals(surname, user.getSurname());
    }

    @Test
    public void testGetSetBirthdate() {
        Date birthdate = Date.valueOf("1990-01-01");
        user.setBirthdate(birthdate);
        assertEquals(birthdate, user.getBirthdate());
    }

    @Test
    public void testGetSetAddress() {
        String address = "123 Main St";
        user.setAddress(address);
        assertEquals(address, user.getAddress());
    }

    @Test
    public void testGetSetCivicNumber() {
        Short civicNumber = 123;
        user.setCivic_number(civicNumber);
        assertEquals(civicNumber, user.getCivic_number());
    }

    @Test
    public void testGetSetCap() {
        Short cap = (short) 123;
        user.setCap(cap);
        assertEquals(cap, user.getCap());
    }

    @Test
    public void testGetSetCity() {
        String city = "Springfield";
        user.setCity(city);
        assertEquals(city, user.getCity());
    }

    @Test
    public void testGetSetState() {
        String state = "IL";
        user.setState(state);
        assertEquals(state, user.getState());
    }

    @Test
    public void testGetSetEmail() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetSetCelNumber() {
        String celNumber = "1234567890";
        user.setCel_number(celNumber);
        assertEquals(celNumber, user.getCel_number());
    }

    @Test
    public void testGetSetRole() {
        String role = "USER";
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void testGetSetDeleted() {
        Character deleted = 'N';
        user.setDeleted(deleted);
        assertEquals(deleted, user.isDeleted());
    }

    @Test
    public void testGetSetProducts() {
        user.setProducts(products);
        assertEquals(products, user.getProducts());
    }

}
