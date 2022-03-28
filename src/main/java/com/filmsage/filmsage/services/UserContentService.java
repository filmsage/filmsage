package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.Role;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserContentService {

    private UserContentRepository userContentDao;
    private UserRepository userDao;

    public UserContentService(UserContentRepository userContentDao, UserRepository userDao) {
        this.userContentDao = userContentDao;
        this.userDao = userDao;
    }

    // this method will get the currently authenticated user's content from the database
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserContent getUserContent() {
        // note: this is slightly more complex than before, I apologize
        // step 1: get the UserPrinciple which contains account identifying info
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // step 2: get the UserContent object which links to all that user's user-created content
        return userContentDao.findUserContentByUser(principle.getUser());
    }

    public User getUser() {
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principle.getUser();
    }

    public UserContent save(UserContent user) {
        return userContentDao.save(user);
    }

    public boolean isAdmin() {
        return getUser().getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_ADMIN");
    }
}
