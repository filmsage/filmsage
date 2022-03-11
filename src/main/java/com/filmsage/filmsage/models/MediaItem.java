package com.filmsage.filmsage.models;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="media_items")
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "imdb")
    private String imdb;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mediaItem")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mediaItem")
    private List<Collection> collections;


    public MediaItem() {}

    public MediaItem(Long id, String imdb) {
        this.id = id;
        this.imdb = imdb;
    }

    public MediaItem(String imdb) {
        this.imdb = imdb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}
