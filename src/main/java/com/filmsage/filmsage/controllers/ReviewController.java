package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.*;
import com.filmsage.filmsage.models.auth.Role;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.auth.RoleRepository;
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
    private RoleRepository roleDao;

    public ReviewController(ReviewRepository reviewDao,
                            MediaItemService mediaItemService,
                            UserContentRepository userContentDao,
                            UserContentService userContentService,
                            OMDBRequester omdbRequester,
                            LikesService likesService,
                            RoleRepository roleDao) {
        this.reviewDao = reviewDao;
        this.mediaItemService = mediaItemService;
        this.userContentDao = userContentDao;
        this.userContentService = userContentService;
        this.omdbRequester = omdbRequester;
        this.likesService = likesService;
        this.roleDao = roleDao;
    }

//    @RequestMapping(value = "/movies/{imdb}/reviews/show",
//            method = RequestMethod.GET,
//            params = "r")
//    public String showReview(Model model, @PathVariable String imdb, @RequestParam long r) {
//        Review review = reviewDao.getById(r);
//        model.addAttribute("review", review);
//        model.addAttribute("movie", omdbRequester.getMovie(imdb));
//        model.addAttribute("likes", review.getUserLikes().size());
//        return "media/reviews/review";
//    }

    @GetMapping("/reviews/show")
    public String showReview(Model model,
                             @RequestParam long r,
                             @RequestParam String imdb) {
        Review review = reviewDao.getById(r);
        model.addAttribute("review", review);
        model.addAttribute("movie", mediaItemService.getTempMediaItemRecord(imdb));
        model.addAttribute("likes", review.getUserLikes().size());

        return "reviews/review";
    }

    @GetMapping("/reviews/index")
    public String showReview(Model model,
                             @RequestParam(required = false) String imdb,
                             @RequestParam(required = false, name = "user") String userId) {
        if (imdb == null && userId == null) {
            model.addAttribute("reviews", reviewDao.findAll());
            model.addAttribute("for", "all");
        } else if (imdb == null) {
            model.addAttribute("reviews", reviewDao.findAllByUserContent_Id(Long.parseLong(userId)));
            model.addAttribute("user", userContentDao.getById(Long.parseLong(userId)));
            model.addAttribute("for", "user");
        } else if (userId == null) {
            model.addAttribute("reviews", reviewDao.findAllByMediaItemImdb(imdb));
            model.addAttribute("movie", mediaItemService.getTempMediaItemRecord(imdb));
            model.addAttribute("for", "movie");
        }
        return "reviews/review-list";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reviews/create")
    public String showReviewForm(Model model, @RequestParam String imdb) {
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        // store it into the model for retrieval later
        model.addAttribute("user", userContent);
        // since we're creating a new review, we give it a fresh new Review to work with
        model.addAttribute("review", new Review());
        model.addAttribute("movie", mediaItemService.getTempMediaItemRecord(imdb));
        return "reviews/create";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/reviews/create")
    public String createReview(@ModelAttribute Review review, @RequestParam String imdb){
        // check if record exists already
        MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        // set the userContent field in the Review
        review.setUserContent(userContent);
        review.setCreatedAt(new Timestamp(System.currentTimeMillis())); // timestamp review
        review.setMediaItem(mediaItem); // associate MediaItem with review
        // persist the review (ie store it in the database)
        review = reviewDao.save(review);
        // user automatically likes their new review
        likesService.initialLikeReview(review.getId());
        return String.format("redirect:/reviews/show?imdb=%s&r=%d", imdb, review.getId());
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reviews/edit")
    public String showEditForm(Model model, @RequestParam String imdb, @RequestParam long r) {
        // get the UserContent object which links to all that user's user-created content
        UserContent userContent = userContentService.getUserContent();
        model.addAttribute("user", userContent);
        model.addAttribute("review", reviewDao.getById(r));
        model.addAttribute("movie", mediaItemService.getTempMediaItemRecord(imdb));
        return "reviews/edit";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/reviews/edit")
    public String submitEdit(@ModelAttribute Review review, @RequestParam String imdb){
        // check if record exists already and prepare it for review
        MediaItem mediaItem = mediaItemService.getTempMediaItemRecord(imdb);
        // set the userContent field in the Review
        review.setUserContent(userContentService.getUserContent());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis())); // timestamp review
        review.setMediaItem(mediaItem); // associate MediaItem with review
        // persist the review (ie store it in the database)
        reviewDao.save(review);
        return String.format("redirect:/reviews/show?imdb=%s&r=%d", imdb, review.getId());
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/reviews/delete")
    public String submitDelete(@RequestParam(name = "review-id") long id){
        Review review = reviewDao.getById(id);
        // check if record exists already and prepare it for review
        if (userContentService.getUserContent().getId() == review.getUserContent().getId() ||
                userContentService.getUser().getRoles().contains(roleDao.findByName("ROLE_ADMIN"))) {
            System.out.println("User matches or role is admin");
            for(UserContent usersWhoLike : review.getUserLikes()) {
                usersWhoLike.getLikedReviews().remove(review);
                review.getUserLikes().remove(usersWhoLike);
            }
            reviewDao.delete(review);
        }
        return String.format("redirect:/reviews/index?user=%s", review.getUserContent().getId());
    }
}
