package com.ingsw.flyingdutchman.model.mo;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private Long userID;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "address")
    private String address;

    @Column(name = "civic_number")
    private Short civic_number;

    @Column(name = "cap")
    private Short cap;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cel_number")
    private String cel_number;

    @Column(name = "role")
    private String role;

    @Column(name = "deleted")
    private boolean deleted;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Short getCivic_number() {
        return civic_number;
    }

    public void setCivic_number(Short civic_number) {
        this.civic_number = civic_number;
    }

    public Short getCap() {
        return cap;
    }

    public void setCap(Short cap) {
        this.cap = cap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCel_number() {
        return cel_number;
    }

    public void setCel_number(String cel_number) {
        this.cel_number = cel_number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
