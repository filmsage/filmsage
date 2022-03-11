package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.repositories.JournalRepository;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserRepository;
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



    public JournalController(UserRepository userDao, JournalRepository journalDao){
        this.userDao = userDao;
        this.journalDao = journalDao;
    }

    @GetMapping("/journals")
    public String showJournals(Model model) {
        model.addAttribute("journals", journalDao.findAll());
        return "journals/index";
    }

    @GetMapping("/journals/{id}")
    public String showJournal(@PathVariable long id, Model model){
        model.addAttribute("journals",journalDao.getById(id));
        return "journals/show";
    }

    @GetMapping("/journals/create")
    public String showJournalCreateForm(Model model) {
        model.addAttribute("journal", new Journal());
            return "journals/create";
        }

    @GetMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable long id) {
        journalDao.delete(journalDao.getById(id));
        return "redirect:/reviews";
    }

    @GetMapping("/journals/{id}/delete")
    public String deleteJournal(@PathVariable long id) {
        journalDao.delete(journalDao.getById(id));
        return "redirect:/journals";
    }

}
