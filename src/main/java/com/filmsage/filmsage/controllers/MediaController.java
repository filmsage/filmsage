package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItemMapped;
import com.filmsage.filmsage.models.MediaSearchMapped;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @GetMapping("/search_test")
    @ResponseBody
    public String searchMoviesTest() {
        List<MediaSearchMapped> movies = omdbRequester.searchMovie("titan");
        StringBuffer sb = new StringBuffer();
        for (MediaSearchMapped movie : movies) {
            sb.append(movie.getTitle() + "\n" + movie.getYear() + "\n" + movie.getImdbID() + "\n");
        }
        return sb.toString();
    }
}
