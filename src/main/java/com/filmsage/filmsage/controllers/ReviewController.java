package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.repositories.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/userReview")
//    public String createReview(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body, Model model){
//                Review r1 = new Review();
//                r1.setTitle(title);
//                r1.setBody(body);
//                reviewDao.save(r1);
//                return "test";
//    }

    @PostMapping("/review")
    @ResponseBody
    public String createReview(@ModelAttribute Review review){
        reviewDao.save(review);
        return review.getTitle();
    }




}
