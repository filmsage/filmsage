package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Collection;
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
        return "collections/home";//"collections/index";
    }

//    to View an individual collection
    @GetMapping("/collections/{id}")
    public String getCollection(@PathVariable long id, Model model) {
        model.addAttribute("singleCollection", usersDao.getById(id));
        return "collections/show";
    }

//  view the form to create a new movie collection
    @GetMapping("/collections/create")
    public String createMovieCollection(Model model) {
        model.addAttribute("newCollection", new Collection());
        return "collections/create";
    }


    @PostMapping("/collections/{id}/edit")
    public String submitMovieCollection(@ModelAttribute Collection submitCollection, @PathVariable long id) {
//       Collection submitCollection = usersDao.getById(id);
//       submitCollection.setTitle(title);
//       submitCollection.setBody(body);
       return "redirect:/collections";
    }

    @GetMapping("collections/{id}/delete")
    public String deleteCollection(@PathVariable long id) {
        usersDao.deleteById(id);
        return "redirect/collections";
    }
}
