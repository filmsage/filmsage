package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.auth.UserDTO;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/register";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        // TODO: more custom validation annotations on UserDTO
        if (bindingResult.hasErrors()) {
            return "users/register";
        }
        if (userDao.existsUserByUsername(userDTO.getUsername())) {
            // TODO: inform user that the username can't be used
            return "users/register";
        }
        if (userDao.existsUserByEmail(userDTO.getEmail())) {
            // TODO: inform user the email can't be used
            return "users/register";
        }
        // map the form details into a new User for our persistence layer
        User user = new User(
                userDTO.getEmail(),
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                new Timestamp(System.currentTimeMillis())
        );
        userDao.save(user);
        return "redirect:/login";
    }
}