package com.filmsage.filmsage.controllers;
import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.repositories.JournalRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class JournalController {
    private JournalRepository journalDao;
    private UserRepository userDao;


    public JournalController(UserRepository userDao, JournalRepository journalDao) {
        this.userDao = userDao;
        this.journalDao = journalDao;
    }

    @GetMapping("/journals")
    public String showJournals(Model model) {
        model.addAttribute("journals", journalDao.findAll());
        return "journals/index";
    }

    @GetMapping("/journals/{id}")
    public String showJournal(@PathVariable long id, Model model) {
        model.addAttribute("journal", journalDao.getById(id));
        return "journals/show";
    }

    @GetMapping("/journals/create")
    public String showJournalCreateForm(Model model) {
        model.addAttribute("journal", new Journal());
        return "journals/create";
    }

    @PostMapping("/journals/create")
    public String submitCreate(@ModelAttribute Journal journal) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        journal.setUser(user);
        journalDao.save(journal);
        return "redirect:/journals";
    }

    @GetMapping("/journals/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        Journal journal = journalDao.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (journal.getUser().getId() == user.getId()) {
            model.addAttribute("journalToEdit", journal);
            return "journals/edit";
        } else {
            return "redirect:/journals";
        }
    }


    @PostMapping("/journals/{id}/edit")
    public String submitEdit(@ModelAttribute Journal journal, @PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        journal.setUser(user);
        journalDao.save(journal);

        return "redirect:/journals";
    }

    // this function defines that when a user attempts to delete a journal,
    // we first look up that journal’s id from the url.
    // We then check if the user who’s currently on the page from the session of their browser is also the user who created the journal.
    // If they are,we delete the journal from our journals table.
    // Whether they are the right user or not, we redirect them back to the journals index.

    @GetMapping("/journals/{id}/delete")
    public String deleteJournal(@PathVariable long id) {
        Journal journal = journalDao.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (journal.getUser().getId() == user.getId()) {
            journalDao.delete(journal);
        }
        return "redirect:/journals";
    }

}
