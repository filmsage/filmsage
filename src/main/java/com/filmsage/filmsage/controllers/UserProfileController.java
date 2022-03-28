package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.LikesService;
import com.filmsage.filmsage.services.OMDBRequester;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserProfileController {
    private ReviewRepository reviewDao;
    private UserRepository userDao;
    private UserContentRepository userContentDao;
    private OMDBRequester omdbRequester;
    private LikesService likesService;
    private UserContentService userContentService;


    public UserProfileController(ReviewRepository reviewDao, UserRepository userDao, UserContentRepository userContentDao, OMDBRequester omdbRequester, LikesService likesService, UserContentService userContentService) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.userContentDao = userContentDao;
        this.omdbRequester = omdbRequester;
        this.likesService = likesService;
        this.userContentService = userContentService;
    }


    @GetMapping("/profile/{userId}")
    public String showUserProfileReview(Model model, @PathVariable String userId, Principal principal) {
//        get the usersContent to link all the users content and reviews
        long id = Long.parseLong(userId);
        UserContent userContent = userContentDao.getById(id);
        model.addAttribute("user", userDao.getById(id));
//        get back a collection of items that the user reviewed
        model.addAttribute("reviews", reviewDao.findAllByUserContent_Id(id));
        model.addAttribute("usercontent", userContent);
        model.addAttribute("likes", likesService.getAllLikesForUser(userContent));
        if (principal != null) {
            model.addAttribute("isOwner", userContentService.getUserContent().getId() == userContent.getId());
        }
        return "profile/profile-page";
    }

//    get the users own review to display on their profile page
    @GetMapping("/profile")
    public String showOwnUserProfile(){
        return "redirect:/profile/" + userContentService.getUserContent().getId();
    }

//    get the
    @GetMapping("/profile/{userId}/edit")
    public String getUpdateProfileForm(Model model, @PathVariable String userId) {
        long id = Long.parseLong(userId);
        User user = userDao.getById(Long.parseLong(userId));
        if (userContentService.getUserContent().getId() == user.getUserContent().getId() ||
                userContentService.isAdmin()) {
            model.addAttribute("user", userContentDao.getById(Long.parseLong(userId)));
            return "profile/update-profile";
        } else {
            return "redirect:/profile/" + userId;
        }
//       model.addAttribute("user", getUserContent());
    }

    @PostMapping("/profile/{id}/edit")
    public String submitUpdatedProfile(@ModelAttribute UserContent userContent, @PathVariable long id) {
//        userContent = user
        UserContent existingUser = userContentDao.getById(id);
        if (userContentService.getUserContent().getId() == existingUser.getId() ||
                userContentService.isAdmin()) {
            existingUser.setFirstName(userContent.getFirstName());
            existingUser.setLastName(userContent.getLastName());
            existingUser.setCountry(userContent.getCountry());
            userContentDao.save(existingUser);
        }
        return "redirect:/profile/" + id;
    }

}



