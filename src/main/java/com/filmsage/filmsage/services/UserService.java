package com.filmsage.filmsage.services;

import com.filmsage.filmsage.exception.UserAlreadyExistException;
import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.VerificationToken;
import com.filmsage.filmsage.models.dto.UserDTO;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.repositories.VerificationTokenRepository;
import com.filmsage.filmsage.repositories.auth.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;

@Service
@Transactional
public class UserService implements IUserService {
    private UserRepository userDao;

    private VerificationTokenRepository tokenDao;

    private RoleRepository roleDao;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userDao,
                       VerificationTokenRepository tokenDao,
                       RoleRepository roleDao,
                       PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is already an account with that email: " + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setRoles(Arrays.asList(roleDao.findByName("ROLE_USER")));
        return userDao.save(user);
    }

    private boolean emailExist(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenDao.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenDao.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userDao.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenDao.save(myToken);
    }

//     old
//    @Override
//    public User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException {
//        if (emailExist(userDto.getEmail())) {
//            throw new UserAlreadyExistException(
//                    "There is an account with that email adress: "
//                            + userDto.getEmail());
//        }
//
//        User user = new User(
//                userDto.getEmail(),
//                userDto.getUsername(),
//                passwordEncoder.encode(userDto.getPassword()),
//                new Timestamp(System.currentTimeMillis())
//        );
//        user.setRole(new Role(Integer.valueOf(1), user));
//        return userDao.save(user);
//    }

}
