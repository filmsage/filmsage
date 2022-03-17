package com.filmsage.filmsage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.models.json.MediaSearchMapped;
import com.filmsage.filmsage.models.json.SearchMapped;
import com.filmsage.filmsage.properties.OMDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OMDBRequester {

    private final OMDBProperties omdbProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OMDBRequester(OMDBProperties omdbProperties, RestTemplate restTemplate) {
        this.omdbProperties = omdbProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public MediaItemMapped getMovie(String imdbId) {
        return restTemplate.getForObject("https://www.omdbapi.com/?apikey={apikey}&i={imdbId}", MediaItemMapped.class, this.omdbProperties.getKey(), imdbId);
    }

    public List<MediaSearchMapped> searchMovie(String query) {
        SearchMapped search = restTemplate.getForObject("https://www.omdbapi.com/?apikey={apikey}&s={query}", SearchMapped.class, this.omdbProperties.getKey(), query);
        List<MediaSearchMapped> results = new LinkedList<>();
        if (search.getSearchResults().length > 0) {
            results = Arrays.stream(search.getSearchResults()).collect(Collectors.toList());
        }
        return results;
    }
}
