package com.filmsage.filmsage.Models;

import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

@Entity
@Table(name="medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false, length = 300)
    private String title;

    @OneToMany
    @JoinColumn(name = "user_id")
    private User user;

    public Media(){

    }

    public Media(Long id, String body, String title, User user) {
        this.id = id;
        this.body = body;
        this.title = title;
        this.user = user;
    }


}
