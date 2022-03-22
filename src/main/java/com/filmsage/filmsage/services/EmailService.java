package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailService {

    public JavaMailSender emailSender;

//    @Value("${SPRING_MAIL_FROM}")
//    private String from;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void prepareAndSend(User user, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("registration@filmsage.net");
        msg.setTo(user.getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        try{
            this.emailSender.send(msg);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}