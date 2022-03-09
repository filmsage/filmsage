package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CollectionController {
    private UserRepository usersDao;

    public CollectionController( UserRepository usersDao) {
        this.usersDao = usersDao;
    }

    @GetMapping("/collections")
    public String collections( Model model) {
        model.addAttribute("");
        return "collections/index";
    }

//    to View an individual collection
    @GetMapping("/collections/{id}")
    public String getCollection(@PathVariable long id, Model model) {
        model.addAttribute("singleCollection", usersDao.getById(id));
        return "collections/show";
    }

//    view users collection
//    @GetMapping("/collection/create")
//    public String viewCollectionCreatedTime(Model model) {
//        model.addAttribute();
//
//
//    }
}
