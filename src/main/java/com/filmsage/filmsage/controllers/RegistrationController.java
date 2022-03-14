package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.event.event.OnRegistrationComplete;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.VerificationToken;
import com.filmsage.filmsage.models.dto.UserDTO;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.services.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

@Controller
public class RegistrationController {

    private IUserService service;
    private UserRepository userDao;
    ApplicationEventPublisher eventPublisher;

    public RegistrationController(IUserService service, UserRepository userDao, ApplicationEventPublisher eventPublisher) {
        this.service = service;
        this.userDao = userDao;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/registration-confirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

//        Locale locale = request.getLocale();

        VerificationToken verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
            //String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", "Invalid token");
            return "users/registration-error";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            // String messageValue = messages.getMessage("auth.message.expired", null, locale)
            model.addAttribute("message", "Token expired");
            return "users/registration-error";
        }

        user.setEnabled(true);
        service.saveRegisteredUser(user);
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/register";
    }

    @PostMapping("/register")
    public String submitUserRegistration(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model, HttpServletRequest request) {
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

        User registered = service.registerNewUserAccount(userDTO);
        String url = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationComplete(registered, request.getLocale(), url));
        // map the form details into a new User for our persistence layer
//        User user = new User(
//                userDTO.getEmail(),
//                userDTO.getUsername(),
//                passwordEncoder.encode(userDTO.getPassword()),
//                new Timestamp(System.currentTimeMillis())
//        );
//        userDao.save(user);
        return "users/register-email-confirmation";
    }
}
