package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItemMapped;
import com.filmsage.filmsage.models.MediaSearchMapped;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MediaController {

    OMDBRequester omdbRequester;

    public MediaController(OMDBRequester omdbRequester) {
        this.omdbRequester = omdbRequester;
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    // all these methods with the @ResponseBody annotation need to be connected to templates
    // and get their values consumed. the users are hungry, please feed the users our content
    @GetMapping("/movies/{id}")
    @ResponseBody
    public String getMoviesById(@PathVariable String id) {
        MediaItemMapped movie = omdbRequester.getMovie(id);
        return movie.getTitle();
    }

    @RequestMapping(value = "/search", params = "q")
    @ResponseBody
    public String searchMovies(@RequestParam("q") String query) {
        List<MediaSearchMapped> movies = omdbRequester.searchMovie(query);
        StringBuffer sb = new StringBuffer("<p>");
        for (MediaSearchMapped movie : movies) {
            sb.append(movie.getTitle())
                    .append(movie.getYear())
                    .append(movie.getImdbID())
                    .append("<br/>");
        }
        sb.append("</p>");
        return sb.toString();
    }

    // these are just for testing if anyone needs them. should be removed ASAP
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
