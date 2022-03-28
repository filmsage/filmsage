package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.dto.UpdatePasswordDTO;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.repositories.WatchlistRepository;

import com.filmsage.filmsage.services.UserContentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private UserContentRepository userContentDao;
    private WatchlistRepository watchlistDao;
    private UserContentService userContentService;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, UserContentRepository userContentDao, WatchlistRepository watchlistDao, UserContentService userContentService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userContentDao = userContentDao;
        this.watchlistDao = watchlistDao;
        this.userContentService = userContentService;
    }

    @GetMapping("/update-password/{id}")
    public String showUpdatePassword(@PathVariable long id, Model model, Principal principal){
        if (principal != null) {
            if (userContentService.getUserContent().getId() == id) {
                model.addAttribute("userPassword", new UpdatePasswordDTO(id));
                return "users/update-password";
            }
        }
        return "redirect:/profile/" + id;
    }

    @PostMapping("/update-password")
    public String updatePassword(@ModelAttribute("userPassword") UpdatePasswordDTO updatePasswordDTO ){

        User user = userDao.getById(updatePasswordDTO.getId());
        if (user.getId() == userContentService.getUserContent().getId()) {
            if (passwordEncoder.matches(updatePasswordDTO.getOldpassword(), user.getPassword()) &&
                    updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmpassword())) {
                user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
                userDao.save(user);
            }
        }

        return "redirect:/profile/" + user.getId();

    }

}