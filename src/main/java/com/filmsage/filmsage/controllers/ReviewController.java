package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.*;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

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
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = getUserContent();
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
        MediaItem mediaItem = getMediaItemRecord(imdb);
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = getUserContent();
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
        model.addAttribute("review", reviewDao.findReviewById(r));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
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
        UserContent userContent = getUserContent();
        model.addAttribute("user", userContent);
        model.addAttribute("review", reviewDao.getById(r));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "media/reviews/edit";
    }

    @PostMapping("/movies/{imdb}/reviews/edit")
    public String submitEdit(@ModelAttribute Review review, @PathVariable String imdb){
        // check if record exists already and prepare it for review
        MediaItem mediaItem = getMediaItemRecord(imdb);
        // set the userContent field in the Review
        review.setUserContent(getUserContent());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis())); // timestamp review
        review.setMediaItem(mediaItem); // associate MediaItem with review
        // persist the review (ie store it in the database)
        reviewDao.save(review);
        return String.format("redirect:/movies/%s/reviews/show?r=%d", imdb, review.getId());
    }

   private MediaItem getMediaItemRecord(String imdb) {
        // if we have a record of this imdb in our system,
       if (mediaItemDao.existsByImdb(imdb)) {
           // we retrieve that record
           return mediaItemDao.findByImdb(imdb);
       } else {
           // if not, we make a new record and return that instead
           return mediaItemDao.save(new MediaItem(imdb));
       }
   }

   private UserContent getUserContent() {
       // note: this is slightly more complex than before, I apologize
       // step 1: get the UserPrinciple which contains account identifying info
       UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       // step 2: get the UserContent object which links to all that user's user-created content
       return userContentDao.findUserContentByUser(principle.getUser());
   }

}
