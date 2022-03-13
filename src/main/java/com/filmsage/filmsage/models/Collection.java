package com.filmsage.filmsage.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private UserContent userContent;

    @ManyToOne
    @JoinColumn (name = "media_id")
    private MediaItem mediaItem;

    public Collection() {}

    public Collection(long id, String title, Timestamp createdAt) {
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
