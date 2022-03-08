package com.filmsage.filmsage.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

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
    private String firstName;

    @Column(nullable = true, length = 255)
    private String lastName;

    @Column(nullable = true, length = 255)
    private String country;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Journal> journals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Collection> collections;

    @ManyToMany
    @JoinTable(
            name = "user_reviews_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<Review> likedReviews;

    public User() {
    }

    public User(long id, String email, String username, String password, boolean admin, String firstName, String lastName, String country, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.firstName = firstName;
        this.lastName = lastName;
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
        return firstName;
    }

    public void setFirst_name(String firstName) {
        this.firstName = firstName;
    }

    public String getLast_name() {
        return lastName;
    }

    public void setLast_name(String lastName) {
        this.lastName = lastName;
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
