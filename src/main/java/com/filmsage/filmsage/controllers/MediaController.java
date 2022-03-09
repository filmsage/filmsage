package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItemMapped;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MediaController {

    OMDBRequester omdbRequester;

    public MediaController(OMDBRequester omdbRequester) {
        this.omdbRequester = omdbRequester;
    }

    @GetMapping("/movies_test")
    @ResponseBody
    public String getMoviesTest() {
        MediaItemMapped movie = omdbRequester.getMovie("tt5727208");
        return movie.getTitle();
    }
}
