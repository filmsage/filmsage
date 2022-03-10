package com.filmsage.filmsage.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmsage.filmsage.models.MediaItemMapped;
import com.filmsage.filmsage.models.MediaSearchMapped;
import com.filmsage.filmsage.models.SearchMapped;
import com.filmsage.filmsage.omdb.OMDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
//        Object json = restTemplate.getForEntity("https://www.omdbapi.com/?apikey={apikey}&s={imdbId}", Object[].class, this.omdbProperties.getKey(), query)
//        return objectMapper.readValue(json, new TypeReference<List<MediaSearchMapped>>(){})
//        ResponseEntity<MediaSearchMapped[]> responseEntity =  restTemplate.getForEntity("https://www.omdbapi.com/?apikey={apikey}&s={imdbId}", MediaSearchMapped[].class, this.omdbProperties.getKey(), query);
        //String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://www.omdbapi.com/?apikey={apikey}&s={imdbId}")
//        ResponseEntity<List<MediaSearchMapped>> responseEntity =
//                restTemplate.exchange(
//                        "https://www.omdbapi.com/?apikey={apikey}&s={imdbId}",
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<MediaSearchMapped>>() {},
//                        this.omdbProperties.getKey(),
//                        query
//                );
        //MediaSearchMapped[] arr = responseEntity.getBody();
        //return responseEntity.getBody();

//        ResponseEntity<List<SearchMapped>> responseEntity =
//                restTemplate.exchange(
//                        "https://www.omdbapi.com/?apikey={apikey}&s={imdbId}",
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<SearchMapped>>() {},
//                        this.omdbProperties.getKey(),
//                        query
//                );
        SearchMapped search = restTemplate.getForObject("https://www.omdbapi.com/?apikey={apikey}&s={query}", SearchMapped.class, this.omdbProperties.getKey(), query);
        List<MediaSearchMapped> results = new LinkedList<>();
        if (search.getSearchResults().length > 0) {
            results = Arrays.stream(search.getSearchResults()).collect(Collectors.toList());
        }
        return results;
    }
}
