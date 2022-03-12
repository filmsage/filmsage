package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.auth.UserDTO;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.UserDetailsLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private UserDetailsLoader userDetailsLoader;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, UserDetailsLoader userDetailsLoader) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsLoader = userDetailsLoader;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute @Valid UserDTO userDTO) {
        if (userDao.existsUserByUsername(userDTO.getUsername())) {
            // TODO: some page or route to send new users to if they try to register an account w/ a taken username
            return "index";
        }
        if (userDao.existsUserByEmail(userDTO.getEmail())) {
            // TODO: some page or route to send new users to if they try to register an account w/ a taken email
            return "index";
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