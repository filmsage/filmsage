package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.models.json.MediaSearchMapped;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        return "search/search";
    }

    // all these methods with the @ResponseBody annotation need to be connected to templates
    // and get their values consumed. the users are hungry, please feed the users our content
    @GetMapping("/movies/{id}")
    public String getMoviesById(@PathVariable String id, Model model) {
        MediaItemMapped movie = omdbRequester.getMovie(id);
        model.addAttribute("movie", movie);
        return "movies/show";
    }

    @RequestMapping(value = "/search", params = "q")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        List<MediaSearchMapped> movies = omdbRequester.searchMovie(query);
        model.addAttribute("movies", movies);
        return "search/results";
    }
}
