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
        model.addAttribute("collections", collectionsDao.findAll());
        return "collect/index";
    }


//    to View an individual collection
    @GetMapping("/collections/{id}/show")
    public String getMovieCollection(@PathVariable long id, Model model) {
        model.addAttribute("collection", collectionsDao.getById(id));
        return "collect/show";
    }

////  view the form to create a new movie collection
    @GetMapping("/collections/create")
    public String createMovieCollection(Model model) {
        model.addAttribute("collection", new Collection());
        return "collect/create";
    }

    @PostMapping("/collections/create")
    public String createMovieCollection(@ModelAttribute Collection collection) {
    collectionsDao.save(collection);
        return "collect/create";
    }


    @GetMapping("/collections/{id}/edit")
    public String editMovieCollection(@PathVariable long id, Model model) {
        Collection collectionToEdit = collectionsDao.getById(id);
        Collection collection = (Collection)SecurityCotextHolder.getContext().getAuthentication().getPrincipal();
        if(collectionToEdit.getId() == collection.getId()) {
        model.addAttribute("collection", collectionsDao.getById(id));
        return "collect/edit";
    } else {
            return "redirect:/collect";
        }

    @PostMapping("/collections/{id}/edit")
    public String submitEditMovieCollection(@ModelAttribute Collection submitEditMovieCollection, @PathVariable long id) {
       collectionToEdit = collectionsDao.getById(id);
       collectionToEdit.setTitle(collectionToEdit).getTitle());



        return "redirect:/collect" + id;
    }

    @GetMapping("collections/{id}/delete")
    public String deleteMovieCollection(@PathVariable long id) {
        Collections collections = collectionsDao.getByIfd(id);
        User user = (User) Security
        collectionsDao.delete(collectionsDao.getById(id));
        return "redirect:/collect";
    }
}
