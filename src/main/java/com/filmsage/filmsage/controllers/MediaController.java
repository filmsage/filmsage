package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.models.json.MediaSearchMapped;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MediaController {

    OMDBRequester omdbRequester;
    ReviewRepository reviewDao;

    public MediaController(OMDBRequester omdbRequester, ReviewRepository reviewDao) {
        this.omdbRequester = omdbRequester;
        this.reviewDao = reviewDao;
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "search/search";
    }

    @GetMapping("/movies/{imdb}")
    public String getMoviesById(@PathVariable String imdb, Model model) {
        MediaItemMapped movie = omdbRequester.getMovie(imdb);
        List<Review> reviews = reviewDao.findAllByMediaItemImdb(imdb);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);
        return "movies/show";
    }

    @RequestMapping(value = "/search", params = "q")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        List<MediaSearchMapped> movies = omdbRequester.searchMovie(query);
        model.addAttribute("movies", movies);
        return "search/results";
    }
}
