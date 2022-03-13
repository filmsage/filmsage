package com.filmsage.filmsage.event.listener;

import com.filmsage.filmsage.event.event.OnRegistrationComplete;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.services.UserDetailsLoader;
import com.filmsage.filmsage.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationComplete> {
    private UserService service;

    private MessageSource messages;

    private JavaMailSender mailSender;

    // TODO: we will need to replace this when we deploy
    private String applicationUrl = "http://localhost:8080";

    public RegistrationListener(UserService service, MessageSource messages, JavaMailSender mailSender) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationComplete event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationComplete event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "FilmSage - Confirm Your Account";
        String confirmationUrl = event.getAppUrl() + "/registration-confirm.html?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + applicationUrl + confirmationUrl);
        mailSender.send(email);
    }
}
