package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Collection;
import com.filmsage.filmsage.repositories.CollectionRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CollectionController {
    private CollectionRepository collectionsDao;
    private UserRepository usersDao;


    public CollectionController(CollectionRepository collectionsDao, UserRepository usersDao) {
        this.usersDao = usersDao;
        this.collectionsDao = collectionsDao;
    }

    @GetMapping("/collections")
    public String viewCollections(Model model) {
        model.addAttribute("newCollection", new Collection());
        return "collect/index";
    }

//    to View an individual collection
    @GetMapping("/collections/show/{id}")
    public String getCollection(@PathVariable long id, Model model) {
        model.addAttribute("singleCollection", collectionsDao.getById(id));
        return "collect/show";
    }

////  view the form to create a new movie collection
    @GetMapping("/collections/create")
    public String createMovieCollection(Model model) {
        model.addAttribute("newCollection", new Collection());
        return "collect/create";
    }

    @PostMapping("/collections/{id}/edit")
    public String submitMovieCollection(@ModelAttribute Collection submitCollection, @PathVariable long id) {
//       Collection submitCollection = usersDao.getById(id);
//       submitCollection.setTitle(title);
//       submitCollection.setBody(body);
       return "redirect:/collect" + id;
    }

    @GetMapping("collections/{id}/delete")
    public String deleteCollection(@PathVariable long id) {
        collectionsDao.delete(collectionsDao.getById(id));
        return "redirect/collect";
    }
}
