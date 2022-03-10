package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class JournalController {
    private UserRepository userDao;
    private ReviewRepository reviewDao;

    public JournalController(UserRepository userDao, ReviewRepository reviewDao){
        this.userDao = userDao;
        this.reviewDao = reviewDao;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("allusers", userDao.findAll());
        return "users/index";
    }

    @GetMapping("/users/create")
    public String showCreate(Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("reviews", reviewDao.findAll());
            return "users/create";
        }

        @GetMapping("/users/{id}")
    public String showReview(@PathVariable long id, Model model){
        model.addAttribute("singleReview",reviewDao.getById(id));
        return "reviews/show";
        }

    @PostMapping("/reviews/{id}/edit")
    public String submitReviewEdit(@ModelAttribute Review reviewToEdit, @PathVariable long id) {
        reviewToEdit.setUser(userDao.getById(1L));
        reviewDao.save(reviewToEdit);
        return "redirect:/reviews/" + id;
    }

    @PostMapping("/reviews/create")
    public String createReview(@ModelAttribute Review review) {
        review.setUser(userDao.getById(1L));
        reviewDao.save(review);
        return "redirect:/reviews";
    }


    @GetMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable long id) {
        reviewDao.delete(reviewDao.getById(id));
        return "redirect:/reviews";
    }


}
