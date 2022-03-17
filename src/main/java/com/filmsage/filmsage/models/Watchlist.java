package com.filmsage.filmsage.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "collections")
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserContent userContent;

    @ManyToMany
    @JoinTable(
            name = "has_media",
            joinColumns = @JoinColumn(name = "coll_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id"))
    private Set<MediaItem> mediaItems;

    public Watchlist() {}

    public Watchlist(long id, String title, Timestamp createdAt, UserContent userContent, Set<MediaItem> mediaItems) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.userContent = userContent;
        this.mediaItems = mediaItems;
    }

    public UserContent getUserContent() {
        return userContent;
    }

    public void setUserContent(UserContent userContent) {
        this.userContent = userContent;
    }

    public Set<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(Set<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public Watchlist(long id, String title, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
