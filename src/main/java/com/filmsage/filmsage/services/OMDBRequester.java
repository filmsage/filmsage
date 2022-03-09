package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.MediaItemMapped;
import com.filmsage.filmsage.omdb.OMDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OMDBRequester {

    private final OMDBProperties omdbProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public OMDBRequester(OMDBProperties omdbProperties, RestTemplate restTemplate) {
        this.omdbProperties = omdbProperties;
        this.restTemplate = restTemplate;
    }

    public MediaItemMapped getMovie(String imdbId) {
        return restTemplate.getForObject("https://www.omdbapi.com/?apikey={apikey}&i={imdbId}", MediaItemMapped.class, this.omdbProperties.getKey(), imdbId);
    }
}
