package com.filmsage.filmsage.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountProperties {
    @Value("${APP_ADMIN_EMAIL}")
    private String email;
    @Value("${APP_ADMIN_PASSWORD}")
    private String password;
    @Value("${APP_ADMIN_USERNAME}")
    private String username;

    public AdminAccountProperties() {
    }

    public AdminAccountProperties(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
