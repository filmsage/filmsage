package com.filmsage.filmsage.controllers;
import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.JournalRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@Controller
public class JournalController {
    private JournalRepository journalDao;
    private UserRepository userDao;
    private UserContentRepository userContentDao;

    public JournalController(UserRepository userDao, JournalRepository journalDao, UserContentRepository userContentDao) {
        this.userDao = userDao;
        this.journalDao = journalDao;
        this.userContentDao = userContentDao;
    }

    @GetMapping("/journals")
    public String showJournals(Model model,
                               @RequestParam(required = false) String id,
                               @RequestParam(required = false) String user,
                               @RequestParam(required = false) String username) {
        if (StringUtils.hasText(id)) {
            model.addAttribute("journal", journalDao.getById(Long.parseLong(id)));
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

    @PostMapping("/journals/create")
    public String submitCreate(@ModelAttribute Journal journal) {
        journal.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        journal.setUserContent(getUserContent());
        journal = journalDao.save(journal);
        return "redirect:/journals?id=" + journal.getId();
    }

    @GetMapping("/journals/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        Journal journal = journalDao.getById(id);
        if (journal.getUserContent().getId() == getUserContent().getId()) {
            model.addAttribute("journal", journal);
            return "journals/edit";
        } else {
            return "redirect:/journals";
        }
    }


    @PostMapping("/journals/{id}/edit")
    public String submitEdit(@ModelAttribute Journal journal, @PathVariable long id) {
        journal.setUserContent(getUserContent());
        journal.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        journalDao.save(journal);
        return "redirect:/journals?id=" + journal.getId();
    }

    // this function defines that when a user attempts to delete a journal,
    // we first look up that journal’s id from the url.
    // We then check if the user who’s currently on the page from the session of their browser is also the user who created the journal.
    // If they are,we delete the journal from our journals table.
    // Whether they are the right user or not, we redirect them back to the journals index.

    @GetMapping("/journals/{id}/delete")
    public String deleteJournal(@PathVariable long id) {
        Journal journal = journalDao.getById(id);
        if (journal.getUserContent().getId() == getUserContent().getId()) {
            journalDao.delete(journal);
        }
        return "redirect:/journals";
    }

    private UserContent getUserContent() {
        // note: this is slightly more complex than before, I apologize
        // step 1: get the UserPrinciple which contains account identifying info
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // step 2: get the UserContent object which links to all that user's user-created content
        return userContentDao.findUserContentByUser(principle.getUser());
    }

}
