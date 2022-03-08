package com.filmsage.filmsage.models;

import javax.persistence.*;

@Entity
@Table(name = "journals")
public class Journals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private Users user;
}
