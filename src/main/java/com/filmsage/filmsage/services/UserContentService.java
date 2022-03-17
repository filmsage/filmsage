package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.UserPrinciple;
import com.filmsage.filmsage.repositories.UserContentRepository;
import com.filmsage.filmsage.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContentService {

    private UserContentRepository userContentDao;
    private UserRepository userRepository;

    public UserContentService(UserContentRepository userContentDao, UserRepository userRepository) {
        this.userContentDao = userContentDao;
        this.userRepository = userRepository;
    }

    // this method will get the currently authenticated user's content from the database
    public UserContent getUserContent() {
        // note: this is slightly more complex than before, I apologize
        // step 1: get the UserPrinciple which contains account identifying info
        UserPrinciple principle = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // step 2: get the UserContent object which links to all that user's user-created content
        return userContentDao.findUserContentByUser(principle.getUser());
    }

}
