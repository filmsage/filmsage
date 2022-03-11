package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private MediaItemRepository mediaItemDao;
    private OMDBRequester omdbRequester;

    public ReviewController(ReviewRepository reviewDao, MediaItemRepository mediaItemDao, OMDBRequester omdbRequester) {
        this.reviewDao = reviewDao;
        this.mediaItemDao = mediaItemDao;
        this.omdbRequester = omdbRequester;

    }

    @GetMapping("/movies/{imdb}/reviews/create")
    public String showReviewForm(Model model, @PathVariable String imdb){
        model.addAttribute("review", new Review());
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/create";
    }

    @PostMapping("/movies/{imdb}/reviews/create")
    public String createReview(@ModelAttribute Review review, @PathVariable String imdb){
        // check if record exists already
        MediaItem mediaItem = null;
        if (mediaItemDao.existsByImdb(imdb)) {
            mediaItem = mediaItemDao.findByImdb(imdb);
        } else {
            mediaItem = mediaItemDao.save(new MediaItem(imdb));
        }
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        review.setMediaItem(mediaItem);
        reviewDao.save(review);
        return String.format("redirect:/movies/%s/reviews/show?r=%d", imdb, review.getId());
    }

    @RequestMapping(value = "/movies/{imdb}/reviews/show",
            method = RequestMethod.GET,
            params = "r")
    public String showReview(Model model, @PathVariable String imdb, @RequestParam long r) {
        model.addAttribute("review", reviewDao.getById(r));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/review";
    }
}
