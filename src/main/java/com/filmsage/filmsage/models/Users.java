package com.filmsage.filmsage.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 64, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean admin;

    @Column(nullable = true, length = 255)
    private String first_name;

    @Column(nullable = true, length = 255)
    private String last_name;

    @Column(nullable = true, length = 255)
    private String country;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Users() {
    }

    public Users(long id, String email, String username, String password, boolean admin, String first_name, String last_name, String country, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
