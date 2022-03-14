package com.filmsage.filmsage.services;

import com.filmsage.filmsage.exception.UserAlreadyExistException;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.VerificationToken;
import com.filmsage.filmsage.models.dto.UserDTO;

public interface IUserService {

    User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}