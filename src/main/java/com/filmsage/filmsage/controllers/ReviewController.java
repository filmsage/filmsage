package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.*;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.services.LikesService;
import com.filmsage.filmsage.services.MediaItemService;
import com.filmsage.filmsage.services.OMDBRequester;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private MediaItemService mediaItemService;
    private UserContentRepository userContentDao;
    private UserContentService userContentService;
    private OMDBRequester omdbRequester;
    private LikesService likesService;

    public ReviewController(ReviewRepository reviewDao,
                            MediaItemService mediaItemService,
                            UserContentRepository userContentDao,
                            UserContentService userContentService,
                            OMDBRequester omdbRequester,
                            LikesService likesService) {
        this.reviewDao = reviewDao;
        this.mediaItemService = mediaItemService;
        this.userContentDao = userContentDao;
        this.userContentService = userContentService;
        this.omdbRequester = omdbRequester;
        this.likesService = likesService;
    }

    @GetMapping("/movies/{imdb}/reviews/create")
    public String showReviewForm(Model model, @PathVariable String imdb){
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        // store it into the model for retrieval later
        model.addAttribute("user", userContent);
        // since we're creating a new review, we give it a fresh new Review to work with
        model.addAttribute("review", new Review());
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/create";
    }

    @PostMapping("/movies/{imdb}/reviews/create")
    public String createReview(@ModelAttribute Review review, @PathVariable String imdb){
        // check if record exists already
        MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        // set the userContent field in the Review
        review.setUserContent(userContent);
        review.setCreatedAt(new Timestamp(System.currentTimeMillis())); // timestamp review
        review.setMediaItem(mediaItem); // associate MediaItem with review
        // persist the review (ie store it in the database)
        reviewDao.save(review);
        return String.format("redirect:/movies/%s/reviews/show?r=%d", imdb, review.getId());
    }

    @RequestMapping(value = "/movies/{imdb}/reviews/show",
            method = RequestMethod.GET,
            params = "r")
    public String showReview(Model model, @PathVariable String imdb, @RequestParam long r) {
        Review review = reviewDao.getById(r);
        model.addAttribute("review", review);
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        model.addAttribute("likes", review.getUserLikes().size());
        return "media/reviews/review";
    }

    @GetMapping("/movies/{imdb}/reviews")
    public String showReview(Model model, @PathVariable String imdb) {
        model.addAttribute("reviews", reviewDao.findAllByMediaItemImdb(imdb));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/review-list";
    }

    @RequestMapping(value = "/movies/{imdb}/reviews/edit",
            method = RequestMethod.GET,
            params = "r")
    public String showEditForm(Model model, @PathVariable String imdb, @RequestParam long r) {
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        model.addAttribute("user", userContent);
        model.addAttribute("review", reviewDao.getById(r));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/edit";
    }

    @PostMapping("/movies/{imdb}/reviews/edit")
    public String submitEdit(@ModelAttribute Review review, @PathVariable String imdb){
        // check if record exists already and prepare it for review
        MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
        // set the userContent field in the Review
        review.setUserContent(userContentService.getUserContent());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis())); // timestamp review
        review.setMediaItem(mediaItem); // associate MediaItem with review
        // persist the review (ie store it in the database)
        reviewDao.save(review);
        return String.format("redirect:/movies/%s/reviews/show?r=%d", imdb, review.getId());
    }




}
