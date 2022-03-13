package com.filmsage.filmsage.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_content")
public class UserContent {

    @Id
    @Column(name = "user_id")
    private long id;

    @OneToOne
    @MapsId
    private User user;

    // non-auth related user info
    @Column(nullable = true, length = 255)
    private String firstName;

    @Column(nullable = true, length = 255)
    private String lastName;

    @Column(nullable = true, length = 255)
    private String country;

    // user created content
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private List<Journal> journals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private List<Collection> collections;

    @ManyToMany
    @JoinTable(
            name = "user_reviews_likes",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<Review> likedReviews;


    public UserContent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Journal> getJournals() {
        return journals;
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public Set<Review> getLikedReviews() {
        return likedReviews;
    }

    public void setLikedReviews(Set<Review> likedReviews) {
        this.likedReviews = likedReviews;
    }
}
