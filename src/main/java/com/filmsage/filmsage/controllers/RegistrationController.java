package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.VerificationToken;
import com.filmsage.filmsage.services.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class RegistrationController {

    private IUserService service;

    public RegistrationController(IUserService service) {
        this.service = service;
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
}
