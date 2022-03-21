package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.LikesService;
import com.filmsage.filmsage.services.OMDBRequester;
import org.springframework.context.annotation.Profile;
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
    private LikesService likesService;


    public UserProfileController(ReviewRepository reviewDao, UserRepository userDao, UserContentRepository userContentDao, OMDBRequester omdbRequester, LikesService likesService) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.userContentDao = userContentDao;
        this.omdbRequester = omdbRequester;
        this.likesService = likesService;
    }


    @GetMapping("/profile/{userId}")
    public String showUserProfileReview(Model model, @PathVariable String userId) {
//        get the usersContent to link all the users content and reviews
        long id = Long.parseLong(userId);
        UserContent userContent = userContentDao.getById(id);
        model.addAttribute("user", userDao.getById(id));
//        get back a collection of items that the user reviewed
        model.addAttribute("reviews", reviewDao.findAllByUserContent_Id(id));
        model.addAttribute("usercontent", userContent);
        model.addAttribute("likes", likesService.getAllLikesForUser(userContent));
        return "profile/profile-page";
    }

//    get the users own review to display on their profile page
    @GetMapping("/profile")
    public String showOwnUserProfile(){
        return "redirect:/profile/" + getUserContent().getId();
    }

//    get the
    @GetMapping("/profile/{userId}/edit")
    public String getUpdateProfileForm(Model model, @PathVariable String userId) {
        long id = Long.parseLong(userId);
//       model.addAttribute("firstName", getUserContent().getFirstName());
//       model.addAttribute("lastName",getUserContent());
       model.addAttribute("user", getUserContent());
       return "/profile/update-Profile";
    }

    @PostMapping("/profile/{id}/edit")
    public String submitUpdatedProfile(@ModelAttribute UserContent userContent, @PathVariable long id) {
//        userContent = user
        UserContent existingUser = userContentDao.getById(id);
        existingUser.setFirstName(userContent.getFirstName());
        existingUser.setLastName(userContent.getLastName());
        existingUser.setCountry(userContent.getCountry());
        userContentDao.save(existingUser);
        return "redirect:/profile/" + id;
    }
//
//    @PostMapping("/profile")
//    public String submitUpdateProfileForm(@ModelAttribute ("update-Profile"), Model model) {
//        if()
//
//    }
//@PostMapping("/profile/{id}/edit")
//public String submitUpdateProfile(@ModelAttribute Profile profile, @PathVariable long id) {
//    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    Profile.setUserContent(user.getUserContent());
//    userContentDao.save(profile);
//    return "redirect:/profile/";
//}

    private UserContent getUserContent() {
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userContentDao.findUserContentByUser(principle.getUser());
    }
}



