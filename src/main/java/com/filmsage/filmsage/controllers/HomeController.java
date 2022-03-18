package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.repositories.MediaItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private MediaItemRepository mediaItemDao;

    public HomeController(MediaItemRepository mediaItemDao) {
        this.mediaItemDao = mediaItemDao;
    }

    @GetMapping("/")
    public String showSiteIndex(Model model) {
        model.addAttribute("movies", mediaItemDao.findAll());
        return "index";
    }

}
