package com.filmsage.filmsage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {
    @Value("${SPRING_MAIL_HOST}")
    String host;

    @Value("${SPRING_MAIL_PORT}")
    int port;

    @Value("${SPRING_MAIL_USERNAME}")
    String username;

    @Value("${SPRING_MAIL_PASSWORD}")
    String password;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH}")
    boolean auth;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE}")
    boolean startTtls;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set up Gmail config
        mailSender.setHost(host);
        mailSender.setPort(port);

        // Set up email config
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTtls);
        return mailSender;
    }
}
