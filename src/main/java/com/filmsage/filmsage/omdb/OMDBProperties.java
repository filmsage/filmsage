package com.filmsage.filmsage.omdb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app.omdb")
@Component
public class OMDBProperties {
    private String key = "";

    public OMDBProperties(String key) {
        this.key = key;
    }

    public OMDBProperties() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
