package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.repositories.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReportController {
    private ReviewRepository reviewDao;

    public ReportController(ReviewRepository reviewDao){
        this.reviewDao = reviewDao;
    }

    @GetMapping("/report/{id}")
    public String reportContent(Model model, @PathVariable String id){
       model.addAttribute("report",reviewDao.findReviewById(Long.parseLong(id)));
       return "report/reports";
    }

}
