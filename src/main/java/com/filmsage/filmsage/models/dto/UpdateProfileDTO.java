package com.filmsage.filmsage.models.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateProfileDTO {
    @NotNull
    @NotEmpty
    private String profile;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String email;


    @NotNull
    @NotEmpty
    private long id;

    public UpdateProfileDTO() {
    }

    public UpdateProfileDTO(String profile, String username, String firstName, String email, long id) {
        this.profile = profile;
        this.username = username;
        this.firstName = firstName;
        this.email = email;
        this.id = id;
    }

//    public updateProfileDTO (long id) {
//        this.id = id;
//    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

