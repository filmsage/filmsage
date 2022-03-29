package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.models.Watchlist;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.models.json.MediaSearchMapped;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.WatchlistRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class MediaController {

    private OMDBRequester omdbRequester;
    private ReviewRepository reviewDao;
    private WatchlistRepository watchlistDao;
    private UserContentService userContentService;

    public MediaController(OMDBRequester omdbRequester, ReviewRepository reviewDao, WatchlistRepository watchlistDao, UserContentService userContentService) {
        this.omdbRequester = omdbRequester;
        this.reviewDao = reviewDao;
        this.watchlistDao = watchlistDao;
        this.userContentService = userContentService;
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "search/search";
    }

    @RequestMapping(value = "/search", params = "q")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        if (query.isBlank()) {
            return "search/search";
        }
        query = query.strip();
        List<MediaSearchMapped> movies = omdbRequester.searchMovie(query);
        model.addAttribute("movies", movies);
        return "search/results";
    }

    @GetMapping("/movies/{imdb}")
    public String getMoviesById(@PathVariable String imdb, Model model, Principal principal) {
        MediaItemMapped movie = omdbRequester.getMovie(imdb);
        List<Review> reviews = reviewDao.findAllByMediaItemImdb(imdb);
        List<Watchlist> watchlists =watchlistDao.findAllByMediaItems_Imdb(imdb);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);
        model.addAttribute("watchlists", watchlists);
        if (principal != null) {
            List<Watchlist> userWatchlists = watchlistDao.findWatchlistsByUserContent(userContentService.getUserContent());
            model.addAttribute("userWatchlists", userWatchlists);
        }

        return "media/show";
    }

}
