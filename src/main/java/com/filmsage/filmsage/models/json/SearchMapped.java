package com.filmsage.filmsage.models.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMapped {
    @JsonProperty("totalResults")
    private int totalResults;
    @JsonProperty("Search")
    private MediaSearchMapped[] searchResults;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public MediaSearchMapped[] getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(MediaSearchMapped[] searchResults) {
        this.searchResults = searchResults;
    }
}
