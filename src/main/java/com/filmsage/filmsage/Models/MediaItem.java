package com.filmsage.filmsage.Models;



import javax.persistence.*;

@Entity
@Table(name="media_items")
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 255, name = "imdb")
    private String imdb;

    public MediaItem() {

    }

    public MediaItem(Long id, String imdb) {
        this.id = id;
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
