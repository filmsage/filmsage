package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class ReviewController {
    private ReviewRepository reviewDao;
    private MediaItemRepository mediaItemDao;

    public ReviewController(ReviewRepository reviewDao, MediaItemRepository mediaItemDao) {
        this.reviewDao = reviewDao;
        this.mediaItemDao = mediaItemDao;
    }

    @RequestMapping(value = "/reviews/create",
            method = RequestMethod.GET,
            params = "i")
    public String showReviewForm(Model model, @RequestParam String imdb){
        model.addAttribute("review", new Review());
        model.addAttribute("movie", mediaItemDao.findByImdb(imdb));
        return "media/reviews/create";
    }

    @PostMapping("/reviews/create")
    public String createReview(@ModelAttribute Review review){
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        reviewDao.save(review);
        return ;
    }

    @GetMapping("/reviews/{id}")
    public String showReview(Model model, @PathVariable long id) {
        Review review = reviewDao.getById(id);
        model.addAttribute()
    }
}
