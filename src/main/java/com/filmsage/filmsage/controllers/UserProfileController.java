package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserProfileController {
    private ReviewRepository reviewDao;
    private UserRepository userDao;
    private UserContentRepository userContentDao;
    private OMDBRequester omdbRequester;


    public UserProfileController(ReviewRepository reviewDao, UserRepository userDao, UserContentRepository userContentDao, OMDBRequester omdbRequester) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.userContentDao = userContentDao;
        this.omdbRequester = omdbRequester;
    }

    //    Get the user's review on their profile page
//    @GetMapping("/profile")
//    public String viewUserReview(Model model, @PathVariable long id) {
//        UserReview userReview = getUserReview();
//        model.addAttribute("user", userContent);
//        return "/profile/create";
//    }
    @GetMapping("/profile")
    public String showUserProfileReview(Model model, @PathVariable String imdb ) {
//        get the usersContent to link all the users content and reviews
       UserContent userContent = getUserContent();
       model.addAttribute("user", userContent);
        model.addAttribute("userReview", userReview());
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "redirect:/profile";
    }

//    @PostMapping("profile/review/create")
//    public String showUserReview(@ModelAttribute UserReview userReview, @PathVariable long id) {
//        UserContent userContent = getUserContent();
//        userReview.setUserContent(userContent);
//        reviewDao.save(userReview);
//        return "profile/create";
//    }


    @RequestMapping (value ="/profile/edit",
            method = RequestMethod.GET,
            params = "userId")
    public String showUserReviewEdit(Model model,@PathVariable String imdb, @RequestParam long userId) {
        model.addAttribute("userReview", reviewDao.getById(userId));
        model.addAttribute("movie", omdbRequester.getMovie(imdb));
        return "redirect:/profile";
}

//    @PostMapping("profile/reviews/edit")
//    public String submitUserReviewEdit(@ModelAttribute UserReview UserReview, @PathVariable Strin imdb){
//        userReview.setUserContent(getUserContent());
//       userReview.setUserContent
//    }

    private UserContent getUserContent() {
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
return userContentDao.findUserContentByUser(principle.getUser());

    }
}



