package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.User;
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


    @GetMapping("/profile/{userId}")
    public String showUserProfileReview(Model model, @PathVariable String userId) {
//        get the usersContent to link all the users content and reviews
        long id = Long.parseLong(userId);
        model.addAttribute("user", userDao.getById(id));
//        get back a collection of items that the user reviewed
        model.addAttribute("reviews", reviewDao.findAllByUserContent_Id(id));
        return "profile/profile-page";
    }

//    get the users own review to display on their profile page
    @GetMapping("/profile")
    public String showOwnUserProfile(){
        return "redirect:/profile/" + getUserContent().getId();
    }

//    get the users firstname on their profile page
//    @GetMapping("/profile/{userFirstName}")
//    public String showOwnUserFirstName(Model model, @PathVariable String firstName) {
//        model.addAttribute("firstname",getUserContent(firstName);
//        return "profile/profile-page";
//    }


    @GetMapping("/profile")
    public String showUserFirstName(Model model, @PathVariable String firstName){
        model.addAttribute("user",userContentDao.findUserContentByFirstName((firstName)));
        return "profile/profile-page";
    }



    private UserContent getUserContent() {
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userContentDao.findUserContentByUser(principle.getUser());

    }
}


