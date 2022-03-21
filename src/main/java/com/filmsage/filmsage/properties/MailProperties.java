package com.filmsage.filmsage.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailProperties {
    @Value("${SPRING_MAIL_HOST}")
    String host;

    @Value("${SPRING_MAIL_PORT}")
    String port;

    @Value("${SPRING_MAIL_USERNAME}")
    String username;

    @Value("${SPRING_MAIL_PASSWORD}")
    String password;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH}")
    boolean auth;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE}")
    boolean startTtls;

    public MailProperties(String host, String port, String username, String password, boolean auth, boolean startTtls) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.startTtls = startTtls;
    }

    public MailProperties() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isStartTtls() {
        return startTtls;
    }

    public void setStartTtls(boolean startTtls) {
        this.startTtls = startTtls;
    }
}
