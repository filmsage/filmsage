package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReportController {
    private ReviewRepository reviewDao;
    private EmailService emailService;

    public ReportController(ReviewRepository reviewDao, EmailService emailService){
        this.reviewDao = reviewDao;
        this.emailService = emailService;
    }

    @GetMapping("/report/{id}")
    public String reportContent(Model model, @PathVariable String id){
       model.addAttribute("report",reviewDao.findReviewById(Long.parseLong(id)));
       return "report/reports";
    }

    @GetMapping("/report")
    @ResponseBody
    public String reportAbuse(@RequestParam long id, @RequestParam String type){
        switch(type){
            case "review":
                Review review = reviewDao.getById(id);
                String reviewUrl = String.format("https://filmsage.net/reviews/show?r=%d&imbd=%s", review.getId(), review.getMediaItem().getImdb());
                emailService.prepareAbuse(reviewUrl);
                break;
            case "journal":
                String journalUrl = String.format("https://filmsage.net/journals?id=%d", id);
                emailService.prepareAbuse(journalUrl);
                break;
            case "watchlist":
                String watchlistUrl = String.format("https://filmsage.net/watchlist/%d/show", id);
                emailService.prepareAbuse(watchlistUrl);
                break;
        }
        return "This content has been flagged for review.";
    }

}
