package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Watchlist;
import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.*;
import com.filmsage.filmsage.services.MediaItemService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class CollectionController {
    private CollectionRepository collectionsDao;
    private UserRepository usersDao;
    private UserContentRepository userContentDao;
    private MediaItemService mediaItemService;

    public CollectionController(CollectionRepository collectionsDao, UserRepository usersDao, UserContentRepository userContentDao, MediaItemService mediaItemService) {
        this.usersDao = usersDao;
        this.collectionsDao = collectionsDao;
        this.userContentDao = userContentDao;
        this.mediaItemService = mediaItemService;
    }

    @GetMapping("/collections")
    public String viewCollections(Model model) {
        model.addAttribute("collections", collectionsDao.findAll());
        return "collect/index";
    }

//    to View an individual collection
    @GetMapping("/collections/{id}/show")
    public String getMovieCollection(@PathVariable long id, Model model) {
        Watchlist watchlist = collectionsDao.getById(id);
        model.addAttribute("collection", watchlist);
        model.addAttribute("movies", watchlist.getMediaItems());
        return "collect/show";
    }

/// show movie collection
    @GetMapping("/collections/create")
    public String createMovieCollection(Model model) {
        model.addAttribute("collection", new Watchlist());
        return "collect/create";
    }

    @PostMapping("/collections/create")
    public String submitMovieCollection(@ModelAttribute Watchlist watchlist) {
        watchlist.setUserContent(getUserContent());
        watchlist.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        collectionsDao.save(watchlist);
        return "collect/create";
    }

    // commented out for now because we are going to have to treat this different :/
    // gosh darn many-to-many relationships!!
//    @GetMapping("/collections/{id}/edit")
//    public String editMovieCollection(@PathVariable long id, Model model) {
//        Watchlist collection = collectionsDao.getById(id);
//        if (collection.getUserContent().getId() == user.getId()) {
//            model.addAttribute("collection", collectionsDao.getById(id));
//            return "collect/edit";
//        } else {
//            return "redirect:/collect";
//        }
//    }
//
//    @PostMapping("/collections/{id}/edit")
//    public String submitEditMovieCollection(@ModelAttribute Watchlist collection, @PathVariable long id) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        collection.setUserContent(user.getUserContent());
//        collectionsDao.save(collection);
//        return "redirect:/collect";
//    }

    @GetMapping("/collections/{id}/delete")
    public String deleteCollection(@PathVariable long id) {
        Watchlist watchlist = collectionsDao.getById(id);
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(watchlist.getUserContent().getId() == getUserContent().getId()){
                collectionsDao.delete(watchlist);
        }
        return "redirect:/collections";
    }

    @GetMapping("/collections/{id}/add")
    public String addToCollection(@PathVariable long id, @RequestParam String imdb, Model model) {
        MediaItem item = mediaItemService.getMediaItemRecord(imdb);
        Watchlist watchlist = collectionsDao.getById(id);
        watchlist.getMediaItems().add(item);
        collectionsDao.save(watchlist);
        model.addAttribute("collection", watchlist);
        return "redirect:/collections/" + id + "/show";
    }

    private UserContent getUserContent() {
        // note: this is slightly more complex than before, I apologize
        // step 1: get the UserPrinciple which contains account identifying info
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // step 2: get the UserContent object which links to all that user's user-created content
        return userContentDao.findUserContentByUser(principle.getUser());
    }
}
