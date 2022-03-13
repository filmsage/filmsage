package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.*;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private MediaItemRepository mediaItemDao;
    private UserContentRepository userContentDao;
    private OMDBRequester omdbRequester;

    public ReviewController(ReviewRepository reviewDao,
                            MediaItemRepository mediaItemDao,
                            UserContentRepository userContentDao,
                            OMDBRequester omdbRequester) {
        this.reviewDao = reviewDao;
        this.mediaItemDao = mediaItemDao;
        this.userContentDao = userContentDao;
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
        // get and set the user to the new review
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserContent userContent = userContentDao.findUserContentByUser(principle.getUser());
        review.setUserContent(userContent);
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        review.setMediaItem(mediaItem);
        System.out.println(review.getTitle());
        System.out.println(review.getUserContent().getUser().getEmail());
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

    @GetMapping("/movies/{imdb}/reviews")
    public String showReview(Model model, @PathVariable String imdb) {
        model.addAttribute("reviews", reviewDao.findAllByMediaItemImdb(imdb));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/review-list";
    }
}
