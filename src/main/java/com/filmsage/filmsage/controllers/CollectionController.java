package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Collection;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.repositories.CollectionRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
        model.addAttribute("collections", collectionsDao.findAll());
        return "collect/index";
    }


//    to View an individual collection
    @GetMapping("/collections/{id}/show")
    public String getMovieCollection(@PathVariable long id, Model model) {
        model.addAttribute("collection", collectionsDao.getById(id));
        return "collect/show";
    }

/// show movie collection
    @GetMapping("/collections/create")
    public String createMovieCollection(Model model) {
        model.addAttribute("collection", new Collection());
        return "collect/create";
    }

    @PostMapping("/collections/create")
    public String submitMovieCollection(@ModelAttribute Collection collection) {
    collectionsDao.save(collection);
        return "collect/create";
    }

    @GetMapping("/collections/{id}/edit")
    public String editMovieCollection(@PathVariable long id, Model model) {
        Collection collection = collectionsDao.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (collection.getUserContent().getId() == user.getId()) {
            model.addAttribute("collection", collectionsDao.getById(id));
            return "collect/edit";
        } else {
            return "redirect:/collect";
        }
    }

    @PostMapping("/collections/{id}/edit")
    public String submitEditMovieCollection(@ModelAttribute Collection collection, @PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        collection.setUserContent(user.getUserContent());
        collectionsDao.save(collection);
        return "redirect:/collect";
    }

    @GetMapping("/collections/{id}/delete")
    public String deleteMCollection(@PathVariable long id) {
        Collection collection = collectionsDao.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(collection.getUserContent().getId() == user.getId()){
                collectionsDao.delete(collection);
            }
        return "redirect:/collect";
    }
    }
