package com.filmsage.filmsage.models;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="media_items")
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "imdb")
    private String imdb;

    @Column(nullable = false, length = 255, name = "title")
    private String title;

    @Column(length = 63, name = "year")
    private String year;

    @Column(length = 100, name = "genre")
    private String genre;

    @Column(nullable = false, length = 255, name = "poster")
    private String poster;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mediaItem")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "mediaItems")
    private List<Watchlist> watchlists = new ArrayList<>();

    public MediaItem() {}

    public MediaItem(String imdb, String title, String year, String genre, String poster) {
        this.imdb = imdb;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
    }

    public MediaItem(Long id, String imdb, String title, String year, String genre, String poster) {
        this.id = id;
        this.imdb = imdb;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Watchlist> getCollections() {
        return watchlists;
    }

    public void setCollections(List<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }
}
