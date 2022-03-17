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
public class WatchlistController {
    private WatchlistRepository watchlistDao;
    private UserRepository usersDao;
    private UserContentRepository userContentDao;
    private MediaItemService mediaItemService;

    public WatchlistController(WatchlistRepository watchlistDao, UserRepository usersDao, UserContentRepository userContentDao, MediaItemService mediaItemService) {
        this.usersDao = usersDao;
        this.watchlistDao = watchlistDao;
        this.userContentDao = userContentDao;
        this.mediaItemService = mediaItemService;
    }

    @GetMapping("/watchlist")
    public String viewWatchlists(Model model) {
        model.addAttribute("watchlist", watchlistDao.findAll());
        return "watchlist/index";
    }

//    to View an individual watchlist
    @GetMapping("/watchlist/{id}/show")
    public String getWatchlist(@PathVariable long id, Model model) {
        Watchlist watchlist = watchlistDao.getById(id);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("movies", watchlist.getMediaItems());
        return "watchlist/show";
    }

/// show movie watchlist
    @GetMapping("/watchlist/create")
    public String createWatchlist(Model model, @RequestParam(required = false) String imdb) {
        model.addAttribute("watchlist", new Watchlist());
        model.addAttribute("imdb", imdb);
        return "watchlist/create";
    }

    @PostMapping("/watchlist/create")
    public String submitWatchlist(@ModelAttribute Watchlist watchlist, @RequestParam(required = false, name = "imdb") String imdb) {
        watchlist.setUserContent(getUserContent());
        watchlist.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if (imdb != null) {
            MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
            watchlist.getMediaItems().add(mediaItem);
        }
        watchlistDao.save(watchlist);
        return "redirect:/watchlist";
    }

    // commented out for now because we are going to have to treat this different :/
    // gosh darn many-to-many relationships!!
//    @GetMapping("/watchlist/{id}/edit")
//    public String editWatchlist(@PathVariable long id, Model model) {
//        Watchlist watchlist = watchlistDao.getById(id);
//        if (watchlist.getUserContent().getId() == user.getId()) {
//            model.addAttribute("watchlist", watchlistDao.getById(id));
//            return "collect/edit";
//        } else {
//            return "redirect:/collect";
//        }
//    }
//
//    @PostMapping("/watchlist/{id}/edit")
//    public String submitEditWatchlist(@ModelAttribute Watchlist watchlist, @PathVariable long id) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        watchlist.setUserContent(user.getUserContent());
//        watchlistDao.save(watchlist);
//        return "redirect:/collect";
//    }

    @GetMapping("/watchlist/{id}/delete")
    public String deleteWatchlist(@PathVariable long id) {
        Watchlist watchlist = watchlistDao.getById(id);
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(watchlist.getUserContent().getId() == getUserContent().getId()){
                watchlistDao.delete(watchlist);
        }
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist/{id}/add")
    public String addToWatchlist(@PathVariable long id, @RequestParam String imdb, Model model) {
        MediaItem item = mediaItemService.getMediaItemRecord(imdb);
        Watchlist watchlist = watchlistDao.getById(id);
        watchlist.getMediaItems().add(item);
        watchlistDao.save(watchlist);
        model.addAttribute("watchlist", watchlist);
        return "redirect:/watchlist/" + id + "/show";
    }

    private UserContent getUserContent() {
        // note: this is slightly more complex than before, I apologize
        // step 1: get the UserPrinciple which contains account identifying info
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // step 2: get the UserContent object which links to all that user's user-created content
        return userContentDao.findUserContentByUser(principle.getUser());
    }
}
