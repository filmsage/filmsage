package com.filmsage.filmsage.models.dto;

import com.filmsage.filmsage.annotation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UpdatePasswordDTO {
    @NotNull
    @NotEmpty
    private String oldpassword;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String confirmpassword;

    @NotNull
    @NotEmpty
    private long id;


    public UpdatePasswordDTO() {
    }

    public UpdatePasswordDTO(String oldpassword, String password, String confirmpassword, long id) {
        this.oldpassword = oldpassword;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.id = id;
    }

    public UpdatePasswordDTO(long id) {
        this.id = id;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
