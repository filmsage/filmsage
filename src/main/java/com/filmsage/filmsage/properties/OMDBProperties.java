package com.filmsage.filmsage.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class OMDBProperties {
    @Value("${APP_OMDB_KEY}")
    private String key;

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
