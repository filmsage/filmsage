package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Watchlist;
import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.*;
import com.filmsage.filmsage.services.MediaItemService;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class WatchlistController {
    private WatchlistRepository watchlistDao;
    private UserRepository usersDao;
    private UserContentRepository userContentDao;
    private MediaItemService mediaItemService;
    private UserContentService userContentService;

    public WatchlistController(WatchlistRepository watchlistDao, UserRepository usersDao, UserContentRepository userContentDao, MediaItemService mediaItemService, UserContentService userContentService) {
        this.usersDao = usersDao;
        this.watchlistDao = watchlistDao;
        this.userContentDao = userContentDao;
        this.mediaItemService = mediaItemService;
        this.userContentService = userContentService;
    }

    @GetMapping("/watchlist")
    public String viewWatchlists(Model model, @RequestParam(required = false) String user, @RequestParam(required = false) String imdb) {
        if (user != null && imdb == null) {
            model.addAttribute("watchlists", watchlistDao.findWatchlistsByUserContent_Id(Long.parseLong(user)));
            model.addAttribute("user", userContentDao.getById(Long.parseLong(user)));
            model.addAttribute("for", "user");
        } else if (user == null && imdb != null) {
            model.addAttribute("watchlists", watchlistDao.findAllByMediaItems_Imdb(imdb));
            model.addAttribute("movie", mediaItemService.getTempMediaItemRecord(imdb));
            model.addAttribute("for", "movie");
        } else if (user == null && imdb == null) {
            model.addAttribute("watchlists", watchlistDao.findAll());
            model.addAttribute("for", "all");
        }
        return "watchlist/index";
    }

    //    to View an individual watchlist
    @GetMapping("/watchlist/{id}/show")
    public String getWatchlist(@PathVariable long id, Model model, Principal principal) {
        Watchlist watchlist = watchlistDao.getById(id);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("mediaItems", watchlist.getMediaItems());
        if (principal != null) {
            if (watchlist.getUserContent().getId() == userContentService.getUserContent().getId() ||
                    userContentService.isAdmin()) {
                model.addAttribute("canDelete", true);
            }
        }
        return "watchlist/show";
    }

    /// show movie watchlist
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/watchlist/create")
    public String createWatchlist(Model model, @RequestParam(required = false) String imdb) {
        model.addAttribute("watchlist", new Watchlist());
        model.addAttribute("imdb", imdb);
        return "watchlist/create";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/watchlist/create")
    public String submitWatchlist(@ModelAttribute Watchlist watchlist, @RequestParam(required = false, name = "imdb") String imdb) {
        watchlist.setUserContent(userContentService.getUserContent());
        watchlist.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if (imdb != null) {
            MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
            watchlist.getMediaItems().add(mediaItem);
        }
        watchlistDao.save(watchlist);
        return "redirect:/watchlist?user=" + watchlist.getUserContent().getId();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/watchlist/{id}/delete")
    public String deleteWatchlist(@PathVariable long id) {
        Watchlist watchlist = watchlistDao.getById(id);
        if (watchlist.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            watchlistDao.delete(watchlist);
        }
        return "redirect:/watchlist";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/watchlist/delete")
    public String deleteWatchlistButton(@RequestParam long id) {
        Watchlist watchlist = watchlistDao.getById(id);
        if (watchlist.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            watchlistDao.delete(watchlist);
        }
        return "redirect:/watchlist";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/watchlist/{id}/add")
    public String addToWatchlist(@PathVariable long id, @RequestParam String imdb, Model model) {
        MediaItem item = mediaItemService.getMediaItemRecord(imdb);
        Watchlist watchlist = watchlistDao.getById(id);
        if (watchlist.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            watchlist.getMediaItems().add(item);
            watchlistDao.save(watchlist);
        }
        model.addAttribute("watchlist", watchlist);
        return "redirect:/watchlist/" + id + "/show";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/watchlist/deleteitem")
    public String deleteFromWatchlist(@RequestParam long id, @RequestParam String imdb){
        Watchlist watchlist = watchlistDao.getById(id);
        MediaItem mediaItem = mediaItemService.getMediaItemRecord(imdb);
        if (watchlist.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            watchlist.getMediaItems().remove(mediaItem);
            watchlistDao.save(watchlist);
        }
        return "redirect:/watchlist/" + id + "/show";
    }

}


