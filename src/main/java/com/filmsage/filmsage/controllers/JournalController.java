package com.filmsage.filmsage.controllers;
import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.JournalRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;


@Controller
public class JournalController {
    private JournalRepository journalDao;
    private UserRepository userDao;
    private UserContentRepository userContentDao;
    private UserContentService userContentService;

    public JournalController(UserRepository userDao, JournalRepository journalDao, UserContentRepository userContentDao, UserContentService userContentService) {
        this.userDao = userDao;
        this.journalDao = journalDao;
        this.userContentDao = userContentDao;
        this.userContentService = userContentService;
    }

    @GetMapping("/journals")
    public String showJournals(Model model,
                               Principal principal,
                               @RequestParam(required = false) String id,
                               @RequestParam(required = false) String user,
                               @RequestParam(required = false) String username) {
        if (StringUtils.hasText(id)) {
            Journal journal = journalDao.getById(Long.parseLong(id));
            model.addAttribute("journal", journal);
            if (principal != null) {
                if (journal.getUserContent().getId() == userContentService.getUserContent().getId() ||
                        userContentService.isAdmin()) {
                    model.addAttribute("canModify", true);
                }
            }
            return "journals/show";
        } else if (StringUtils.hasText(user)) {
            model.addAttribute("journals", journalDao.findAllByUserContent_Id(Long.parseLong(user)));
        } else if (StringUtils.hasText(username)) {
            model.addAttribute("journals",
                    journalDao.findAllByUserContent_User(userDao.findByUsername(username)) );
        } else {
            model.addAttribute("journals", journalDao.findAll());
        }
        return "journals/index";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/journals/create")
    public String showJournalCreateForm(Model model) {
        model.addAttribute("journal", new Journal());
        return "journals/create";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/journals/create")
    public String submitCreate(@ModelAttribute Journal journal) {
        journal.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        journal.setUserContent(userContentService.getUserContent());
        journal = journalDao.save(journal);
        return "redirect:/journals?id=" + journal.getId();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/journals/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        Journal journal = journalDao.getById(id);
        if (journal.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            model.addAttribute("journal", journal);
            return "journals/edit";
        } else {
            return "redirect:/journals";
        }
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/journals/{id}/edit")
    public String submitEdit(@ModelAttribute Journal journal, @PathVariable long id) {
        Journal existingJournal = journalDao.getById(id);
        if (existingJournal.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            existingJournal.setTitle(journal.getTitle());
            existingJournal.setBody(journal.getBody());
            journalDao.save(existingJournal);
        }
        return "redirect:/journals?id=" + existingJournal.getId();
    }

    // this function defines that when a user attempts to delete a journal,
    // we first look up that journal’s id from the url.
    // We then check if the user who’s currently on the page from the session of their browser is also the user who created the journal.
    // If they are,we delete the journal from our journals table.
    // Whether they are the right user or not, we redirect them back to the journals index.
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/journals/{id}/delete")
    public String deleteJournal(@PathVariable long id) {
        Journal journal = journalDao.getById(id);
        if (journal.getUserContent().getId() == userContentService.getUserContent().getId() ||
                userContentService.isAdmin()) {
            journalDao.delete(journal);
        }
        return "redirect:/journals";
    }

}
