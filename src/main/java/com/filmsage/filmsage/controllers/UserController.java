package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.dto.UpdatePasswordDTO;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.repositories.WatchlistRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private UserContentRepository userContentDao;
    private WatchlistRepository watchlistDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, UserContentRepository userContentDao, WatchlistRepository watchlistDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userContentDao = userContentDao;
        this.watchlistDao = watchlistDao;
    }

    @GetMapping("/update-password/{id}")
    public String showUpdatePassword(@PathVariable long id, Model model){
        model.addAttribute("userPassword", new UpdatePasswordDTO(id));
        return "/update-password";
    }

    @PostMapping("/update-password")
//    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String updatePassword(@ModelAttribute("userPassword") UpdatePasswordDTO updatePasswordDTO ){
        User user = userDao.getById(updatePasswordDTO.getId());
        String hash = user.getPassword();
        System.out.println("hash password");
        if( passwordEncoder.matches(updatePasswordDTO.getOldpassword(), user.getPassword())){
            System.out.println("old password + form password");
            if (updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmpassword())) {
                System.out.println("password and confirmed password are equal");
                String newHash = passwordEncoder.encode(updatePasswordDTO.getPassword());
                user.setPassword(newHash);
                userDao.save(user);
                System.out.println("updated user");
            }
        }
        return "redirect:/profile/" + updatePasswordDTO.getId();
    }

}