package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.repositories.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class ReviewController {
    private ReviewRepository reviewDao;

    public ReviewController(ReviewRepository reviewDao) {
        this.reviewDao = reviewDao;
    }

    @GetMapping("/review")
    public String showReviewForm(Model model){
        model.addAttribute("review", new Review());
        return "review-form";
    }

    @PostMapping("/review")
    @ResponseBody
    public String createReview(@ModelAttribute Review review){
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        reviewDao.save(review);
        return review.getTitle();
    }




}
